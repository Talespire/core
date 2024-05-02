package studio.talespire.core.setting.types.messages;


import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import studio.talespire.core.setting.SettingOption;
import studio.talespire.core.util.StringUtils;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
@Getter
public enum PrivateMessageType {
    EVERYONE,
    FRIENDS,
    STAFF,
    NONE;


    private final SettingOption<PrivateMessageType> value;

    PrivateMessageType() {
        Component displayName = Component.text(StringUtils.capitalize(this.name().toLowerCase()), NamedTextColor.WHITE);
        this.value = new SettingOption<>(displayName, this);

    }

    public PrivateMessageType getNext() {
        int index = ordinal();
        int next = index + 1;
        if (next > NONE.ordinal()) {
            next = 0;
        }
        return values()[next];
    }
}
