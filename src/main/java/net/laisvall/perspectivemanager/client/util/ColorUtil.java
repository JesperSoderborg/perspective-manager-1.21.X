package net.laisvall.perspectivemanager.client.util;

import java.awt.Color;
import java.util.Random;

public class ColorUtil {
    public static int getRandomHue() {
        return new Random().nextInt(361);
    }

    public static Color getColorFromHue(int hue) {
        return Color.getHSBColor(hue / 360f, 1f, 1f);
    }

    public static int getHexFromColor(Color color) {
        return color.getRGB() & 0xFFFFFF;
    }

    public static String getHexStringFromColor(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static int getHueFromColor(Color color) {
        return (int)Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0]*360;
    }

    public static Color getColorFromHexString(String text) {
        if (text == null || text.isBlank()) return Color.WHITE;

        String hex = text.startsWith("#") ? text.substring(1) : text;

        if (hex.isEmpty() || hex.length() != 3 && hex.length() != 6) return null;

        if (hex.length() == 3) {
            hex = "" + hex.charAt(0) + hex.charAt(0)
                    + hex.charAt(1) + hex.charAt(1)
                    + hex.charAt(2) + hex.charAt(2);
            }

        try {
            return Color.decode("#" + hex);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
