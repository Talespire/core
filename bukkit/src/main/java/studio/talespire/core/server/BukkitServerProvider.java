package studio.talespire.core.server;

import org.bukkit.Bukkit;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.placeholder.PlaceholderService;
import studio.talespire.core.server.model.Server;

import java.util.Map;

/**
 * @date 4/19/2024
 * @author Moose1301
 */
public class BukkitServerProvider implements ServerProvider {

    public BukkitServerProvider() {
       Server currentServer = Universe.get(ServerService.class).getCurrentServer();

        currentServer.getMetadata().put("bukkit:ip", Bukkit.getIp());
        currentServer.getMetadata().put("bukkit:port", Bukkit.getServer().getPort());

        Universe.get(PlaceholderService.class).registerPlaceholder("server", new ServerPlaceholder());
    }
    @Override
    public void provide(Map<String, Object> metadata) {
        metadata.put("bukkit:online", Bukkit.getOnlinePlayers().size());
        metadata.put("bukkit:maxplayers", Bukkit.getMaxPlayers());
    }
}
