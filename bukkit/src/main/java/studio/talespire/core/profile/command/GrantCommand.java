package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import me.andyreckt.raspberry.exception.InvalidArgumentException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.UniverseBukkit;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.util.Constants;
import studio.lunarlabs.universe.util.time.TimeUtil;
import studio.lunarlabs.universe.uuid.UUIDCache;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.types.GrantPermission;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.profile.menu.GrantMenu;
import studio.talespire.core.profile.menu.GrantsTypeMenu;
import studio.talespire.core.profile.packet.ProfileGrantPacket;
import studio.talespire.core.rank.Rank;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Moose1301
 * @date 4/28/2024
 */

public class GrantCommand {
    @Command(names = "grant", permission = "admin", description = "Grant a grant")
    public void execute(Player player, @Param(name = "Target") UUID playerId) {
        Profile profile = Universe.get(ProfileService.class).getOrLoadProfile(playerId, null);

        new GrantMenu(profile).openAsync(player);
    }
    @Command(names = "consolerankgrant", permission = "op", description = "Grant a permission", async = true, hidden = true)
    public void executeConsoleGrant(CommandSender sender,
                                    @Param(name = "Target") UUID playerId, @Param(name = "Rank")Rank rank,
                                    @Param(name = "Time") String timeStr, @Param(name = "Reason", wildcard = true) String reason) {
        if(sender instanceof Player) {
            return;
        }
        long time = TimeUtil.parseTime(timeStr);
        if (time == 0) {
            sender.sendMessage(ChatColor.RED + "Please input a valid duration");
            return;
        }
        Profile profile = Universe.get(ProfileService.class).getOrLoadProfile(playerId, null);

        sender.sendMessage(
                Component.text()
                        .append(Component.text("You granted", NamedTextColor.GOLD))
                        .append(Component.space())
                        .append(profile.getFormattedName())
                        .append(Component.space())
                        .append(rank.getPrefix())
        );
        Grant grant = new GrantRank(UUID.randomUUID(), rank, reason, time, Constants.getConsoleUuid(), System.currentTimeMillis());
        profile.getGrants().add(grant);
        Universe.get(RedisService.class).publish(new ProfileGrantPacket(profile.getUuid(), grant));
        Universe.get(ProfileService.class).saveProfile(profile);
    }
    @Command(names = "consolepermissiongrant", permission = "op", description = "Grant a rank", async = true, hidden = true)
    public void executeConsoleGrant(CommandSender sender,
                                    @Param(name = "Target") UUID playerId, @Param(name = "Permission")String permission,
                                    @Param(name = "Time") String timeStr, @Param(name = "Reason", wildcard = true) String reason) {

        if(sender instanceof Player) {
            return;
        }
        long time = TimeUtil.parseTime(timeStr);
        if (time == 0) {
            sender.sendMessage(ChatColor.RED + "Please input a valid duration");
            return;
        }
        Profile profile = Universe.get(ProfileService.class).getOrLoadProfile(playerId, null);

        sender.sendMessage(
                Component.text()
                        .append(Component.text("You granted", NamedTextColor.GOLD))
                        .append(Component.space())
                        .append(profile.getFormattedName())
                        .append(Component.space())
                        .append(Component.text(permission))
        );
        Grant grant = new GrantPermission(UUID.randomUUID(), permission, reason, time, Constants.getConsoleUuid(), System.currentTimeMillis());
        profile.getGrants().add(grant);
        Universe.get(RedisService.class).publish(new ProfileGrantPacket(profile.getUuid(), grant));
        Universe.get(ProfileService.class).saveProfile(profile);
    }
    @Command(names = "grants", permission = "admin", description = "List grants of player")
    public void executeGrants(Player player, @Param(name = "Target") UUID playerId) {

        Profile profile = Universe.get(ProfileService.class).getOrLoadProfile(playerId, null);

        new GrantsTypeMenu(profile).openAsync(player);
    }
}
