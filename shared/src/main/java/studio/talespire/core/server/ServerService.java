package studio.talespire.core.server;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.config.ConfigService;
import studio.lunarlabs.universe.registry.ServiceRegistry;
import studio.lunarlabs.universe.util.Ref;
import studio.lunarlabs.universe.util.Statics;
import studio.talespire.core.Core;
import studio.talespire.core.server.config.ServerConfig;
import studio.talespire.core.server.model.Server;
import studio.talespire.core.server.packet.ServerStartPacket;
import studio.talespire.core.server.task.ServerUpdateTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
public class ServerService {
    private static final String KEY = "core:servers";

    private final Set<ServerProvider> providers = new HashSet<>();
    private final Map<String, Server> servers = new HashMap<>();

    @Getter
    private final Server currentServer;
    private final ServerConfig config;

    public ServerService() {
        ServiceRegistry registry = Universe.get().getRegistry();
        ConfigService configService = Ref.getConfigService();

        config = configService.loadConfiguration(ServerConfig.class, "server", Core.getInstance().getDataFolder());
        registry.put(ServerConfig.class, config);

        this.currentServer = new Server(config.getServerId(), config.getDisplayName(), config.getGroup(), config.getRegion());

        this.updateCurrentSync();
        Ref.getRedisService().publish(new ServerStartPacket(config.getServerId()));
        Universe.TASK_CHAIN.scheduleAtFixedRate(new ServerUpdateTask(), 0, 5, TimeUnit.SECONDS);
    }
    public void disable() {
        if(!config.isDynamic()) {
            return;
        }
        Ref.getRedisService().publish(new ServerStartPacket(config.getServerId()));
    }

    public void updateCurrentSync() {
        currentServer.setHeartbeat(System.currentTimeMillis());
        for (ServerProvider provider : this.providers) {
            provider.provide(currentServer.getMetadata());
        }
        Ref.getRedisService().executeBackendCommand(redis -> {
            redis.hset(KEY, currentServer.getServerId(), Statics.gson().toJson(currentServer));
            return null;
        });
    }

    public void fetchServer(String serverId) {
        Ref.getRedisService().executeBackendCommand(redis -> {
            servers.put(serverId, Statics.gson().fromJson(redis.hget(KEY, serverId), Server.class));
            return null;
        });
    }

    public void fetchServers() {
        Ref.getRedisService().executeBackendCommand(redis -> {
            for (Map.Entry<String, String> entry : redis.hgetAll(KEY).entrySet()) {
                servers.put(entry.getKey(), Statics.gson().fromJson(entry.getValue(), Server.class));
            }
            return null;
        });
    }
    public void deleteServer(String serverId) {
        this.servers.remove(serverId);
    }
    public @Nullable Server getServer(String serverId) {
        return this.servers.get(serverId);
    }

    public void registerProvider(ServerProvider provider) {
        this.providers.add(provider);
    }

    public void unregisterProvider(ServerProvider provider) {
        this.providers.remove(provider);
    }
}
