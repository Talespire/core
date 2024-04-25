package studio.talespire.core.server;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.placeholder.PlaceholderResolver;
import studio.talespire.core.server.model.Server;

/**
 * @date 4/24/2024
 * @author Moose1301
 */
public class ServerPlaceholder implements PlaceholderResolver {
    @Override
    public @Nullable String resolve(Player player, String key) {
        if(key.contains("_")) {
            String[] split = key.split("_");
            String type = split[0];

            String serverId = key.replace(type + "_", "");
            if(type.equals("display")) {
                Server server = Universe.get(ServerService.class).getServer(serverId);
                if(server != null) {
                    return server.getDisplayName();
                }
            } else if(type.equals("online")) {
                Server server = Universe.get(ServerService.class).getServer(serverId);
                if(server != null && server.hasMetadataValue("bukkit:online")) {
                    return String.valueOf(server.getMetadataValue("bukkit:online", Integer.class));
                }
            }
        }
        return null;
    }
}
