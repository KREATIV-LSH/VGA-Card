package ch.lsh.vga_card.frames;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ch.lsh.vga_card.App;
import ch.lsh.vga_card.serial.ConnectionEstablisherAgent;

import java.awt.BorderLayout;

public class ConnectionFrame {
    
    private JFrame frame;
    private JPanel panel;
    private JLabel status;

    public ConnectionFrame(int height, int width, final int serialPortIndex) {
        frame = new JFrame("VGA-Card Controller | ");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        panel = new JPanel();

        // Elements start
        panel.add(new JLabel("You may need to reset the card now"));
        
        status = new JLabel("Status: Connecting...");
        panel.add(status);
        
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean status = establishCon(serialPortIndex);
                if(status) {
                    
                }
            }
        });

        th.start();
        
        // Elements end
        
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        
    }

    private boolean establishCon(int serialPortIndex) {
        try {
            App.ceAgent = new ConnectionEstablisherAgent(serialPortIndex);
            Thread.sleep(500);
            boolean success = App.ceAgent.connect();
            if(success) {
                status.setText("Status: connected");
                return true;
            } else {
                status.setText("Status: failed");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
