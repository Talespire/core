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
public class GuildTagPacket extends GuildPacket{
    private UUID playerId;
    private String tag;
    public GuildTagPacket(UUID guildId, UUID playerId, String tag) {
        super(guildId);
        this.playerId = playerId;
        this.tag = tag;
    }
    @Override
    public void receive() {

    }
}
