package studio.talespire.core.social.guild.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/4/2024
 */
@Getter @Setter @EqualsAndHashCode @AllArgsConstructor
public class GuildMember {
    private final UUID playerId;
    private final long joinedAt;
    private GuildRole role;

    public GuildMember(UUID playerId) {
        this.playerId = playerId;
        this.joinedAt = System.currentTimeMillis();
        this.role = GuildRole.MEMBER;

    }
}
