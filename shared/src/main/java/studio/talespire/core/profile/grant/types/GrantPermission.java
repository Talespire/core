package studio.talespire.core.profile.grant.types;

import lombok.Getter;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.GrantType;

import java.util.UUID;

/**
 * @date 4/28/2024
 * @author Moose1301
 */
@Getter
public class GrantPermission extends Grant {
    private final String permission;
    public GrantPermission(UUID id, String permission, String reason, long duration, UUID executedBy, long executedAt) {
        super(id, GrantType.PERMISSION, reason, duration, executedBy, executedAt);
        this.permission = permission;
    }
}
