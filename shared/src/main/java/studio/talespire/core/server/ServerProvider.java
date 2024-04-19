package studio.talespire.core.server;

import studio.talespire.core.server.model.Server;

import java.util.Map;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
public interface ServerProvider {
    void provide(Map<String, Object> metadata);
}
