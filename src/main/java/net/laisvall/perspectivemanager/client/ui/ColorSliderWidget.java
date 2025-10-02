package net.laisvall.perspectivemanager.client.ui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ColorSliderWidget extends SliderWidget {
    private final int min;
    private final int max;
    private final Consumer<Integer> onValueChanged;

    public ColorSliderWidget(int x, int y, int width, int height,
                             int min, int max, int initialValue,
                             Consumer<Integer> onValueChanged) {
        super(x, y, width, height, Text.of(""), normalize(initialValue, min, max));
        this.min = min;
        this.max = max;
        this.onValueChanged = onValueChanged;
        this.updateMessage();
    }

    public void updateValue(int value) {
        this.value = normalize(value, min, max);
        this.updateMessage();
        this.applyValue();
    }

    private static double normalize(int value, int min, int max) {
        if (max <= min) return 0.0;
        double clamped = MathHelper.clamp(value, min, max);
        return (clamped - min) / (double)(max - min);
    }

    private int getActualValue() {
        if (max <= min) return min;
        return min + MathHelper.floor(this.value * (max - min) + 0.5D);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of("Hue: " + getActualValue()));
    }

    @Override
    protected void applyValue() {
        onValueChanged.accept(getActualValue());
    }
}
