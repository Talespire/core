package studio.talespire.core.rank;

import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import reactor.core.publisher.Flux;
import studio.talespire.core.Core;

import java.net.http.HttpHeaders;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @date 4/28/2024
 * @author Moose1301
 */
public class RankService {
    private final MongoCollection<Document> rankCollection;

    public RankService() {
        this.rankCollection = Core.getInstance().getDatabase().getCollection("rank");



        for(Document document : Flux.from(this.rankCollection.find()).toIterable()) {
            Rank rank = Rank.valueOf(document.getString("_id"));
            List<String> permissions = document.getList("permissions", String.class);
            rank.getPermissions().addAll(permissions);
        }
        Arrays.stream(Rank.values()).sorted((o1, o2) -> Integer.compare(o2.ordinal(), o1.ordinal())).forEach(Rank::loadPermissions);

    }
}
