package studio.talespire.core;

import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.Getter;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.mongo.MongoService;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.rank.RankService;
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
    private final MongoDatabase database;

    public Core(Path dataFolder) {
        instance = this;
        this.dataFolder = dataFolder;
        this.database = Universe.get(MongoService.class).getClient().getDatabase("core");

        Universe.get().getRegistry().put(ServerService.class, new ServerService());
        Universe.get().getRegistry().put(RankService.class, new RankService());
        Universe.get().getRegistry().put(ProfileService.class, new ProfileService());
    }

    public void disable() {
        Universe.get(ServerService.class).disable();
    }
}
