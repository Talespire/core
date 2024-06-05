package studio.talespire.core.effects.effect;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.BaseImageEffect;

public class ImageEffect extends BaseImageEffect {

    /**
     * Invert the image
     */
    public boolean invert = false;

    public ImageEffect(EffectService effectService) {
        super(effectService);
    }

    protected void display(BufferedImage image, Vector v, Location location, int pixel) {
        if (!invert && Color.black.getRGB() != pixel) return;
        else if (invert && Color.black.getRGB() == pixel) return;

        display(particle, location.add(v));
        location.subtract(v);
    }

}
