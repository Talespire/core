package studio.talespire.core.effects.math;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public class EchoTransform implements Transform {

    @Override
    public void load(ConfigurationSection parameters) {
    }

    @Override
    public double get(double input) {
        return input;
    }

}
