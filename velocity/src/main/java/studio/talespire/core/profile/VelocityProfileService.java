package studio.talespire.core.profile;

import studio.talespire.core.CoreVelocityPlugin;
import studio.talespire.core.server.listener.PlayerServerListener;

/**
 * @author Moose1301
 * @date 5/19/2024
 */
public class VelocityProfileService {

    public VelocityProfileService() {
        CoreVelocityPlugin.get().getServer().getEventManager().register(CoreVelocityPlugin.get(), new PlayerServerListener());
    }
}
