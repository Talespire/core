package studio.talespire.core.profile.listener;

import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketListener;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.grant.GrantType;
import studio.talespire.core.profile.packet.ProfileGrantPacket;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class ProfilePacketListener implements RPacketListener {

    @RPacketHandler
    public void onPlayerGrant(ProfileGrantPacket packet) {
        Profile profile = packet.getProfile();
        if(profile == null || packet.getGrant().getType() != GrantType.RANK) {
            return;
        }

    }
}
