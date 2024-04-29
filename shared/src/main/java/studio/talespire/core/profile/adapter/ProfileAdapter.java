package studio.talespire.core.profile.adapter;

import com.google.gson.*;
import studio.talespire.core.Core;
import studio.talespire.core.profile.Profile;

import java.lang.reflect.Type;

public class ProfileAdapter implements JsonSerializer<Profile>, JsonDeserializer<Profile> {

    @Override
    public JsonElement serialize(Profile profile, Type type, JsonSerializationContext context) {
        return context.serialize(profile);
    }

    @Override
    public Profile deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, Core.getInstance().getProfileType());
    }

}