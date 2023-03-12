package ch.lsh.vga_card.serial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

import ch.lsh.vga_card.App;

public class SerialController {

    private Queue<String> packets;
    private Thread keepAlive;
    private Thread loop;

    private boolean stopped;

    private BufferedReader inStream;
    private BufferedWriter outStream;

    public SerialController() {
        inStream = App.ceAgent.getInStream();
        outStream = App.ceAgent.getOutStream();

        stopped = false;

        packets = new LinkedList<>();

        keepAlive = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopped) {
                    try {
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    if (packets.isEmpty()) {
                        packets.add("KEEPALIVE");
                    }
                }
            }
        });
        keepAlive.start();
    }

    public void enterLoop() {
        loop = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopped) {
                    while (!packets.isEmpty()) {
                        String packet = packets.remove();
                        System.out.println("Handeling packet: " + packet);
                        if (!handlePacket(packet)) {
                            JOptionPane.showMessageDialog(null, "Unable to handle packet: " + packet, "Packet error",
                                    JOptionPane.ERROR_MESSAGE);
                            System.exit(-1);
                            break;
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        loop.start();

    }

    public void addToQueue(String packet) {
        packets.add(packet);
    }

    public boolean handlePacket(String packet) {
        try {
            if (packet.equals("KEEPALIVE")) {
                outStream.write(OpCode.PACKET_KEEPALIVE);
                outStream.flush();
                int a1 = inStream.read();
                System.out.println(a1);
                if (a1 != OpCode.STATUS_OK)
                    return false;
            } else if(packet.equals("IMAGE_INNIT")) {
                System.out.println("handeling image innit");

                outStream.write(OpCode.PACKET_IMAGE_INNIT);
                outStream.flush();

                System.out.println("Wrote magic byte");

                int a1 = inStream.read();
                System.out.println(a1);
                if (a1 != OpCode.STATUS_OK)
                    return false;
                System.out.println("read ok");
                App.imageInnitDone();
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // private String readLine() {
    //     try {
    //         char c;
    //         String s = "";
    //         do {
    //             c = (char) inStream.read();
    //             if (c == '\n')
    //                 break;
    //             s += c + "";
    //         } while (c != -1);
    //         return s;
    //     } catch (IOException e) {
    //     }
    //     return "";
    // }

    public BufferedWriter getOutStream() {
        return outStream;
    }

    public BufferedReader getInStream() {
        return inStream;
    }

}
