package studio.talespire.core.setting.types;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import studio.talespire.core.setting.Setting;
import studio.talespire.core.setting.SettingOption;

import java.util.List;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public abstract class BooleanSetting extends Setting<Boolean> {
    public static final SettingOption<Boolean> TRUE_VALUE = SettingOption.of("Enabled", true);
    public static final SettingOption<Boolean> FALSE_VALUE = SettingOption.of("Disabled", false);
    @Override
    public List<SettingOption<Boolean>> getOptions() {
        return List.of(
                new SettingOption<>(Component.text("Enabled"), true),
                new SettingOption<>(Component.text("Disabled"), false)
        );
    }

    @Override
    public Component getDisplayName(SettingOption<Boolean> value) {
        return value.name();
    }

    @Override
    public SettingOption<Boolean> getNext(SettingOption<Boolean> currentValue) {
        return currentValue.value() ? FALSE_VALUE : TRUE_VALUE;
    }



    @Override
    public SettingOption<Boolean> deserialize(JsonElement value) {
        return value.getAsBoolean() ? TRUE_VALUE : FALSE_VALUE;
    }
}
