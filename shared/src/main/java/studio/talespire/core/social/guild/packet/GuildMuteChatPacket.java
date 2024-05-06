package studio.talespire.core.social.guild.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.social.guild.model.Guild;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/6/2024
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class GuildMuteChatPacket extends GuildPacket {
    private UUID senderId;
    private boolean state;

    public GuildMuteChatPacket(UUID guildId, UUID senderId, boolean state) {
        super(guildId);
        this.senderId = senderId;
        this.state = state;
    }
    @Override
    public void receive() {
        Guild guild = getGuild();
        if(guild == null) return;
        guild.setMutechat(this.state);
    }

}
