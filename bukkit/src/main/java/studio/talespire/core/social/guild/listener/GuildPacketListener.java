package studio.talespire.core.social.guild.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.chat.ChatChannelService;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketListener;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.packet.*;

import java.util.UUID;

/**
 * @author Moose1301
 * @date 5/7/2024
 */

public class GuildPacketListener implements RPacketListener {


    @RPacketHandler
    public void onGuildInvite(GuildInvitePacket packet) {}


    @RPacketHandler
    public void onGuildChat(GuildChatPacket packet) {

        Guild guild = Universe.get(ProfileService.class).getProfile(packet.getSenderId()).getGuild();
        if(guild == null) {
            return;
        }
        Component toSend = Component.text("Guild » ", NamedTextColor.DARK_GREEN)
            .append(BukkitProfileUtils.getRankedNameLoaded(packet.getSenderId())
            .appendSpace()
            .append(Component.text("[", guild.getColor()))
                    .append(Component.text(guild.getMember(packet.getSenderId()).getRole().name(), guild.getColor()))
                    .append(Component.text("] ", guild.getColor()))
            .appendSpace()
                    .append(Component.text(": ", NamedTextColor.WHITE))
            .append(packet.getFormattedMessage().color(NamedTextColor.WHITE)));
        for (UUID uuid : guild.getMembers().keySet()) {
            Player onlnePlayer = Bukkit.getPlayer(uuid);
            if(onlnePlayer == null) continue;
            onlnePlayer.sendMessage(toSend);
        }
    }


    @RPacketHandler
    public void onGuildMuteChat(GuildMuteChatPacket packet) {}

    @RPacketHandler
    public void onGuildDelete(GuildDeletePacket packet) {}

    @RPacketHandler
    public void onGuildJoin(GuildJoinPacket packet) {}

    @RPacketHandler
    public void onGuildLeave(GuildLeavePacket packet) {}

    @RPacketHandler
    public void onGuildRoleChange(GuildRolePacket packet) {}

    @RPacketHandler
    public void onGuildTagChange(GuildTagPacket packet) {}
}