package studio.talespire.core.profile.packet.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/19/2024
 */
@AllArgsConstructor @NoArgsConstructor  @Getter
public abstract class ProfileFriendPacket implements RPacket {
    protected UUID senderId;
    protected UUID recipientId;


    public Profile getSender() {
        return Universe.get(ProfileService.class).getProfile(this.senderId);
    }
    public Profile getRecipient() {
        return Universe.get(ProfileService.class).getProfile(this.recipientId);
    }
}
