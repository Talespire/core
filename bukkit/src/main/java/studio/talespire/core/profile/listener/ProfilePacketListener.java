package studio.talespire.core.profile.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketListener;
import studio.talespire.core.profile.BukkitProfile;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.grant.GrantType;
import studio.talespire.core.profile.packet.ProfileGrantPacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendAcceptPacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendPacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendRemovePacket;
import studio.talespire.core.profile.packet.friend.ProfileFriendRequestCreatePacket;
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
    @RPacketHandler
    public void onProfileFriendRequestCreate(ProfileFriendRequestCreatePacket packet) {
        if(packet.getRecipient() == null) {
            return;
        }
        BukkitProfile profile = (BukkitProfile) packet.getRecipient();
        if(profile.getPlayer() == null) {
            return;
        }
        //TODO: Add friend request message here
        profile.getPlayer().sendMessage("Add friend request message here");
    }
    @RPacketHandler
    public void onProfileFriendAccept(ProfileFriendAcceptPacket packet) {
        if(packet.getRecipient() == null) {
            return;
        }
        BukkitProfile profile = (BukkitProfile) packet.getRecipient();
        if(profile.getPlayer() == null) {
            return;
        }
        //TODO: Add friend accept message here
        profile.getPlayer().sendMessage("Add friend accept message here");
    }
    @RPacketHandler
    public void onProfileFriendRemove(ProfileFriendRemovePacket packet) {
        if(packet.getRecipient() == null) {
            return;
        }
        BukkitProfile profile = (BukkitProfile) packet.getRecipient();
        if(profile.getPlayer() == null) {
            return;
        }
        //TODO: Add friend remove message here
        profile.getPlayer().sendMessage("Add friend remove message here");
    }
}
