package studio.talespire.core.social.guild.packet;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/8/2024
 */
@NoArgsConstructor
@Getter
public class GuildRolePacket extends GuildPacket{
    private UUID promoterId;
    private UUID promotedId;
    private boolean demoted;

    public GuildRolePacket(UUID guildId, UUID promoterId, UUID promotedId, boolean demoted) {
        super(guildId);
        this.promoterId = promoterId;
        this.promotedId = promotedId;
        this.demoted = demoted;

    }
    @Override
    public void receive() {

    }
}
