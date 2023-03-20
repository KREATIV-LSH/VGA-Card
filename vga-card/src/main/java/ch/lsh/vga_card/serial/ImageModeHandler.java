package ch.lsh.vga_card.serial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import ch.lsh.vga_card.App;

public class ImageModeHandler {

    private static int imageMode;
    private static SerialController sController;
    private BufferedReader inStream;
    private BufferedWriter outStream;

    private boolean available = true;

    private byte[][] data;

    public ImageModeHandler(int imageMode, byte[][] data) {
        this.imageMode = imageMode;
        sController = App.sController;
        inStream = sController.getInStream();
        outStream = sController.getOutStream();

        this.data = data;
    }

    public void writeImage() {
        if(data == null || imageMode < 1) {
            available = true;
            return;
        } else {
            available = false;
        }
        try {
            System.out.println("Writing image");
            outStream.write(imageMode);
            outStream.flush();

            System.out.println("wrote mode magic byte");

            int height = data.length;
            int width = data[0].length;
            for(int row = 0; row < height; row++) {
                for(int pixel = 0; pixel < width; pixel++) {
                    outStream.write((char)data[row][pixel]);
                }
                if(row % 10 == 0)
                    System.out.println(row);
            }
            outStream.flush();

            available = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
