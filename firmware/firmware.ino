int adressLines[] = { 21, 47, 35, 36, 37, 38, 39, 40, 41, 42, 2, 1, 7, 6, 5, 4, 15, 16, 17, 18 };

int outputLines[] = { 8, 3, 9, 10, 11, 12 };

int writeEnabled = 13;


long baud = 3000000;
String version = "0.1";

void setup() {

  Serial.begin(baud);
  while (!Serial) { int i; }
  delay(3000);

  for (int i = 0; i < 20; i++) {
    pinMode(adressLines[i], OUTPUT);
  }

  for (int i = 0; i < 6; i++) {
    pinMode(outputLines[i], OUTPUT);
  }

  pinMode(writeEnabled, INPUT);

  handleInit();
}

bool handleInit() {
  Serial.println("vga_card");
  Serial.flush();
  while (Serial.available() == 0) {}
  String m1 = Serial.readString();
  m1.trim();
  if (m1 != "ACK")
    return false;


  Serial.print(baud);
  Serial.print(" ");
  Serial.println(version);
  Serial.flush();
  while (Serial.available() == 0) {}
  String m2 = Serial.readString();
  m2.trim();
  if (m2 != "OK")
    return false;

  return true;
}




void handleImageMode(int mode, uint8_t * array) {
  // MODE_IMAGE_SINGLE
  if (mode == 0x11) {
    Serial.readBytes(array, 800 * 600);
  }
}

long indexX = 0;
long indexY = 0;

long counter = 0;

void loop() {
  int data = Serial.read();
  if (data == 0x1C) {
    Serial.write(0x0E);
    Serial.flush();
  } else if (data == 0x22) {
    Serial.write(0x0E);
    Serial.flush();
    data = Serial.read();

    uint8_t imData[800*600];
    handleImageMode(data, imData);
    indexX = 0;
    indexY = 0;
    counter = 0;

    while (true) {
      // Wait until writing is permited
      while (digitalRead(writeEnabled) != 0)
        ;
      while (digitalRead(writeEnabled) != 1)
        ;

      // Writing

      // building adress from x and y indecies
      long adress = (indexX) | (indexY << 10);

      write(adress, imData[counter]);

      counter++;
      indexX++;
      if (indexX > 800) {
        indexX = 0;
        indexY++;
        if (indexY > 600) {
          indexY = 0;
          indexX = 0;
          counter = 0;
        }
      }
    }
  }
}

// Write givven data to given adress
void write(unsigned long adress, int data) {
  if (adress < 1048576) {
    for (int i = 0; i < 20; i++) {
      int addrBit = bitRead(adress, i);
      if (addrBit == 0) {
        directWriteLow(adressLines[i]);
      } else {
        directWriteHigh(adressLines[i]);
      }
    }
    for (int i = 0; i < 6; i++) {
      int dataBit = bitRead(data, i);
      if (dataBit == 0) {
        directWriteLow(outputLines[i]);
      } else {
        directWriteHigh(outputLines[i]);
      }
    }
  }
}

// Faster writing low
inline __attribute__((always_inline)) void directWriteLow(int pin) {
  if (pin < 32)
    GPIO.out_w1tc = ((uint32_t)1 << pin);
  else if (pin < 34)
    GPIO.out1_w1tc.val = ((uint32_t)1 << (pin - 32));
}

// Faster writing high
inline __attribute__((always_inline)) void directWriteHigh(int pin) {
  if (pin < 32)
    GPIO.out_w1ts = ((uint32_t)1 << pin);
  else if (pin < 34)
    GPIO.out1_w1ts.val = ((uint32_t)1 << (pin - 32));
}
