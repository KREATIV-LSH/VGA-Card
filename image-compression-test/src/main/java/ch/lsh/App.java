package ch.lsh;

import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        BufferedImage inputImage = ImageIO.read(new File(args[0]));
        
        if(inputImage.getWidth() != 800 && inputImage.getHeight() != 600) {
            throw new Exception("Invalid image size, image must be 800 by 600 pixels and not contain an alpha channel.");
        }

        int pixels[][][] = imageToArray(inputImage);
        System.out.println(Arrays.toString(getPixel(399, 299, pixels)));
    }

    private static int[] getPixel(int x, int y, int[][][] pixels) {
        return pixels[y][x];
    }

    /**
     * Converts a given image into it's pixels and color channels, <b>images with alpha channels are not supported.</b>
     * @param image The BufferedImage to be converted
     * @return  Image in an array form: [row][col][0=red, 1=green, 2=blue]
     * @author Luis Hutterli
     */
    private static int[][][] imageToArray(BufferedImage image) throws Exception {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        float normalizer = (float)Math.pow(2, 16); // TO normalize between 0 and 255

        int[][][] result = new int[height][width][3];
        if (!hasAlphaChannel) {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                result[row][col][2] = (int)((((int) pixels[pixel]     & 0xff) << 16) / normalizer); // blue
                result[row][col][1] = (int)((((int) pixels[pixel + 1] & 0xff) << 16) / normalizer); // green
                result[row][col][0] = (int)((((int) pixels[pixel + 2] & 0xff) << 16) / normalizer); // red
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            throw new Exception("Can't parse image with alpha channel");
        }

        return result;
    }
}
