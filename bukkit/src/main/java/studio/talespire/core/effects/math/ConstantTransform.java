package studio.talespire.core.effects.math;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public class ConstantTransform implements Transform {

    private double value;

    public ConstantTransform() {

    }

    public ConstantTransform(double value) {
        this.value = value;
    }

    @Override
    public void load(ConfigurationSection parameters) {
        value = parameters.getDouble("value");
    }

    @Override
    public double get(double input) {
        return value;
    }
}
