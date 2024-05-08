package studio.talespire.core.social.guild.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import studio.talespire.core.social.guild.model.Guild;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/6/2024
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GuildMuteChatPacket extends GuildPacket {
    private UUID senderId;
    private boolean state;
    private @Nullable UUID target;

    public GuildMuteChatPacket(UUID guildId, UUID senderId, boolean state) {
        super(guildId);
        this.senderId = senderId;
        this.state = state;
    }

    public GuildMuteChatPacket(UUID guildId, UUID senderId, boolean state, @Nullable UUID target) {
        super(guildId);
        this.senderId = senderId;
        this.state = state;
        this.target = target;
    }

    @Override
    public void receive() {
    }

}
