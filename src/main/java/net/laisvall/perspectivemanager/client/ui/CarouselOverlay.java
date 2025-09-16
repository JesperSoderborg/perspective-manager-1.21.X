package net.laisvall.perspectivemanager.client.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CarouselOverlay extends Screen {
    public static boolean open = false;

    public CarouselOverlay(Text title) {
        super(title);
    }
}
