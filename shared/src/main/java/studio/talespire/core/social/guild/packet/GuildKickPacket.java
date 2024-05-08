package studio.talespire.core.social.guild.packet;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/7/2024
 */
@NoArgsConstructor @Getter
public class GuildKickPacket extends GuildPacket{
    private UUID playerId;
    private UUID removedId;

    /**
     * @param guildId ID of the guild
     * @param playerId ID of the player that kicked the player
     * @param removedId ID of the kicked player
     */
    public GuildKickPacket(UUID guildId, UUID playerId, UUID removedId) {
        super(guildId);
        this.playerId = playerId;
        this.removedId = removedId;
    }
    @Override
    public void receive() {

    }
}
