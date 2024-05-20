package studio.talespire.core;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import studio.lunarlabs.universe.UniverseVelocity;
import studio.lunarlabs.universe.UniverseVelocityPlugin;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @date 4/19/2024
 * @author Moose1301
 */

@Plugin(id = "core",
        name = "Core Velocity",
        version = "0.0.1",
        authors = "Talespire"
)
@Getter
public class CoreVelocityPlugin {

    private static CoreVelocityPlugin instance;

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataFolder;

    @Inject
    public CoreVelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataFolder) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    @Subscribe(
            order = PostOrder.FIRST
    )
    public void initPlugin(ProxyInitializeEvent event) {
        instance = this;
        new CoreVelocity(this.getClass().getAnnotation(Plugin.class), server, dataFolder);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

    }

    public static CoreVelocityPlugin get() {
        return instance;
    }
}
