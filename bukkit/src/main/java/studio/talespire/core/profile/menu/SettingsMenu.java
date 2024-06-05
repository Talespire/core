package studio.talespire.core.profile.menu;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.menus.api.pagination.PaginatedMenu;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.menu.button.impl.SettingInfoButton;
import studio.talespire.core.profile.menu.button.impl.SettingSwitchButton;
import studio.talespire.core.profile.menu.button.impl.SettingValueButton;
import studio.talespire.core.setting.Setting;
import studio.talespire.core.setting.SettingOption;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/30/2024
 */
@AllArgsConstructor
public class SettingsMenu extends Menu {
    private final Profile profile;

    @Override
    public String getTitle(Player player) {
        return "Settings";
    }
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int index = 0;
        for (Map.Entry<Setting<?>, SettingOption<?>> entry : profile.getSettings().entrySet()) {
            buttons.put(getSlot(index, 1), new SettingInfoButton(entry.getKey()));
            buttons.put(getSlot(index, 2), new SettingValueButton(entry.getValue()));
            buttons.put(getSlot(index, 3), new SettingSwitchButton(profile, entry.getKey(), entry.getValue()));
            index++;
        }
        return buttons;
    }

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }
}
