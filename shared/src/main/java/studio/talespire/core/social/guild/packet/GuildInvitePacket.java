package studio.talespire.core.social.guild.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.data.redis.packet.RPacket;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/6/2024
 */
@NoArgsConstructor @Getter
public class GuildInvitePacket extends GuildPacket {
    private UUID senderInvite;
    private UUID targetInvite;

    public GuildInvitePacket(UUID guildId, UUID senderInvite, UUID targetInvite) {
        super(guildId);
        this.senderInvite = senderInvite;
        this.targetInvite = targetInvite;
    }


    @Override
    public void receive() {
    }
}
