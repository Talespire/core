package studio.talespire.core.server.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.server.ServerService;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class PlayerServerListener {

    @Subscribe(order = PostOrder.NORMAL)
    public void onProxyPing(ProxyPingEvent event) {
        ServerPing.Builder ping = event.getPing().asBuilder();
        int online = Universe.get(ServerService.class).getNetworkPlayerCount();
        ping.onlinePlayers(online);
        event.setPing(ping.build());
    }
}
