package ch.lsh.vga_card.serial;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.fazecast.jSerialComm.SerialPort;

public class ConnectionEstablisherAgent {

    private SerialPort port;
    private InputStreamReader inStream;
    private BufferedWriter outStream;
    private int baudRate;
    private String version;

    private int statusCode = -1;

    public ConnectionEstablisherAgent(int serialPortIndex) {
        this.port = SerialPort.getCommPorts()[serialPortIndex];
        port.openPort();
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        inStream  = new InputStreamReader(port.getInputStream());
        outStream = new BufferedWriter(new OutputStreamWriter(port.getOutputStream()));
    }

    public boolean connect() {
        if(createHandshake()) {
            statusCode = 0;
            return true;
        } else {
            statusCode = 1;
            port.closePort();

            port.openPort();
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

            inStream  = new InputStreamReader(port.getInputStream());
            outStream = new BufferedWriter(new OutputStreamWriter(port.getOutputStream()));

            return createHandshake();
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getVersion() {
        return version;
    }

    public int getBaudRate() {
        return baudRate;
    }

    private String readLine() {
        try {
        char c;
        String s = "";
        do {
                c = (char) inStream.read();
                if (c == '\n')
                    break;
                s += c + "";
            } while (c != -1);
            return s;
        }  catch (IOException e) {
            
        }
        return "";
    }

    private boolean createHandshake() {
        try {
            if(!readLine().trim().endsWith("vga_card"))
                return false;
            outStream.write("ACK");
            outStream.flush();

            String data = readLine().trim();


            String[] dataSt = data.split(" ");
            if(dataSt.length != 2)
                return false;

            baudRate = Integer.parseInt(dataSt[0]);
            version = dataSt[1];
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public SerialPort getPort() {
        return port;
    }

}
