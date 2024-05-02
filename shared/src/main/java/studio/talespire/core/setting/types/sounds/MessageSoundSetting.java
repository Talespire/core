package studio.talespire.core.setting.types.sounds;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import studio.talespire.core.setting.SettingOption;
import studio.talespire.core.setting.types.BooleanSetting;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class MessageSoundSetting extends BooleanSetting {
    @Override
    public String getId() {
        return "message-sound";
    }

    @Override
    public Component getName() {
        return Component.text("Message Sounds", NamedTextColor.WHITE);
    }

    @Override
    public Component getDescription() {
        return Component.text("Should a Sound play when you receive a private message.");
    }

    @Override
    public SettingOption<Boolean> getDefaultValue() {
        return TRUE_VALUE;
    }

}
