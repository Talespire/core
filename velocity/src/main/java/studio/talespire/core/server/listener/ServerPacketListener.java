package studio.talespire.core.server.listener;

import com.velocitypowered.api.proxy.server.ServerInfo;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketListener;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.talespire.core.CoreVelocityPlugin;
import studio.talespire.core.server.VelocityServerService;
import studio.talespire.core.server.model.Server;
import studio.talespire.core.server.packet.ServerStartPacket;
import studio.talespire.core.server.packet.ServerStopPacket;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class ServerPacketListener implements RPacketListener {


    @RPacketHandler
    public void onServerStart(ServerStartPacket packet) {
        Server server = packet.getServer();
        if(server.getPlatform() != SystemType.BUKKIT) {
            return;
        }
        Universe.get(VelocityServerService.class).registerServer(server);

    }

    @RPacketHandler
    public void onServerStop(ServerStopPacket packet) {
        Server server = packet.getServer();
        if(server.getPlatform() != SystemType.BUKKIT) {
            return;
        }
        Universe.get(VelocityServerService.class).unregisterServer(server);
    }

}
