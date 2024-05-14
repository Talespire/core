package studio.talespire.core.social.guild.packet;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class GuildJoinRequestPacket extends GuildPacket {
    private UUID playerId;
    private UUID guildId;

    public GuildJoinRequestPacket(UUID playerId, UUID guildId) {
        this.playerId = playerId;
        this.guildId = guildId;
    }

    @Override
    public void receive() {
    }
}
