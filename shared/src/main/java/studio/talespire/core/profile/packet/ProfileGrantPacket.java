package studio.talespire.core.profile.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.GrantType;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
@AllArgsConstructor @Getter @NoArgsConstructor
public class ProfileGrantPacket implements RPacket {
    private UUID playerId;
    private Grant grant;
    @Override
    public void receive() {
        Profile profile = getProfile();
        if(profile == null) {
            return;
        }
        profile.getGrants().add(grant);
        if(grant.getType() == GrantType.RANK) {
            profile.load();
        }
    }

    public Profile getProfile() {
        return Universe.get(ProfileService.class).getProfile(playerId);
    }
}
