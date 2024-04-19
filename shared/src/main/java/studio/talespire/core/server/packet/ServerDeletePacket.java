package studio.talespire.core.server.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.packet.RPacket;
import studio.talespire.core.Core;
import studio.talespire.core.server.ServerService;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServerDeletePacket implements RPacket {
    private String serverId;

    @Override
    public void receive() {
        Universe.get(ServerService.class).deleteServer(this.serverId);
    }
}
