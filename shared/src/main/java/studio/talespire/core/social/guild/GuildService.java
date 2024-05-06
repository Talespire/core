package studio.talespire.core.social.guild;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.jetbrains.annotations.ApiStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import studio.lunarlabs.universe.util.Statics;
import studio.talespire.core.Core;
import studio.talespire.core.social.guild.model.Guild;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Moose1301
 * @date 5/4/2024
 */
public class GuildService {
    public static final Pattern NAME_REGEX = Pattern.compile("^[-a-zA-Z0-9]+");

    private final MongoCollection<Document> guildCollection;
    private final Map<UUID, Guild> guilds = new HashMap<>();

    public GuildService() {
        this.guildCollection = Core.getInstance().getDatabase().getCollection("guilds");

        for (Document document : Flux.from(this.guildCollection.find()).toIterable()) {
            Guild guild = Statics.gson().fromJson(document.toJson(), Guild.class);

            this.guilds.put(guild.getUuid(), guild);
        }
    }

    public Guild getGuild(UUID guildId) {
        return this.guilds.get(guildId);
    }

    public void forgetGuild(UUID guildId) {
        this.guilds.remove(guildId);
    }

    public void deleteGuild(Guild guild) {
        this.guilds.remove(guild.getUuid());
        Mono.from(this.guildCollection.deleteOne(
                Filters.eq("_id", guild.getUuid().toString())
        )).subscribe();
    }

    public void registerGuild(Guild guild) {
        this.guilds.put(guild.getUuid(), guild);
        Mono.from(this.guildCollection.replaceOne(
                Filters.eq("_id", guild.getUuid().toString()),
                Document.parse(Statics.plainGson().toJson(guild)),
                new ReplaceOptions().upsert(true)
        )).subscribe();
    }

    @ApiStatus.Internal
    public void fetchGuild(UUID guildId) {
        Document document = Mono.from(this.guildCollection.find(Filters.eq("_id", guildId.toString())).first()).block();
        if(document == null) {
            return;
        }
        Guild guild = Statics.gson().fromJson(document.toJson(), Guild.class);

        this.guilds.put(guild.getUuid(), guild);
    }
}
