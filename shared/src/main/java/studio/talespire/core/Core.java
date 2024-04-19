package studio.talespire.core;

import lombok.Getter;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.server.ServerService;

import java.nio.file.Path;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
@Getter
public abstract class Core {
    @Getter
    private static Core instance;

    private final Path dataFolder;

    public Core(Path dataFolder) {
        instance = this;
        this.dataFolder = dataFolder;

        Universe.get().getRegistry().put(ServerService.class, new ServerService());
    }

    public void disable() {
        Universe.get(ServerService.class).disable();
    }
}
