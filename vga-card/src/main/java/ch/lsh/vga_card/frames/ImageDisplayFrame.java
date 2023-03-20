package ch.lsh.vga_card.frames;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * @author Luis Hutterli
 * @see https://github.com/KREATIV-LSH/MandelbrotGenerator/blob/master/src/main/java/ch/lsh/gui/mandelbrot/MandelbrotDisplay.java
 *      Part of this code is based on my own implementation from my mandelbrot
 *      renderer
 */

public class ImageDisplayFrame extends JPanel {

    private final BufferedImage image;

    private int[][][] imageData;


    public ImageDisplayFrame() {
        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);

        repaint();
    }

    public void setNewImage(BufferedImage inIm) {
        if (inIm.getWidth() != 800 || inIm.getHeight() != 600) {
            JOptionPane.showMessageDialog(null, "The provided image needs to be 800 by 600 pixels",
                    "Image could not be loaded",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        clearFrame();

        imageData = imageToArray(inIm);

        repaint();
    }

    public int[][][] getImageData() {
        return imageData;
    }

    private void clearFrame() {
        for(int x = 0; x < 800; x++) {
            for(int y = 0; y < 600; y++) {
                image.setRGB(x, y, Color.WHITE.getRGB());
            }   
        }
    }

    /**
     * Converts a given image into it's pixels and color channels, <b>images with
     * alpha channels are not supported.</b>
     * 
     * @param inIm The BufferedImage to be converted
     * @return Image in an array form: [row][col][0=red, 1=green, 2=blue]
     * @author Luis Hutterli
     * @see image-compression-test/src/main/java/ch/lsh/App.java
     */
    private int[][][] imageToArray(BufferedImage inIm) {

        final byte[] pixels = ((DataBufferByte) inIm.getRaster().getDataBuffer()).getData();
        final int width = inIm.getWidth();
        final int height = inIm.getHeight();
        final boolean hasAlphaChannel = inIm.getAlphaRaster() != null;

        float normalizer = (float) Math.pow(2, 16); // TO normalize between 0 and 255

        int[][][] result = new int[height][width][3];
        if (!hasAlphaChannel) {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                result[row][col][2] = (int) ((((int) pixels[pixel] & 0xff) << 16) / normalizer); // blue
                result[row][col][1] = (int) ((((int) pixels[pixel + 1] & 0xff) << 16) / normalizer); // green
                result[row][col][0] = (int) ((((int) pixels[pixel + 2] & 0xff) << 16) / normalizer); // red

                // crush image down to 2bits per color
                result[row][col][0] = result[row][col][0] / 64;
                result[row][col][1] = result[row][col][1] / 64;
                result[row][col][2] = result[row][col][2] / 64;

                int rgb = 65536 * (result[row][col][0] * 64) + 256 * (result[row][col][1] * 64)
                        + (result[row][col][2] * 64);

                image.setRGB(col, row, rgb);

                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            // Show error dialog when trying to load an image with an alpha channel and
            // instead of returning the image return the old image
            JOptionPane.showMessageDialog(null,
                    "The provided image could't be loaded correctly, because it contains an alpha channel.",
                    "Image could not be loaded",
                    JOptionPane.ERROR_MESSAGE);
            return imageData;
        }

        return result;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, null, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
