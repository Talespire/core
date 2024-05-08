package studio.talespire.core.social.guild.packet;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/7/2024
 */
@NoArgsConstructor @Getter
public class GuildJoinPacket extends GuildPacket{
    private UUID playerId;
    public GuildJoinPacket(UUID guildId, UUID playerId) {
        super(guildId);
        this.playerId = playerId;
    }
    @Override
    public void receive() {

    }
}
