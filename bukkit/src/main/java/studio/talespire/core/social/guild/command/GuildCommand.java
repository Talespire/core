package studio.talespire.core.social.guild.command;

import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.menus.api.MenuHandler;
import studio.lunarlabs.universe.util.Statics;
import studio.lunarlabs.universe.uuid.UUIDCache;
import studio.talespire.core.CorePlugin;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.setting.types.privacy.GuildInviteSetting;
import studio.talespire.core.social.guild.GuildService;
import studio.talespire.core.social.guild.command.param.GuildParameter;
import studio.talespire.core.social.guild.menus.GuildLandingPage;
import studio.talespire.core.social.guild.menus.GuildSettings;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.model.GuildMember;
import studio.talespire.core.social.guild.model.GuildPermission;
import studio.talespire.core.social.guild.model.GuildRole;
import studio.talespire.core.social.guild.packet.*;
import studio.talespire.core.utils.MenuUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Moose1301
 * @date 5/5/2024
 */

@Command(names = {"guild", "g"}, description = "Guild Commands")
public class GuildCommand {

    /**
     * Creates a new guild
     * @param player
     * @param name
     */
    @Children(names = "create", permission = "hero", async = true, description = "Creates a new guild")
    public void handleCreate(Player player, @Param(name = "Name", wildcard = true) String name) {
        // Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        // Checks if the player is already in a guild, and if the name is valid
        if (profile.getGuild() != null) {
            player.sendMessage(Component.text("You are already in a guild", NamedTextColor.RED));
            return;
        }
        if (!GuildService.NAME_REGEX.matcher(name).matches()) {
            player.sendMessage(Component.text("That is not a valid guild name", NamedTextColor.RED));
            return;
        }

        // Create a new Guild
        Guild guild = new Guild(player.getUniqueId(), name);
        Universe.get(GuildService.class).registerGuild(guild);
        profile.setGuildId(guild.getUuid());
        Universe.get(ProfileService.class).saveProfile(profile);
        Universe.get(RedisService.class).publish(new GuildCreatePacket(guild.getUuid()));

        player.sendMessage(Component.text("You have created a guild with the name", NamedTextColor.GREEN)
                .append(Component.space())
                .append(Component.text(name, NamedTextColor.YELLOW)));

        // Open the guild's menu
        Universe.get(MenuHandler.class).openMenuAsync(player, new GuildLandingPage(player));
    }

    /**
     * Disbands the guild
     * @param player
     */
    @Children(names = "disband", permission = "hero", async = true, description = "Disbands the guild")
    public void handleDisband(Player player) {
        // Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        // Check if the player is in a guild and if they are the leader
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        if (!profile.getGuild().getLeader().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("You are not the leader of the guild", NamedTextColor.RED));
            return;
        }

        // Disband the guild
        Guild guild = profile.getGuild();

        guild.getMembers().forEach((key, guildMember) -> {
            Player p = Bukkit.getPlayer(guildMember.getPlayerId());
            if (p != null) {
                p.sendMessage(Component.text("The guild has been disbanded", NamedTextColor.RED));
            }
        });

        guild.getMembers().forEach((key, guildMember) -> {
                    Profile memberProfile = Universe.get(ProfileService.class).getProfile(guildMember.getPlayerId());
                    memberProfile.setGuildId(null);
                    Universe.get(ProfileService.class).saveProfile(memberProfile);
        });

        Universe.get(GuildService.class).deleteGuild(guild);
        Universe.get(ProfileService.class).saveProfile(profile);
        Universe.get(RedisService.class).publish(new GuildDeletePacket(guild.getUuid()));

