package studio.talespire.core.effects.math;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public class SumTransform implements Transform {

    private Collection<Transform> inputs;

    @Override
    public void load(ConfigurationSection parameters) {
        inputs = Transforms.loadTransformList(parameters, "inputs");
    }

    @Override
    public double get(double input) {
        double value = 0;
        for (Transform transform : inputs) {
            value += transform.get(input);
        }
        return value;
    }

}