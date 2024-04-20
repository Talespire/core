package studio.talespire.core.server.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.server.ServerService;
import studio.talespire.core.server.model.Server;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
@AllArgsConstructor @NoArgsConstructor @Getter
public class ServerStopPacket implements RPacket {
    private String serverId;
    @Override
    public void receive() {

    }

    public Server getServer() {
        return Universe.get(ServerService.class).getServer(serverId);
    }
}
