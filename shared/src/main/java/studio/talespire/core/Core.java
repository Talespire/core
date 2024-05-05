package studio.talespire.core;

import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.Getter;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.mongo.MongoService;
import studio.lunarlabs.universe.util.Constants;
import studio.lunarlabs.universe.uuid.UUIDCache;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.rank.RankService;
import studio.talespire.core.server.ServerService;
import studio.talespire.core.setting.SettingService;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.UUID;

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

        Universe.get(UUIDCache.class).update(Constants.getConsoleUuid(), "Console", true);
        Universe.get().getRegistry().put(ServerService.class, new ServerService());
        Universe.get().getRegistry().put(RankService.class, new RankService());
        Universe.get().getRegistry().put(SettingService.class, new SettingService());
        Universe.get().getRegistry().put(ProfileService.class, new ProfileService());

    }
    public abstract Type getProfileType();
    public abstract Profile createProfile(UUID playerId, String username);

    public void disable() {
        Universe.get(ServerService.class).disable();
    }
}
