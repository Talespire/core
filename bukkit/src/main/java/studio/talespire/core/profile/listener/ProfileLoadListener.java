package studio.talespire.core.profile.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.util.general.Tasks;
import studio.lunarlabs.universe.util.time.TimeUtil;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.model.Punishment;
import studio.talespire.core.profile.model.PunishmentType;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.util.SaltingUtils;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class ProfileLoadListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        Profile profile = Universe.get(ProfileService.class).getOrLoadProfile(event.getUniqueId(), event.getName());
        String address = SaltingUtils.salt(event.getAddress().getHostAddress());
        profile.getIpAddresses().add(address);
        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BLACKLIST);
        if (punishment == null) {
            punishment = profile.getActivePunishmentByType(PunishmentType.BAN);
        }

        if (punishment != null) {
            Component component = Component.text("Your account is " + punishment.getType().getContext() + " from Talespire", NamedTextColor.RED);
            if (!punishment.isPermanent()) {
                component = component.append(
                        Component.text("\nThis punishment expires in " + TimeUtil.formatTimeShort(punishment.getRemaining()) + ".")
                );
            }

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, component);
            return;
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Universe.get(ProfileService.class).uncacheProfile(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = Universe.get(ProfileService.class).getProfile(event.getPlayer().getUniqueId());
        Tasks.runAsync(profile::apply);


    }
}
