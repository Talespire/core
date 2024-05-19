package studio.talespire.core.profile.packet.friend;

import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/19/2024
 */
@NoArgsConstructor
public class ProfileFriendRequestPacket extends ProfileFriendPacket{
    public ProfileFriendRequestPacket(UUID senderId, UUID recipientId) {
        super(senderId, recipientId);
    }

    @Override
    public void receive() {

    }
}
