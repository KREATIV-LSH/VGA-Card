package ch.lsh.vga_card.frames;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.fazecast.jSerialComm.SerialPort;

import ch.lsh.vga_card.App;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PortSelectFrame {

    private JFrame frame;

    public PortSelectFrame(int height, int width) {
        frame = new JFrame("VGA-Card Controller | Port's");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();

        // Elements start

        panel.add(new JLabel("Select a serial port:"), BorderLayout.NORTH);

        String[] choices = new String[SerialPort.getCommPorts().length];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = (i + 1) + ", " + SerialPort.getCommPorts()[i].getDescriptivePortName() + " "
                    + SerialPort.getCommPorts()[i].getSystemPortName();
        }

        final JComboBox<String> ports = new JComboBox<String>(choices);
        final DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) ports.getModel();
        panel.add(ports, BorderLayout.WEST);

        JButton refreshBt = new JButton("Refresh");
        refreshBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                model.removeAllElements();
                for (int i = 0; i < SerialPort.getCommPorts().length; i++) {
                    model.addElement((i + 1) + ", " + SerialPort.getCommPorts()[i].getDescriptivePortName() + " "
                    + SerialPort.getCommPorts()[i].getSystemPortName());
                }
            }
        });
        panel.add(refreshBt);


        JButton nextBt = new JButton("Next");
        nextBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(ports.getSelectedIndex() != -1) {
                    frame.dispose();
                    App.cnFrame = new ConnectionFrame(100, 300, ports.getSelectedIndex());
                }
            }
        });

        panel.add(nextBt);


        // Elements end

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