        player.sendMessage(Component.text("You have disbanded the guild", NamedTextColor.GREEN)
                .append(Component.space())
                .append(Component.text(guild.getName(), NamedTextColor.YELLOW)));
    }

    /**
     * Invites a player to the guild
     * @param player
     * @param targetId
     */
    @Children(names = "invite", async = true, description = "Invites a player to your guild")
    public void handleInvite(Player invitor, @Param(name = "Target") Player target) {
        // Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(invitor.getUniqueId());

        // Check if the player is in a guild, if they have perms to invite, if the target is already in the guild, if the target has already been invited, and if the target can be invited
        if (profile.getGuild() == null) {
            invitor.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Guild guild = profile.getGuild();
        if (!guild.hasPermission(invitor.getUniqueId(), GuildPermission.INVITE)) {
            invitor.sendMessage(Component.text()
                    .append(Component.text("You do not have permission to do this", NamedTextColor.RED))
            );
            return;
        }
        Profile targetProfile = Universe.get(ProfileService.class).getOrLoadProfile(target.getUniqueId());
        if (guild.getMembers().containsKey(targetProfile.getUuid())) {
            invitor.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" is already in your guild", NamedTextColor.RED))
            );
            return;
        }
        if (guild.getInvites().containsKey(targetProfile.getUuid())) {
            invitor.sendMessage(Component.text()
                    .append(targetProfile.getFormattedName())
                    .append(Component.text(" has already been invited to your guild", NamedTextColor.RED))
            );
            return;
        }
        if (!targetProfile.getSetting(GuildInviteSetting.class).value().doesMatch(profile, targetProfile)) {
            invitor.sendMessage(Component.text("You cannot invite ", NamedTextColor.RED)
                    .append(Component.text(targetProfile.getUsername(), NamedTextColor.YELLOW))
            );
            return;
        }

        invitor.sendMessage(Component.text("You have invited", NamedTextColor.GREEN)
                .append(Component.space())
                .append(targetProfile.getFormattedName())
                .append(Component.space())
                .append(Component.text("to join the guild", NamedTextColor.GREEN)));

        // Invite the player to the guild
        guild.createInvite(profile.getUuid(), targetProfile.getUuid());
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildInvitePacket(guild.getUuid(), profile.getUuid(), targetProfile.getUuid()));

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.getUniqueId() == (target.getUniqueId())) {
                p.sendMessage(Component.text("You have been invited to join the guild", NamedTextColor.GREEN)
                        .append(Component.space())
                        .append(Component.text(guild.getName(), NamedTextColor.YELLOW))
                        .append(Component.space())
                        .append(Component.text("by", NamedTextColor.GREEN))
                        .append(Component.space())
                        .append(profile.getFormattedName())
                );
            }
        });
    }

    /**
     * Joins a player to the guild
     * @param player
     * @param guild
     */
    @Children(names = "join", async = true, description = "Joins a guild")
    public void handleJoin(Player joinee, @Param(name = "Player", wildcard = true) Player target) {
        Profile joineeProfile = Universe.get(ProfileService.class).getProfile(joinee.getUniqueId());
        Profile targetProfile = Universe.get(ProfileService.class).getProfile(target.getUniqueId());

        // Check if either the joinee is already in a guild or if the target isn't
        if (joineeProfile.getGuild() != null) {
            joinee.sendMessage(Component.text("You are already in a guild", NamedTextColor.RED));
            return;
        }
        if (targetProfile.getGuild() == null) {
            joinee.sendMessage(Component.text("The player is not in a guild", NamedTextColor.RED));
            return;
        }

        Guild guild = Universe.get(ProfileService.class).getProfile(target.getUniqueId()).getGuild();

        // If the player already has an invite to the guild
        if (guild.hasInvite(joinee.getUniqueId())) {

            // Remove the joinee's pending invite and add them to the guild
            guild.removeInvite(joinee.getUniqueId());
            guild.addMember(joinee.getUniqueId());
            joineeProfile.setGuildId(guild.getUuid());
            Universe.get(ProfileService.class).saveProfile(joineeProfile);
            Universe.get(GuildService.class).saveGuild(guild);
            Universe.get(RedisService.class).publish(new GuildJoinPacket(guild.getUuid(), joinee.getUniqueId()));

            // Broadcast to all the guild members that the joinee has joined
            guild.getMembers().forEach((key, guildMember) -> {
                Player p = Bukkit.getPlayer(guildMember.getPlayerId());
                if (p != null) {
                    p.sendMessage(Component.text()
                            .append(Component.text("Player ", NamedTextColor.AQUA))
                            .append(Component.text(joineeProfile.getUsername(), NamedTextColor.YELLOW))
                            .append(Component.text(" has joined the guild", NamedTextColor.AQUA))
                    );
                }
            });

            return;
        // If the target player's guild is already full
        } else if (guild.getMembers().size() == 125) {
            joinee.sendMessage(Component.text("The guild is full!", NamedTextColor.RED));
            return;
        // Otherwise send a request to join the guild
        } else {
            // Give the joinee a request packet and broadcast to all the guild members that they are requesting to join
            Universe.get(RedisService.class).publish(new GuildJoinRequestPacket(guild.getUuid(), joinee.getUniqueId()));

            guild.getMembers().forEach((key, guildMember) -> {
                Player p = Bukkit.getPlayer(guildMember.getPlayerId());
                if (p != null) {
                    p.sendMessage(Component.text()
                            .append(Component.text("Player ", NamedTextColor.AQUA))
                            .append(Component.text(joineeProfile.getUsername(), NamedTextColor.YELLOW))
                            .append(Component.text(" has requested to join the guild", NamedTextColor.AQUA))
                    );
                }
            });
        }
    }

    /**
     * Accepts an invite to the guild
     * @param player
     * @param guild
     */
    @Children(names = "accept", async = true, description = "Accepts an invite to the guild")
    public void handleAccept(Player invitor, @Param(name = "Player", wildcard = true) Player invitee) {
        Guild guild = Universe.get(ProfileService.class).getProfile(invitor.getUniqueId()).getGuild();
        Profile joineeProfile = Universe.get(ProfileService.class).getProfile(invitee.getUniqueId());

        if (guild.hasInvite(invitee.getUniqueId())) {

            guild.removeInvite(invitee.getUniqueId());
            guild.addMember(invitee.getUniqueId());
            joineeProfile.setGuildId(guild.getUuid());

            Universe.get(GuildService.class).saveGuild(guild);
            Universe.get(RedisService.class).publish(new GuildJoinPacket(guild.getUuid(), invitee.getUniqueId()));

            // Broadcast to all the guild members that the joinee has joined
            guild.getMembers().forEach((key, guildMember) -> {
                Player p = Bukkit.getPlayer(guildMember.getPlayerId());
                if (p != null) {
                    p.sendMessage(Component.text()
                            .append(Component.text("Player ", NamedTextColor.AQUA))
                            .append(Component.text(joineeProfile.getUsername(), NamedTextColor.YELLOW))
                            .append(Component.text(" has joined the guild", NamedTextColor.AQUA))
                    );
                }
            });
        } else {
            invitor.sendMessage(Component.text("The player does not have an incoming invite request", NamedTextColor.RED));
        }
    }

    /**
     * Opens the guild menu
     * @param player
     */
    @Children(names = "open", permission = "*", async = true, description = "Opens the guild menu")
    public void handleOpen(Player player) {
        if (Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }
        Universe.get(MenuHandler.class).openMenuAsync(player, new GuildLandingPage(player));
    }

    /**
     * Grabs the most relevent information of the guild
     * @param player
     * @param guild
     */
    @Children(names = "info", async = true, description = "Gets the info of the guild")
    public void handleInfo(Player player) {

        if (Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }

        Guild guild = Universe.get(ProfileService.class).getProfile(player.getUniqueId()).getGuild();
        final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        // Create a text component with the guild's information
        Component text = Component.text()
                .append(MenuUtils.chatSeparator())
                .append(MenuUtils.centerSeparator(10, Component.text("\n" + guild.getName(), NamedTextColor.GOLD)))
                .append(Component.text("\n"))
                .append(Component.text("\nCreated: ", NamedTextColor.AQUA), Component.text(dateFormat.format(guild.getCreatedAt()), NamedTextColor.YELLOW))
                .append(Component.text("\nDescription: ", NamedTextColor.AQUA), Component.text(guild.getDescription(), NamedTextColor.YELLOW))
                .append(Component.text("\n"))
                .append(Component.text("\nMembers: ", NamedTextColor.AQUA), Component.text(guild.getMembers().size(), NamedTextColor.YELLOW))
                .append(Component.text("\nDiscord: ", NamedTextColor.AQUA), Component.text(guild.getDiscord(), NamedTextColor.YELLOW))
                .append(Component.text("\n"),MenuUtils.chatSeparator())
                .build();

        // Send it to the player
        player.sendMessage(text);
    }

    /**
     *  Opens the Settings menu for that player
     * @param player
     */
    @Children(names = "settings", async = true, description = "Opens your current guild's settings")
    public void handleSettings(Player player) {
        // Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        // Check if the player ins't in a guild
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }

        // Otherwise opens the settings menu
        Guild guild = profile.getGuild();
        Universe.get(MenuHandler.class).openMenuAsync(player, new GuildSettings());
    }

    /**
     * Mutes a player and prevents them from being able to speak in guild chat
     * @param player
     * @param target
     */
    @Children(names = "mute", async = true, description = "<Target/All> Mutes the chat for the guild")
    public void handleMute(Player player, @Param(name = "Target") String target) {
        // Instance Variables
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());

        // Checks if the player is in a guild
        if (profile.getGuild() == null) {
            player.sendMessage(Component.text("You are not in a guild", NamedTextColor.RED));
            return;
        }

        // If they are, check to what scope and permission that player has
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

    /**
     * Allows the user to set the guild's description
     * @param player
     * @param description
     */
    @Children(names = "description", async = true, description = "Sets the guild's description")
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

    @Children(names = "tag", async = true, description = "Sets the guild [TAG]")
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

//        final Component component = PlainTextComponentSerializer.plainText().deserialize(tag);
        guild.setTag(tag);
        Universe.get(GuildService.class).saveGuild(guild);
        Universe.get(RedisService.class).publish(new GuildTagPacket(guild.getUuid(), player.getUniqueId(), tag));
    }


    @Children(names = "kick", async = true, description = "Kicks the player from your guild")
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

    @Children(names = "promote", async = true, description = "Promotes the player to the next rank")
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

    @Children(names = "transfer", async = true, description = "Transfers ownership of the guild to another player")
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

    @Children(names = "demote", async = true, description = "Demotes the player to the previous rank")
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

    public void SendGuildMessage(Guild guild, Component message) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (guild.isMember(p.getUniqueId())) {
                p.sendMessage(message);
            }
        });
    }
}
