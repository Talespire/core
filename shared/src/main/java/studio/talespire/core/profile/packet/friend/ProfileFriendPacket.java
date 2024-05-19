package studio.talespire.core.profile.packet.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.data.redis.packet.RPacket;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/19/2024
 */
@AllArgsConstructor @NoArgsConstructor  @Getter
public abstract class ProfileFriendPacket implements RPacket {
    protected UUID senderId;
    protected UUID receiverId;
}
