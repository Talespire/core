package studio.talespire.core.setting;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;

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
    public abstract Component getName();
    public abstract Component getDescription();

    /**
     * @return The default value that set for the player
     */
    public abstract SettingOption<T> getDefaultValue();

    /**
     * @return All the options for thie setting
     */
    public abstract List<SettingOption<T>> getOptions();

    /**
     * @param value The option
     * @return The display name of the option
     */
    public abstract Component getDisplayName(SettingOption<T> value);

    /**
     * @param currentValue The current option that the player has selected
     * @return The next option for the player
     */
    public abstract SettingOption<T> getNext(SettingOption<T> currentValue);
    public SettingOption<T> getRawNext(SettingOption<?> currentValue) {
        return getNext((SettingOption<T>) currentValue);
    }


    public abstract SettingOption<T> deserialize(JsonElement value);
}
