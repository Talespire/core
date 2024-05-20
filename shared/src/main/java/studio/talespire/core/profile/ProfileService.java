package studio.talespire.core.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisCommand;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.util.Statics;
import studio.talespire.core.Core;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.adapter.GrantAdapter;
import studio.talespire.core.server.ServerService;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Moose1301
 * @date 4/25/2024
 */
public class ProfileService {
    private final static String CACHE_KEY = "core:profiles";

    private final MongoCollection<Document> profileCollection;
    private final Map<UUID, Profile> profiles = new ConcurrentHashMap<>();

    public ProfileService() {
        this.profileCollection = Core.getInstance().getDatabase().getCollection("profiles");
        Statics.registerTypeAdapter(Grant.class, new GrantAdapter());
    }

    public Profile getProfile(UUID uuid) {
        return this.profiles.get(uuid);
    }

    public Profile getOrLoadProfile(UUID playerId) {
        return getOrLoadProfile(playerId, null);
    }

    public Profile getOrLoadProfile(UUID playerId, String username) {
        if (this.profiles.containsKey(playerId)) {
            return this.profiles.get(playerId);
        }
        Document document = Mono.from(this.profileCollection.find(Filters.eq("_id", playerId.toString())).first()).block();
        Profile profile;
        if (document == null) {
            profile = Core.getInstance().createProfile(playerId, username);
            saveProfile(profile);
        } else {
            profile = Statics.gson().fromJson(document.toJson(), Core.getInstance().getProfileType());
        }
        profile.load();
        this.profiles.put(playerId, profile);
        return profile;
    }

    public void saveProfile(Profile profile) {
        Mono.from(this.profileCollection.replaceOne(
                Filters.eq("_id", profile.getUuid().toString()),
                Document.parse(Statics.plainGson().toJson(profile)),
                new ReplaceOptions().upsert(true)
        )).subscribe();

    }

    public void uncacheProfile(UUID playerId) {
        this.profiles.remove(playerId);
    }

    public void cachePlayerServer(UUID playerId) {
        Universe.get(RedisService.class).executeBackendCommand(jedis -> {
            jedis.hset(CACHE_KEY, playerId.toString(), Universe.get(ServerService.class).getCurrentServer().getServerId());
            return null;
        });
    }
    public void uncachePlayerServer(UUID playerId) {
        Universe.get(RedisService.class).executeBackendCommand(jedis -> {
            jedis.hdel(CACHE_KEY, playerId.toString());
            return null;
        });
    }
}
