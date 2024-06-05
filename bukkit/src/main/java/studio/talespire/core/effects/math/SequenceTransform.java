package studio.talespire.core.effects.math;

import org.bukkit.configuration.ConfigurationSection;
import studio.talespire.core.effects.util.ConfigUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public class SequenceTransform implements Transform {

    private List<Sequence> steps;

    private static class Sequence {

        private final Transform transform;
        private final double start;

        public Sequence(ConfigurationSection configuration) {
            transform = Transforms.loadTransform(configuration, "transform");
            start = configuration.getDouble("start", 0);
        }

        public double getStart() {
            return start;
        }

        public double get(double t) {
            return transform.get(t);
        }

    }

    @Override
    public void load(ConfigurationSection parameters) {
        steps = new ArrayList<>();
        Collection<ConfigurationSection> stepConfigurations = ConfigUtils.getNodeList(parameters, "steps");
        for (ConfigurationSection stepConfig : stepConfigurations) {
            steps.add(new Sequence(stepConfig));
        }
        Collections.reverse(steps);
    }

    @Override
    public double get(double input) {
        double value = 0;
        for (Sequence step : steps) {
            if (step.getStart() <= input) return step.get(input);
        }
        return value;
    }

}
