package studio.talespire.core;

import com.google.gson.reflect.TypeToken;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.lunarlabs.universe.util.logging.LogFactory;
import studio.lunarlabs.universe.util.logging.VelocityLogFactory;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.VelocityProfile;
import studio.talespire.core.profile.VelocityProfileService;
import studio.talespire.core.server.VelocityServerService;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class CoreVelocity extends Core {

    public CoreVelocity(Plugin plugin, ProxyServer server, Path dataFolder, Logger logger) {
        super(dataFolder, logger);

        Universe.get().getRegistry().put(VelocityServerService.class, new VelocityServerService());
        Universe.get().getRegistry().put(VelocityProfileService.class, new VelocityProfileService());
    }


    @Override
    public Type getProfileType() {
        return new TypeToken<VelocityProfile>() {}.getType();
    }

    @Override
    public Profile createProfile(UUID playerId, String username) {
        return new VelocityProfile(playerId, username);
    }
}
