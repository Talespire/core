package studio.talespire.core.profile.packet.friend;

import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/19/2024
 */
@NoArgsConstructor
public class ProfileFriendRemovePacket extends ProfileFriendPacket{
    public ProfileFriendRemovePacket(UUID senderId, UUID recipientId) {
        super(senderId, recipientId);
    }

    @Override
    public void receive() {

        if(this.getRecipient() == null) {
            return;
        }
        this.getRecipient().removeFriend(this.senderId);
    }
}
