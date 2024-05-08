package studio.talespire.core.social.guild.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.util.Statics;
import studio.lunarlabs.universe.uuid.UUIDCache;
import studio.talespire.core.CorePlugin;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.setting.types.privacy.GuildInviteSetting;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.command.param.GuildParameter;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildMember;
import studio.talespire.core.social.guild.model.GuildPermission;
import studio.talespire.core.social.guild.model.GuildRole;
import studio.talespire.core.social.guild.packet.GuildInvitePacket;
import studio.talespire.core.social.guild.packet.GuildMuteChatPacket;
import studio.talespire.core.social.guild.packet.GuildRolePacket;
import studio.talespire.core.social.guild.packet.GuildTagPacket;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Moose1301
 * @date 5/5/2024
 */

@Command(names = "guild", description = "Guild Commands")
public class GuildCommand {
    @Children(names = "create", permission = "hero", async = true)
    public void handleCreate(Player player, @Param(name = "Name", wildcard = true) String name) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() != null) {
            player.sendMessage(Component.text("You are already in a guild", NamedTextColor.RED));
            return;
        }
        if (!GuildService.NAME_REGEX.matcher(name).matches()) {
            player.sendMessage(Component.text("That is not a valid guild name", NamedTextColor.RED));
            return;
        }

        Guild guild = new Guild(player.getUniqueId(), name);
        Universe.get(GuildService.class).registerGuild(guild);
        profile.setGuildId(guild.getUuid());
        Universe.get(ProfileService.class).saveProfile(profile);
        player.sendMessage(Component.text("You have created a guild with the name", NamedTextColor.GREEN)
                .append(Component.space())
                .append(Component.text(name, NamedTextColor.YELLOW)));
    }

    @Children(names = "info", async = true)
    public void handleInfo(Player player, @Param(name = "Guild", baseValue = GuildParameter.DEFAULT_VALUE_SELF) Guild guild) {
        player.sendMessage(Statics.gson().toJson(guild));
    }

    @Children(names = "invite", async = true)
    public void handleInvite(Player player, @Param(name = "Target") UUID targetId) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.INVITE)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(targetId);
        if (guild.getMembers().containsKey(targetProfile.getUuid())) {
            player.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" is already in your guild", NamedTextColor.RED))
            );
            return;
        }
        if (guild.getInvites().containsKey(targetProfile.getUuid())) {
            player.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" has already been invited to your guild", NamedTextColor.RED))
            );
            return;
        }
        if (!targetProfile.getSetting(GuildInviteSetting.class).value().doesMatch(profile, targetProfile)) {
            player.sendMessage(Component.text("You cannot invite ", NamedTextColor.RED)
                    .append(Component.text(targetProfile.getUsername(), NamedTextColor.YELLOW))
            );
            return;
        }

        guild.createInvite(profile.getUuid(), targetProfile.getUuid());
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildInvitePacket(guild.getUuid(), profile.getUuid(), targetProfile.getUuid()));
    }

    @Children(names = "settings", async = true)
    public void handleInvite(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        //open the menu for seting it
    }

    @Children(names = "mute", async = true)
    public void handleMute(Player player, @Param(name = "Target") String target) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();

        if (target.equalsIgnoreCase("all")) {
            if (!guild.hasPermission(player.getUniqueId(), GuildPermission.MUTE_ALL)) {
                player.sendMessage(Component.text()
                        .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
                );
                return;
            }
            guild.setMutechat(!guild.isMutechat());
            Universe.get(RedisService.class).publish(new GuildMuteChatPacket(
                    guild.getUuid(), player.getUniqueId(), guild.isMutechat()
            ));

            return;
        }
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.MUTE)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        UUID targetId = null;
        try {
            targetId = Universe.get(UUIDCache.class).uuid(target).get(250, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            CorePlugin.getInstance().getLogger().severe("Failed to get uuid of player " + target);
            e.printStackTrace();
        }
        if (targetId == null) {
            player.sendMessage(Component.text()
                    .append(Component.text("Unknown User", NamedTextColor.RED))
                    .append(Component.space())
                    .append(Component.text(target, NamedTextColor.YELLOW))
            );
            return;
        }
        if (player.getUniqueId().equals(targetId)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You cannot promote yourself", NamedTextColor.RED))
            );
            return;
        }
        if (!guild.isMember(targetId)) {
            player.sendMessage(Component.text()
                    .append(Component.text(target, NamedTextColor.RED))
                    .append(Component.space())
                    .append(Component.text("is not in your guild", NamedTextColor.RED))
            );
            return;
        }
        boolean state = !guild.getMutedPlayers().contains(targetId);
        if (state) {
            guild.getMutedPlayers().add(targetId);
        } else {
            guild.getMutedPlayers().remove(targetId);
        }
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildMuteChatPacket(
                guild.getUuid(), player.getUniqueId(), state, targetId
        ));
    }


    @Children(names = "description", async = true)
    public void handleDescription(Player player, @Param(name = "Description", wildcard = true) String description) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.DESCRIPTION)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        guild.setDescription(description);
        Universe.get(GuildService.class).saveGuild(guild);
    }

    @Children(names = "tag", async = true)
    public void handleTag(Player player, @Param(name = "Description") String tag) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        if (!GuildService.NAME_REGEX.matcher(tag).matches() || (tag.length() > 5 || tag.length() < 3)) {
            player.sendMessage(Component.text("That is not a valid guild tag", NamedTextColor.RED));
            return;
        }

        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.TAG)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        guild.setTag(tag);
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildTagPacket(guild.getUuid(), player.getUniqueId(), tag));
    }


    @Children(names = "kick", async = true)
    public void handleKick(Player player, @Param(name = "Target") UUID target) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.KICK)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        String name;
        try {
            name = Universe.get(UUIDCache.class).getName(target).get(250, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            name = target.toString();
            CorePlugin.getInstance().getLogger().severe("Failed to get name of " + target.toString());
            e.printStackTrace();
        }

        if (!guild.isMember(target)) {
            player.sendMessage(Component.text()
                    .append(Component.text(name, NamedTextColor.RED))
                    .append(Component.space())
                    .append(Component.text("is not in your guild", NamedTextColor.RED))
            );
            return;
        }
        GuildMember member = guild.getMember(player.getUniqueId());
        GuildMember targetMember = guild.getMember(target);
        if (member.getPlayerId().equals(targetMember.getPlayerId())) {
            player.sendMessage(Component.text()
                    .append(Component.text("You cannot promote yourself", NamedTextColor.RED))
            );
            return;
        }
        if (member.getRole().ordinal() <= targetMember.getRole().ordinal()) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do cannot kick someone with a higher or the same role as you", NamedTextColor.RED))
            );
            return;
        }
        guild.kickMember(player.getUniqueId(), target);
    }

    @Children(names = "promote", async = true)
    public void handlePromote(Player player, @Param(name = "Target") UUID target) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.PROMOTE)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        String name;
        try {
            name = Universe.get(UUIDCache.class).getName(target).get(250, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            name = target.toString();
            CorePlugin.getInstance().getLogger().severe("Failed to get name of " + target.toString());
            e.printStackTrace();
        }
        if (!guild.isMember(target)) {
            player.sendMessage(Component.text()
                    .append(Component.text(name, NamedTextColor.RED))
                    .append(Component.space())
                    .append(Component.text("is not in your guild", NamedTextColor.RED))
            );
            return;
        }
        GuildMember member = guild.getMember(player.getUniqueId());
        GuildMember targetMember = guild.getMember(target);
        if (member.getPlayerId().equals(targetMember.getPlayerId())) {
            player.sendMessage(Component.text()
                    .append(Component.text("You cannot promote yourself", NamedTextColor.RED))
            );
            return;
        }
        if (member.getRole().ordinal() <= targetMember.getRole().ordinal()) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do cannot promote someone with a higher or the same role as you", NamedTextColor.RED))
            );
            return;
        }
        GuildRole nextRole = targetMember.getRole().getNext();
        if (nextRole == GuildRole.LEADER) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do cannot promote someone with to leader. To do this do /guild leader <name>", NamedTextColor.RED))
            );
            return;
        }
        targetMember.setRole(nextRole);
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildRolePacket(guild.getUuid(), player.getUniqueId(), target, false));
    }

    @Children(names = "leader", async = true)
    public void handleLeader(Player player, @Param(name = "Target") UUID target) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildRole.LEADER)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        String name;
        try {
            name = Universe.get(UUIDCache.class).getName(target).get(250, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            name = target.toString();
            CorePlugin.getInstance().getLogger().severe("Failed to get name of " + target.toString());
            e.printStackTrace();
        }
        if (!guild.isMember(target)) {
            player.sendMessage(Component.text()
                    .append(Component.text(name, NamedTextColor.RED))
                    .append(Component.space())
                    .append(Component.text("is not in your guild", NamedTextColor.RED))
            );
            return;
        }
        GuildMember member = guild.getMember(player.getUniqueId());
        GuildMember targetMember = guild.getMember(target);
        if (member.getPlayerId().equals(targetMember.getPlayerId())) {
            player.sendMessage(Component.text()
                    .append(Component.text("You cannot set your as leader", NamedTextColor.RED))
            );
            return;
        }

        targetMember.setRole(GuildRole.LEADER);
        member.setRole(GuildRole.CAPTAIN);
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildRolePacket(guild.getUuid(), player.getUniqueId(), target, false));
    }

    @Children(names = "demote", async = true)
    public void handleDemote(Player player, @Param(name = "Target") UUID target) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(player.getUniqueId(), GuildPermission.PROMOTE)) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        String name;
        try {
            name = Universe.get(UUIDCache.class).getName(target).get(250, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            name = target.toString();
            CorePlugin.getInstance().getLogger().severe("Failed to get name of " + target.toString());
            e.printStackTrace();
        }
        if (!guild.isMember(target)) {
            player.sendMessage(Component.text()
                    .append(Component.text(name, NamedTextColor.RED))
                    .append(Component.space())
                    .append(Component.text("is not in your guild", NamedTextColor.RED))
            );
            return;
        }
        GuildMember member = guild.getMember(player.getUniqueId());
        GuildMember targetMember = guild.getMember(target);
        if (member.getPlayerId().equals(targetMember.getPlayerId())) {
            player.sendMessage(Component.text()
                    .append(Component.text("You cannot demote yourself", NamedTextColor.RED))
            );
            return;
        }
        if (member.getRole().ordinal() <= targetMember.getRole().ordinal()) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do cannot demote someone with a higher or the same role as you", NamedTextColor.RED))
            );
            return;
        }
        GuildRole nextRole = targetMember.getRole().getNext();
        if (nextRole == GuildRole.LEADER) {
            player.sendMessage(Component.text()
                    .append(Component.text("You do cannot demote someone lower then member.", NamedTextColor.RED))
            );
            return;
        }
        targetMember.setRole(nextRole);
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildRolePacket(guild.getUuid(), player.getUniqueId(), target, true));
    }
}
