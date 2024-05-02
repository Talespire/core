package studio.talespire.core.profile.command;

import me.andyreckt.raspberry.annotation.Command;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.menu.SettingsMenu;

/**
 * @author Moose1301
 * @date 5/1/2024
 */
public class SettingsCommand {
    @Command(names = "settings")
    public void execute(Player player) {
        Profile profile = Universe.get(ProfileService.class).getProfile(player.getUniqueId());
        new SettingsMenu(profile).openAsync(player);
    }
}
