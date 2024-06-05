package studio.talespire.core.effects.effect;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.BaseImageEffect;

public class ColoredImageEffect extends BaseImageEffect {

    public ColoredImageEffect(EffectService effectService) {
        super(effectService);
    }

    protected void display(BufferedImage image, Vector v, Location location, int pixel) {
        Color c = new Color(pixel);
        display(particle, location.add(v), org.bukkit.Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
        location.subtract(v);
    }

}
