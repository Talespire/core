package studio.talespire.core.profile.grant.types;

import lombok.Getter;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.GrantType;
import studio.talespire.core.rank.Rank;

import java.util.UUID;

/**
 * @date 4/28/2024
 * @author Moose1301
 */
@Getter
public class GrantRank extends Grant {
    private final Rank rank;
    public GrantRank(UUID id, Rank rank, String reason, long duration, UUID grantedBy, long grantedAt) {
        super(id, GrantType.RANK, reason, duration, grantedBy, grantedAt);
        this.rank = rank;
    }
}
