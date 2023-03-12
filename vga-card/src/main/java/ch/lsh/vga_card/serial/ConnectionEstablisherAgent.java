package ch.lsh.vga_card.serial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.fazecast.jSerialComm.SerialPort;

public class ConnectionEstablisherAgent {

    private int baud;

    private SerialPort port;
    private BufferedReader inStream;
    private BufferedWriter outStream;
    private int baudRate;
    private String version;

    private int statusCode = -1;

    public ConnectionEstablisherAgent(int serialPortIndex, int baud) {

        this.baud = baud;

        this.port = SerialPort.getCommPorts()[serialPortIndex];
        port.openPort();
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        port.setBaudRate(baud);


        inStream  = new BufferedReader(new InputStreamReader(port.getInputStream()));
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
            port.setBaudRate(baud);


            inStream  = new BufferedReader(new InputStreamReader(port.getInputStream()));
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

    public BufferedReader getInStream() {
        return inStream;
    }
    
    public BufferedWriter getOutStream() {
        return outStream;
    }


    private boolean createHandshake() {
        try {
            String m1 = inStream.readLine();
            System.out.println("data:"+m1);
            if(!m1.endsWith("vga_card"))
                return false;
            outStream.write("ACK");
            outStream.flush();

            String data = inStream.readLine();
            System.out.println("data2:"+data);

            String[] dataSt = data.split(" ");
            if(dataSt.length != 2)
                return false;

            baudRate = Integer.parseInt(dataSt[0]);
            version = dataSt[1];

            outStream.write("OK");
            outStream.flush();
            
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
