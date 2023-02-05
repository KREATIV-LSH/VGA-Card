package ch.lsh.color;

import java.util.Objects;

public class Color {

    private Integer red;
    private Integer green;
    private Integer blue;

    public Color(Integer red, Integer green, Integer blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int[] channels) {
        this.red = channels[0];
        this.green = channels[1];
        this.blue = channels[2];
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int[] split() {
        int[] output = new int[3];
        output[0] = red;
        output[1] = green;
        output[2] = blue;

        return output;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if(getClass() != obj.getClass())
            return false;

        Color color = (Color) obj;

        return red.equals(color.getRed()) && green.equals(color.getGreen()) && blue.equals(color.getBlue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }
}
