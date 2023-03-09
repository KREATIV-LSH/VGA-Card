package ch.lsh.vga_card;

import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;

import ch.lsh.vga_card.frames.ConnectionFrame;
import ch.lsh.vga_card.frames.LoadingFrame;
import ch.lsh.vga_card.frames.PortSelectFrame;
import ch.lsh.vga_card.serial.ConnectionEstablisherAgent;

/**
 * Hello world!
 *
 */
public class App 
{

    public static ConnectionFrame cnFrame;
    public static ConnectionEstablisherAgent ceAgent;

    public static void main( String[] args ) throws InterruptedException {
        LoadingFrame lFrame = new LoadingFrame(500);

        System.setProperty("fazecast.jSerialComm.appid", "vga_card-controller");

        Thread.sleep(1000);

        lFrame.close();

        PortSelectFrame psFrame = new PortSelectFrame(125, 400);

        // ceAgent = new ConnectionEstablisherAgent(0);
        // System.out.println(ceAgent.connect());
    }

}
