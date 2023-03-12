package ch.lsh.vga_card.serial;

public class OpCode {

    public static final int STATUS_OK = 0x0E;
    public static final int STATUS_BAD = 0x0F;
    public static final int MODE_IMAGE_SINGLE = 0x11;
    public static final int PACKET_KEEPALIVE = 0x1c;
    public static final int PACKET_IMAGE_INNIT = 0x22;
    

    // Constructor to satisfy sonarlint S111
    private OpCode() {
        throw new IllegalStateException("Utility class");
    }
    
}
