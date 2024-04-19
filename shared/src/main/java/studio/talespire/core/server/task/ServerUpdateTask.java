package studio.talespire.core.server.task;

import studio.lunarlabs.universe.Universe;
import studio.talespire.core.server.ServerService;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
public class ServerUpdateTask implements Runnable {
    @Override
    public void run() {
        ServerService serverService = Universe.get(ServerService.class);

        serverService.updateCurrentSync();
        serverService.fetchServers();
    }
}
