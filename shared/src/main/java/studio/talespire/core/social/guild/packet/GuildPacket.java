package studio.talespire.core.social.guild.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.model.Guild;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/6/2024
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public abstract class GuildPacket implements RPacket {
    protected UUID guildId;
    public Guild getGuild() {
        return Universe.get(GuildService.class).getGuild(guildId);
    }
}
