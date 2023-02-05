package ch.lsh.color;

import java.util.HashMap;
import java.util.Map;

public class ColorAnalyser {

    public static int[] determineMostCommonColor(int[][][] image) {
        // Result array, index 0-2 are for the determent color and index 3 for the
        // percentage of that color
        int[] result = new int[4];

        int height = image.length;
        int width = image[0].length;

        System.out.println("Image loaded for color analysis with width " + width + "p and height " + height + "p");


        HashMap<Color, Integer> colors = new HashMap<>();

        for (int row = 0; row < height; row++) {
            HashMap<Color, Integer> rowColors = new HashMap<>();
            for (int pixel = 0; pixel < width; pixel++) {
                Color colorAtPixel = new Color(image[row][pixel][0], image[row][pixel][1], image[row][pixel][2]);

                if(rowColors.containsKey(colorAtPixel)) {
                    rowColors.put(colorAtPixel, rowColors.get(colorAtPixel) + 1);
                } else {
                    rowColors.put(colorAtPixel, 1);
                }

            }
            int[] rowResults = mostCommonColorInHashMap(rowColors);
            Color rowColor = new Color(rowResults);
            if(colors.containsKey(rowColor)) {
                colors.put(rowColor, colors.get(rowColor) + rowResults[3]);
            } else {
                colors.put(rowColor, rowResults[3]);
            }
        }

        result = mostCommonColorInHashMap(colors);
        result[3] = (int)((result[3]/1f/(width*height))*100);


        return result;
    }

    private static int[] mostCommonColorInHashMap(HashMap<Color, Integer> colors) {
        int[] result = new int[4];

        int[] mostCommonColor = new int[3];
        int mostCommonColorCount = 0;

        for (Map.Entry<Color, Integer> set : colors.entrySet()) {
            if (set.getValue() > mostCommonColorCount) {
                mostCommonColorCount = set.getValue();
                mostCommonColor = set.getKey().split();
            }
        }

        result[0] = mostCommonColor[0];
        result[1] = mostCommonColor[1];
        result[2] = mostCommonColor[2];
        result[3] = mostCommonColorCount;

        return result;
    }

}
