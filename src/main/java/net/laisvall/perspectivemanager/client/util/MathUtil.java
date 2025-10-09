package net.laisvall.perspectivemanager.client.util;

public class MathUtil {
    public static double roundDouble(double val, int decimals) {
        float scale = (float) Math.pow(10, decimals);
        return Math.round(val * scale) / scale;
    }

    public static float roundFloat(float val, int decimals) {
        float scale = (float) Math.pow(10, decimals);
        return Math.round(val * scale) / scale;
    }
}
