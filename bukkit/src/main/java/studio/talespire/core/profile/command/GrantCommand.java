package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import me.andyreckt.raspberry.exception.InvalidArgumentException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.uuid.UUIDCache;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.menu.GrantMenu;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Moose1301
 * @date 4/28/2024
 */

public class GrantCommand {
    @Command(names = "grant", permission = "op", description = "Grant a grant")
    public void execute(Player player, @Param(name = "Target") UUID playerId) {

        Profile profile = Universe.get(ProfileService.class).getOrLoadProfile(playerId, null);

        new GrantMenu(profile).openAsync(player);
    }

}
