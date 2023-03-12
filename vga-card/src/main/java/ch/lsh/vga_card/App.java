package ch.lsh.vga_card;

import ch.lsh.vga_card.frames.ConnectionFrame;
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

    public final static int BAUD = 19200;

    static long startTime;

    public static void main( String[] args ) throws InterruptedException {
        // LoadingFrame lFrame = new LoadingFrame(500);

        // System.setProperty("fazecast.jSerialComm.appid", "vga_card-controller");

        // Thread.sleep(1000);

        // lFrame.close();

        // PortSelectFrame psFrame = new PortSelectFrame(125, 400);

        ceAgent = new ConnectionEstablisherAgent(0, BAUD);
        System.out.println(ceAgent.connect());
        connectionEstablished();

        Thread.sleep(1000);

        startTime = System.currentTimeMillis();
        currentImageMode = new ImageModeHandler(OpCode.MODE_IMAGE_SINGLE, new byte[800][600]);
        sController.addToQueue("IMAGE_INNIT");
        System.out.println("added to queue");
    }

    public static void connectionEstablished() {
        sController = new SerialController();
        sController.enterLoop();

        currentImageMode = new ImageModeHandler(-1, null);
    }

    public static void imageInnitDone() {
        currentImageMode.writeImage();
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("writing took " + elapsedTime + " ms");
    }

}
