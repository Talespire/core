package studio.talespire.core.profile;

import com.velocitypowered.api.scheduler.TaskStatus;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.CoreVelocity;
import studio.talespire.core.CoreVelocityPlugin;
import studio.talespire.core.server.listener.PlayerServerListener;

import java.util.concurrent.TimeUnit;

/**
 * @author Moose1301
 * @date 5/19/2024
 */
public class VelocityProfileService {

    public VelocityProfileService() {
        CoreVelocityPlugin.get().getServer().getEventManager().register(CoreVelocityPlugin.get(), new PlayerServerListener());
        CoreVelocityPlugin.get().getServer().getScheduler().buildTask(CoreVelocityPlugin.get(),
                () -> Universe.get(ProfileService.class).saveRequiredProfiles())
                .repeat(ProfileService.AUTOSAVE_DURATION)
                .schedule();
    }
}
