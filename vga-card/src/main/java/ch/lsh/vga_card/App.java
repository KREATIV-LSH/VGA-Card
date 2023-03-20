package ch.lsh.vga_card;

import ch.lsh.vga_card.frames.ConnectionFrame;
import ch.lsh.vga_card.frames.ImageSelectFrame;
import ch.lsh.vga_card.frames.LoadingFrame;
import ch.lsh.vga_card.frames.PortSelectFrame;
import ch.lsh.vga_card.serial.ConnectionEstablisherAgent;
import ch.lsh.vga_card.serial.ImageModeHandler;
import ch.lsh.vga_card.serial.OpCode;
import ch.lsh.vga_card.serial.SerialController;

/**
 * Hello world!
 *
 */
public class App 
{

    public static ConnectionFrame cnFrame;
    public static ConnectionEstablisherAgent ceAgent;
    public static SerialController sController;
    public static ImageModeHandler currentImageMode;

    private static ImageSelectFrame imSFrame;

    public static final int BAUD = 3_000_000;

    public static long startTime;

    public static void main( String[] args ) throws InterruptedException {
        LoadingFrame lFrame = new LoadingFrame(500);

        System.setProperty("fazecast.jSerialComm.appid", "vga_card-controller");

        Thread.sleep(1000);

        lFrame.close();

        PortSelectFrame psFrame = new PortSelectFrame(125, 400);

        // ceAgent = new ConnectionEstablisherAgent(0, BAUD);
        // System.out.println(ceAgent.connect());
        // connectionEstablished();

        // Thread.sleep(1000);

        // startTime = System.currentTimeMillis();
        // currentImageMode = new ImageModeHandler(OpCode.MODE_IMAGE_SINGLE, new byte[800][600]);
        // sController.addToQueue("IMAGE_INNIT");
        // System.out.println("added to queue");

        // imSFrame = new ImageSelectFrame(810, 675);
    }

    public static void connectionEstablished() {
        sController = new SerialController();
        sController.enterLoop();

        currentImageMode = new ImageModeHandler(-1, null);

        imSFrame = new ImageSelectFrame(810, 675);
    }

    public static void imageInnitDone() {
        currentImageMode.writeImage();
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("writing took " + elapsedTime + " ms");
    }

}
