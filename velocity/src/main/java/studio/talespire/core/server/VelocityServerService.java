package studio.talespire.core.server;

import studio.lunarlabs.universe.data.redis.packet.listener.RPacketHandler;
import studio.lunarlabs.universe.data.redis.packet.listener.RPacketListener;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.talespire.core.server.model.Server;
import studio.talespire.core.server.packet.ServerStartPacket;
import studio.talespire.core.server.packet.ServerStopPacket;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class VelocityServerService implements RPacketListener {

    public VelocityServerService() {

    }


    @RPacketHandler
    public void onServerStart(ServerStartPacket packet) {
        Server server = packet.getServer();
        if(server.getPlatform() != SystemType.BUKKIT) {
            return;
        }
        String ip = server.getMetadataValue("server:ip", String.class);
    }
    @RPacketHandler
    public void onServerStop(ServerStopPacket packet) {

    }
}
