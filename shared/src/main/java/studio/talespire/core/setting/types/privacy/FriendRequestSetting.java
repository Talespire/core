package studio.talespire.core.setting.types.privacy;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import studio.talespire.core.setting.Setting;
import studio.talespire.core.setting.SettingOption;
import studio.talespire.core.setting.types.PrivacyLevel;

import java.util.List;

/**
 * @author Moose1301
 * @date 4/30/2024
 */
public class FriendRequestSetting extends Setting<PrivacyLevel> {
    @Override
    public String getId() {
        return "friend-requests";
    }

    @Override
    public Component getName() {
        return Component.text("Friend Requests");
    }

    @Override
    public Component getDescription() {
        return Component.text("Who is allowed to send you friend requests");
    }

    @Override
    public SettingOption<PrivacyLevel> getDefaultValue() {
        return PrivacyLevel.EVERYONE.getValue();
    }

    @Override
    public List<SettingOption<PrivacyLevel>> getOptions() {
        return List.of(
                PrivacyLevel.EVERYONE.getValue(),
                PrivacyLevel.GUILD.getValue(),
                PrivacyLevel.STAFF.getValue(),
                PrivacyLevel.NONE.getValue()
        );
    }

    @Override
    public Component getDisplayName(SettingOption<PrivacyLevel> value) {
        return value.name();
    }

    @Override
    public SettingOption<PrivacyLevel> getNext(SettingOption<PrivacyLevel> currentValue) {
        return currentValue.value().getNext().getValue();
    }



    @Override
    public SettingOption<PrivacyLevel> deserialize(JsonElement value) {
        return PrivacyLevel.valueOf(value.getAsString().toUpperCase()).getValue();
    }
}
