package studio.talespire.core.setting.types;


import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.setting.SettingOption;
import studio.talespire.core.util.StringUtils;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
@Getter
public enum PrivacyLevel {
    EVERYONE,
    FRIENDS,
    STAFF,
    NONE;


    private final SettingOption<PrivacyLevel> value;

    PrivacyLevel() {
        Component displayName = Component.text(StringUtils.capitalize(this.name().toLowerCase()), NamedTextColor.WHITE);
        this.value = new SettingOption<>(displayName, this);

    }

    public PrivacyLevel getNext() {
        int index = ordinal();
        int next = index + 1;
        if (next > NONE.ordinal()) {
            next = 0;
        }
        return values()[next];
    }

    public boolean doesMatch(Profile fromProfile, Profile targetProfile) {
        if(this == EVERYONE) {
            return true;
        } else if(this == NONE) {
            return false;
        }
        if(this.ordinal() <= STAFF.ordinal()) {
            return fromProfile.getRank().isStaff();
        }

        //TODO Friend System
        return true;

    }
}
