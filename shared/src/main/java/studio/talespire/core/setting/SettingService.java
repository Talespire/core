package studio.talespire.core.setting;

import studio.talespire.core.setting.types.messages.PrivateMessageSetting;
import studio.talespire.core.setting.types.sounds.MessageSoundSetting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Moose1301
 * @date 4/30/2024
 */
public class SettingService {
    private final Map<String, Setting<?>> registeredSettings = new HashMap<>();


    public SettingService() {
        registerSetting(new PrivateMessageSetting());
        registerSetting(new MessageSoundSetting());
    }

    private void registerSetting(Setting<?> setting) {
        this.registeredSettings.put(setting.getId(), setting);
        System.out.println("Registered sertting " + setting.getId());
    }
    public Setting<?> getSetting(String id) {
        return this.registeredSettings.get(id);
    }
    public Collection<Setting<?>> getSettings() {
        return this.registeredSettings.values();
    }
}
