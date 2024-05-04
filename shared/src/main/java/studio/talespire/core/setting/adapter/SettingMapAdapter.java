package studio.talespire.core.setting.adapter;

import com.google.gson.*;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.util.logging.Debugger;
import studio.talespire.core.Core;
import studio.talespire.core.setting.Setting;
import studio.talespire.core.setting.SettingService;
import studio.talespire.core.setting.SettingOption;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Moose1301
 * @date 4/30/2024
 */
public class SettingMapAdapter implements JsonSerializer<ConcurrentHashMap<Setting<?>, SettingOption<?>>>, JsonDeserializer<ConcurrentHashMap<Setting<?>, SettingOption<?>>> {

    @Override
    public JsonElement serialize(ConcurrentHashMap<Setting<?>, SettingOption<?>> map, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for (Map.Entry<Setting<?>, SettingOption<?>> entry : map.entrySet()) {
            Setting<?> key = entry.getKey();
            SettingOption<?> value = entry.getValue();
            json.add(key.getId(), context.serialize(value.value()));
        }
        return json;
    }

    @Override
    public ConcurrentHashMap<Setting<?>, SettingOption<?>> deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        ConcurrentHashMap<Setting<?>, SettingOption<?>> map = new ConcurrentHashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            try {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                Setting<?> setting = Universe.get(SettingService.class).getSetting(key);
                if (setting == null) {
                    System.out.println("Faileed to get setting " + key);
                    continue;
                }

                SettingOption<?> settingValue = setting.deserialize(value);
                map.put(setting, settingValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}