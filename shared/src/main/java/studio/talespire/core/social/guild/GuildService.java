package studio.talespire.core.social.guild;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.jetbrains.annotations.ApiStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.util.Statics;
import studio.talespire.core.Core;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.social.guild.packet.GuildCreatePacket;
import studio.talespire.core.social.guild.packet.GuildDeletePacket;
import studio.talespire.core.social.guild.packet.GuildUpdatePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Moose1301
 * @date 5/4/2024
 */
public class GuildService {
    public static final Pattern NAME_REGEX = Pattern.compile("^[a-zA-Z0-9 ]+");

    private final MongoCollection<Document> guildCollection;
    private final Map<UUID, Guild> guilds = new HashMap<>();
    private final Map<String, UUID> nameToGuild = new HashMap<>();

    public GuildService() {
        this.guildCollection = Core.getInstance().getDatabase().getCollection("guilds");

        for (Document document : Flux.from(this.guildCollection.find()).toIterable()) {
            Guild guild = Statics.gson().fromJson(document.toJson(), Guild.class);

            this.guilds.put(guild.getUuid(), guild);
            this.nameToGuild.put(guild.getName().toLowerCase(), guild.getUuid());
        }
    }

    public Guild getGuild(UUID guildId) {
        return this.guilds.get(guildId);
    }

    public Guild getGuild(String name) {
        return getGuild(this.nameToGuild.get(name.toLowerCase()));
    }

    public void forgetGuild(UUID guildId) {
        Guild guild = this.guilds.remove(guildId);
        if(guild == null) return;
        this.nameToGuild.remove(guild.getName());
    }

    public void deleteGuild(Guild guild) {
        this.guilds.remove(guild.getUuid());
        this.nameToGuild.remove(guild.getName());
        Mono.from(this.guildCollection.deleteOne(
                Filters.eq("_id", guild.getUuid().toString())
        )).subscribe();
        Universe.get(RedisService.class).publish(new GuildDeletePacket(guild.getUuid()));
    }

    public void saveGuild(Guild guild) {
        Mono.from(this.guildCollection.replaceOne(
                Filters.eq("_id", guild.getUuid().toString()),
                Document.parse(Statics.plainGson().toJson(guild)),
                new ReplaceOptions().upsert(true)
        )).subscribe();
        Universe.get(RedisService.class).publish(new GuildUpdatePacket(guild.getUuid()));
        this.nameToGuild.put(guild.getName(), guild.getUuid());
    }

    public void registerGuild(Guild guild) {
        this.guilds.put(guild.getUuid(), guild);
        this.nameToGuild.put(guild.getName(), guild.getUuid());
        Mono.from(this.guildCollection.replaceOne(
                Filters.eq("_id", guild.getUuid().toString()),
                Document.parse(Statics.plainGson().toJson(guild)),
                new ReplaceOptions().upsert(true)
        )).subscribe();
        Universe.get(RedisService.class).publish(new GuildCreatePacket(guild.getUuid()));
    }

    @ApiStatus.Internal
    public void fetchGuild(UUID guildId) {
        Document document = Mono.from(this.guildCollection.find(Filters.eq("_id", guildId.toString())).first()).block();
        if (document == null) {
            return;
        }
        Guild guild = Statics.gson().fromJson(document.toJson(), Guild.class);

        this.guilds.put(guild.getUuid(), guild);
        this.nameToGuild.put(guild.getName(), guild.getUuid());
    }
}
