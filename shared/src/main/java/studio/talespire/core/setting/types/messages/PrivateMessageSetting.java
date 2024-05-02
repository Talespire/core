package studio.talespire.core.setting.types.messages;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import studio.talespire.core.setting.Setting;
import studio.talespire.core.setting.SettingOption;

import java.util.List;

/**
 * @author Moose1301
 * @date 4/30/2024
 */
public class PrivateMessageSetting extends Setting<PrivateMessageType> {
    @Override
    public String getId() {
        return "private-message";
    }

    @Override
    public Component getName() {
        return Component.text("Private Messages");
    }

    @Override
    public Component getDescription() {
        return Component.text("How would you like incoming messages to be filtered");
    }

    @Override
    public SettingOption<PrivateMessageType> getDefaultValue() {
        return PrivateMessageType.EVERYONE.getValue();
    }

    @Override
    public List<SettingOption<PrivateMessageType>> getOptions() {
        return List.of(
                PrivateMessageType.EVERYONE.getValue(),
                PrivateMessageType.FRIENDS.getValue(),
                PrivateMessageType.STAFF.getValue(),
                PrivateMessageType.NONE.getValue()
        );
    }

    @Override
    public Component getDisplayName(SettingOption<PrivateMessageType> value) {
        return value.name();
    }

    @Override
    public SettingOption<PrivateMessageType> getNext(SettingOption<PrivateMessageType> currentValue) {
        return currentValue.value().getNext().getValue();
    }



    @Override
    public SettingOption<PrivateMessageType> deserialize(JsonElement value) {
        return PrivateMessageType.valueOf(value.getAsString().toUpperCase()).getValue();
    }
}
