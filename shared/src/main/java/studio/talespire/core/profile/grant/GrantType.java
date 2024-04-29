package studio.talespire.core.profile.grant;

import lombok.Getter;
import studio.talespire.core.profile.grant.types.GrantPermission;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.util.StringUtils;

/**
 * @date 4/28/2024
 * @author Moose1301
 */
@Getter
public enum GrantType {
    RANK(GrantRank.class),
    PERMISSION(GrantPermission.class);

    private final Class<? extends Grant> clazz;
    private String displayName = null;

    GrantType(Class<? extends Grant> clazz) {
        this.clazz = clazz;
    }
    public String getDisplayName() {
        if(displayName == null) {
            displayName = StringUtils.capitalize(this.name().toLowerCase());
        }
        return displayName;
    }
}
