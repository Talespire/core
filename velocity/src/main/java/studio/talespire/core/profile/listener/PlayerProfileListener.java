package studio.talespire.core.profile.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.ProfileService;

/**
 * @author Moose1301
 * @date 5/19/2024
 */
public class PlayerProfileListener {

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        Universe.get(ProfileService.class).uncachePlayerServer(event.getPlayer().getUniqueId());
    }

}
