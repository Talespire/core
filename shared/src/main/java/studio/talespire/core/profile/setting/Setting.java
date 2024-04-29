package studio.talespire.core.profile.setting;

import studio.talespire.core.profile.Profile;

import javax.swing.*;
import java.util.List;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public abstract class Setting<T> {
    /**
     * @return The id which this is stored as
     */
    public abstract String getId();

    /**
     * @return The name that is shown to the player
     */
    public abstract String getName();

    /**
     * @return The default value that set for the player
     */
    public abstract T getDefaultValue();

    /**
     * @return All the options for thie setting
     */
    public abstract List<SettingOption<T>> getOptions();

    /**
     * @param value The option
     * @return The display name of the option
     */
    public abstract String getDisplayName(T value);

    /**
     * @param profile The player which is switching the setting
     * @param currentValue The current option that the player has selected
     * @return The next option for the player
     */
    public abstract T getNext(Profile profile, T currentValue);

    /**
     * @param profile The player
     * @param value The option
     * @return If the player can switch to that option
     */
    public abstract boolean hasPermission(Profile profile, T value);
}
