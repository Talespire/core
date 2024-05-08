package studio.talespire.core.setting;

import studio.talespire.core.setting.types.privacy.GuildInviteSetting;
import studio.talespire.core.setting.types.privacy.PrivateMessageSetting;
import studio.talespire.core.setting.types.sounds.MessageSoundSetting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/30/2024
 */
public class SettingService {
    private final Map<String, Setting<?>> registeredSettings = new HashMap<>();
    private final Map<Class<?>, String> classToName = new HashMap<>();

    public SettingService() {
        registerSetting(new PrivateMessageSetting());
        registerSetting(new MessageSoundSetting());
        registerSetting(new GuildInviteSetting());
    }

    private void registerSetting(Setting<?> setting) {
        this.registeredSettings.put(setting.getId(), setting);
        this.classToName.put(setting.getClass(), setting.getId());
    }
    public Setting<?> getSetting(String id) {
        return this.registeredSettings.get(id);
    }
    public Setting<?> getSetting(Class<? extends Setting<?>> setting) {
        return this.registeredSettings.get(this.classToName.get(setting));
    }
    public Collection<Setting<?>> getSettings() {
        return this.registeredSettings.values();
    }
}
