package studio.talespire.core.profile.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketListener;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.grant.GrantType;
import studio.talespire.core.profile.packet.ProfileGrantPacket;
import studio.talespire.core.profile.utils.BukkitProfileUtils;

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
        Player player = Bukkit.getPlayer(packet.getPlayerId());
        if(player == null) {
            return;
        }
        BukkitProfileUtils.updatePlayerDisplay(player, profile);
    }
}
