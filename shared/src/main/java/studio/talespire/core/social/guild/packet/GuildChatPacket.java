package studio.talespire.core.social.guild.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/5/2024
 */
@AllArgsConstructor
@Getter @NoArgsConstructor
public class GuildChatPacket extends GuildPacket {
    private UUID senderId;
    private String message;
    private transient Component formattedMessage;

    public GuildChatPacket(UUID guildId, UUID senderId, String message) {
        super (guildId);
        this.senderId = senderId;
        this.message = message;
    }
    @Override
    public void receive() {
        this.formattedMessage = GsonComponentSerializer.gson().deserialize(this.message);
    }
}
