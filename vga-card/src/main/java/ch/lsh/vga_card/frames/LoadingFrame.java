package ch.lsh.vga_card.frames;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Image;
import java.net.URL;

public class LoadingFrame {

    private JFrame frame;

    public LoadingFrame(int width) {
        // Setup the frame
        frame = new JFrame("VGA-Card Controller | Loading");
        frame.setSize(width,width/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);

        // Display Splash screen image
        ImageIcon splash = new ImageIcon(getClass().getResource("/splash-screen.png"));
        frame.add(new JLabel(splash));
        // Center the frame and show it
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}


