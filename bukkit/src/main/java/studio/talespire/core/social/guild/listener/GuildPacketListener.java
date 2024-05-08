package studio.talespire.core.social.guild.listener;

import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketListener;
import studio.talespire.core.social.guild.packet.*;

/**
 * @author Moose1301
 * @date 5/7/2024
 */

public class GuildPacketListener implements RPacketListener {


    @RPacketHandler
    public void onGuildInvite(GuildInvitePacket packet) {}


    @RPacketHandler
    public void onGuildChat(GuildChatPacket packet) {}


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
