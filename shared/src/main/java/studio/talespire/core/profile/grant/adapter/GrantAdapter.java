package studio.talespire.core.profile.grant.adapter;

import com.google.gson.*;
import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.profile.grant.GrantType;
import studio.talespire.core.profile.grant.types.GrantPermission;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.rank.Rank;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * @date 4/28/2024
 * @author Moose1301
 */
public class GrantAdapter implements JsonDeserializer<Grant> {
    @Override
    public Grant deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();
        GrantType grantType = GrantType.valueOf(object.get("type").getAsString());

        UUID grantId = UUID.fromString(object.get("_id").getAsString());
        String reason = object.get("reason").getAsString();
        long duration = object.get("duration").getAsLong();
        UUID grantedBy = UUID.fromString(object.get("grantedBy").getAsString());
        long grantedAt = object.get("grantedAt").getAsLong();
        Long expiresAt = null;
        if(!object.get("expiresAt").isJsonNull()) {
            expiresAt = object.get("expiresAt").getAsLong();
        }

        boolean removed = object.get("removed").getAsBoolean();
        Grant grant = null;
        if(grantType == GrantType.RANK) {
            grant = new GrantRank(
                    grantId, Rank.valueOf(object.get("rank").getAsString()),
                    reason, duration, grantedBy, grantedAt
            );
        } else if(grantType == GrantType.PERMISSION) {
            grant = new GrantPermission(
                    grantId, object.get("permission").getAsString(),
                    reason, duration, grantedBy, grantedAt
            );
        }
        if(removed) {
            grant.setRemoved(true);
            grant.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
            grant.setRemovedAt(object.get("removedAt").getAsLong());
            grant.setRemovedReason(object.get("removedReason").getAsString());
        }

        return grant;
    }


}
