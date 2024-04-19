package studio.talespire.core.server.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studio.lunarlabs.universe.config.StaticConfiguration;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
@NoArgsConstructor @Setter
public class ServerConfig implements StaticConfiguration {
    private String serverId = "hub-01";
    private String displayName = "Lobby 1";
    private String group = "hub";
    private String region = "NA";
    private boolean dynamic = false;


    public String getServerId() {
        return System.getenv().getOrDefault("SERVER_ID", serverId);
    }
    public String getDisplayName() {
        return System.getenv().getOrDefault("SERVER_DISPLAY", displayName);
    }
    public String getGroup() {
        return System.getenv().getOrDefault("SERVER_GROUP", group);
    }
    public String getRegion() {
        return System.getenv().getOrDefault("SERVER_REGION", region);
    }

    public boolean isDynamic() {
        if(System.getenv().containsKey("SERVER_DYNAMIC")) {
            return Boolean.parseBoolean(System.getenv("SERVER_DYNAMIC"));
        }
        return dynamic;
    }
}
