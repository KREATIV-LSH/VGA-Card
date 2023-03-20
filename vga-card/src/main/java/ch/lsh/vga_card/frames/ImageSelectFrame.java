package ch.lsh.vga_card.frames;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JOptionPane;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.lsh.vga_card.App;
import ch.lsh.vga_card.serial.ImageModeHandler;
import ch.lsh.vga_card.serial.OpCode;

public class ImageSelectFrame {

    private JFrame frame;
    private JPanel panel;

    private ImageDisplayFrame imDisplay;

    public ImageSelectFrame(int width, int height) {
        frame = new JFrame("VGA-Card Controller");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(width, height));

        panel = new JPanel();

        // Elements start

        imDisplay = new ImageDisplayFrame();

        // load default image
        BufferedImage buff;
        try {
            buff = ImageIO.read(getClass().getResource("/no-data.png"));
            imDisplay.setNewImage(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.add(imDisplay);



        JButton importButton = new JButton("Import image");
        // Some cool lambda inline stuff
        importButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("Select an image file...");
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "bmp"));
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();

                // Check for correct file-extension
                String fileExtension = "";
                try {
                    fileExtension = (selectedFile.getAbsolutePath().split("\\.")[1])
                            .toLowerCase();
                } catch (Exception adada) {
                    JOptionPane.showMessageDialog(null,
                            "The provided file doesn't seem to be an image.\nProvided file extension: " + fileExtension,
                            "Image could not be loaded",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (fileExtension.equals("jpg") || fileExtension.equals("png") || fileExtension.equals("bmp")) {
                    try {
                        imDisplay.setNewImage(ImageIO.read(selectedFile));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "The provided file doesn't seem to be an image.\nProvided file extension: " + fileExtension,
                            "Image could not be loaded",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        });
        panel.add(importButton);



        JButton sendButton = new JButton("Send data...");
        // Some cool lambda inline stuff
        sendButton.addActionListener(e -> { 
            App.startTime = System.currentTimeMillis();

            App.currentImageMode = new ImageModeHandler(OpCode.MODE_IMAGE_SINGLE, generateImageData());
            App.sController.addToQueue("IMAGE_INNIT");
        });

        panel.add(sendButton);

        // Elements end

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private byte[][] generateImageData() {
        byte[][] result = new byte[600][800];

        int[][][] image = imDisplay.getImageData();

        
        for(int row = 0; row < 600; row++) {
            for(int pixel = 0; pixel < 800; pixel++) {
                int red = image[row][pixel][0];
                int green = image[row][pixel][1];
                int blue = image[row][pixel][2];

                byte color = (byte) ((blue << 4) + (green << 2) + red);
                result[row][pixel] = color;
            }
        }

        return result;
    }

}
