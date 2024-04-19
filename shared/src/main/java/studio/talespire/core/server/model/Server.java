package studio.talespire.core.server.model;

import lombok.Getter;
import lombok.Setter;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.systemtype.SystemType;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
@Getter @Setter
public class Server {
    private static final long MAX_HEARTBEAT_DELTA = 30_000L;

    private final String serverId;
    private String displayName;
    private String group, region;
    private SystemType platform;
    private Map<String, Object> metadata;


    private long heartbeat;

    public Server(String serverId, String displayName, String group, String  region) {
        this.serverId = serverId;
        this.displayName = displayName;
        this.group = group;
        this.region = region;
        this.platform = Universe.getSystemType();
        this.metadata = new HashMap<>();
        this.heartbeat = System.currentTimeMillis();

    }



    public boolean hasMetadataValue(String key) {
        return this.metadata.containsKey(key);
    }

    public void removeMetadataValue(String key) {
        this.metadata.remove(key);
    }

    public void setMetadataValue(String key, Object value) {
        if (value == null) {
            return;
        }

        this.metadata.put(key, value);
    }

    public <T> T getMetadataValue(String key, Class<T> clazz) {
        Object json = this.metadata.get(key);

        if (json == null) {
            return null;
        }

        return (T) json;
    }


}
