package studio.talespire.core.server;

import com.velocitypowered.api.proxy.server.ServerInfo;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.talespire.core.CoreVelocityPlugin;
import studio.talespire.core.server.listener.PlayerServerListener;
import studio.talespire.core.server.listener.ServerPacketListener;
import studio.talespire.core.server.model.Server;

import java.net.InetSocketAddress;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class VelocityServerService {

    public VelocityServerService() {
        Universe.get(RedisService.class).registerListener(new ServerPacketListener());
        CoreVelocityPlugin.get().getServer().getEventManager().register(CoreVelocityPlugin.get(), new PlayerServerListener());
        this.registerExistingServers();
    }

    private void registerExistingServers() {
        for (Server server : Universe.get(ServerService.class).getServers()) {
            if (server.getPlatform() != SystemType.BUKKIT) {
                continue;
            }
            registerServer(server);
        }
    }

    public void registerServer(Server server) {
        ServerInfo serverInfo = buildServerInfo(server);
        if (serverInfo == null) {
            return;
        }
        CoreVelocityPlugin.get().getServer().registerServer(serverInfo);
    }

    public void unregisterServer(Server server) {
        ServerInfo serverInfo = buildServerInfo(server);
        if (serverInfo == null) {
            return;
        }
        CoreVelocityPlugin.get().getServer().unregisterServer(serverInfo);
    }


    private ServerInfo buildServerInfo(Server server) {
        if (server.getPlatform() != SystemType.BUKKIT) {
            return null;
        }
        if (!server.getRegion().equalsIgnoreCase(Universe.get(ServerService.class).getCurrentServer().getRegion())) {
            return null;
        }
        String ip = server.getMetadataValue("bukkit:ip", String.class);
        int port = server.getMetadataValue("bukkit:port", Integer.class);
        if (ip == null) {
            return null;
        }
        return new ServerInfo(server.getServerId(), new InetSocketAddress(ip, port));
    }
}
