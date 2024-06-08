package studio.talespire.core.tablist.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.TabListService;
import studio.talespire.core.tablist.util.GlitchFixEvent;

import java.util.EnumSet;

/**
 * @author Disunion
 * @date 6/7/2024
 * Listener for TeamsPacket events.
 * This class is responsible for handling packet send events related to teams.
 */
@RequiredArgsConstructor
public class TeamsPacketListener extends PacketListenerAbstract {
    // Method that gets triggered when a packet is sent
    @Override
    public void onPacketSend(PacketSendEvent event) {
        // Check if the packet type is PLAYER_INFO, PLAYER_INFO_UPDATE or TEAMS
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO
                && event.getPacketType() != PacketType.Play.Server.PLAYER_INFO_UPDATE
                && event.getPacketType() != PacketType.Play.Server.TEAMS) {
            return;
        }

        // Get the player who sent the packet
        Player player = (Player) event.getPlayer();
        // If the player is null, return
        if (player == null) {
            return;
        }

        // If the packet type is PLAYER_INFO_UPDATE
        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE) {
            // Create a new WrapperPlayServerPlayerInfoUpdate instance
            WrapperPlayServerPlayerInfoUpdate infoUpdate = new WrapperPlayServerPlayerInfoUpdate(event);

            // Get the actions from the info update
            EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> action = infoUpdate.getActions();
            // If the action does not contain ADD_PLAYER, return
            if (!action.contains(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER)) return;

            // Loop through the entries in the info update
            for (WrapperPlayServerPlayerInfoUpdate.PlayerInfo info : infoUpdate.getEntries()) {
                // Get the game profile from the info
                UserProfile userProfile = info.getGameProfile();
                // If the user profile is null, continue to the next iteration
                if (userProfile == null) {
                    continue;
                }

                // Call the preventGlitch method with the player and user profile
                this.preventGlitch(player, userProfile);
            }
        } else if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            // Create a new WrapperPlayServerPlayerInfo instance
            WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(event);
            // Get the action from the info packet
            WrapperPlayServerPlayerInfo.Action action = infoPacket.getAction();
            // If the action is not ADD_PLAYER, return
            if (action != WrapperPlayServerPlayerInfo.Action.ADD_PLAYER) return;

            // Loop through the player data list in the info packet
            for (WrapperPlayServerPlayerInfo.PlayerData data : infoPacket.getPlayerDataList()) {
                // Get the user profile from the data
                UserProfile userProfile = data.getUserProfile();
                // If the user profile is null, continue to the next iteration
                if (userProfile == null) {
                    continue;
                }

                // Call the preventGlitch method with the player and user profile
                this.preventGlitch(player, userProfile);
            }
        }
    }

    // Method to prevent glitches
    private void preventGlitch(Player player, UserProfile userProfile) {
        // If the player is null, return
        if (player == null) return;

        // Get the online player with the UUID from the user profile
        Player online = Bukkit.getPlayer(userProfile.getUUID());
        // If the online player is null, return
        if (online == null) return;

        // Get the scoreboard from the player
        Scoreboard scoreboard = player.getScoreboard();
        // Get the team "ztab" from the scoreboard
        Team team = scoreboard.getTeam("ztab");

        // If the team is null, register a new team "ztab"
        if (team == null) {
            team = scoreboard.registerNewTeam("ztab");
        }

        // If the team does not have the online player's name as an entry, add it
        if (!team.hasEntry(online.getName())) {
            team.addEntry(online.getName());
        }

        // Create a new GlitchFixEvent instance
        GlitchFixEvent glitchFixEvent = new GlitchFixEvent(player);
        // If the plugin from the TabListService instance in the Universe is enabled
        if (Universe.get(TabListService.class).getPlugin().isEnabled()) {
            // Run a task on the Bukkit scheduler that calls the glitch fix event
            Bukkit.getScheduler().runTask(Universe.get(TabListService.class).getPlugin(), () -> Bukkit.getPluginManager().callEvent(glitchFixEvent));
        }
    }
}