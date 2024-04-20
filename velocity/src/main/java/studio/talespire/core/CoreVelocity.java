package studio.talespire.core;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.lunarlabs.universe.util.logging.LogFactory;
import studio.lunarlabs.universe.util.logging.VelocityLogFactory;
import studio.talespire.core.server.VelocityServerService;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class CoreVelocity extends Core {

    public CoreVelocity(Plugin plugin, ProxyServer server, Path dataFolder) {
        super(dataFolder);

        Universe.get().getRegistry().put(VelocityServerService.class, new VelocityServerService());
    }
}
