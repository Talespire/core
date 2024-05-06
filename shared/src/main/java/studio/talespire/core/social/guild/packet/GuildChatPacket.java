package studio.talespire.core.social.guild.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.social.guild.GuildService;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/5/2024
 */
@AllArgsConstructor
@Getter @NoArgsConstructor
public class GuildChatPacket implements RPacket {
    private UUID guildId;
    private UUID senderId;
    private String message;

    @Override
    public void receive() {
    }
}
