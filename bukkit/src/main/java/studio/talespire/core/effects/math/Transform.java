package studio.talespire.core.effects.math;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public interface Transform {

    public void load(ConfigurationSection parameters);

    public double get(double input);

}
