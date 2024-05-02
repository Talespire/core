package studio.talespire.core.profile.menu.button.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.talespire.core.CoreBukkit;
import studio.talespire.core.setting.Setting;

/**
 * @author Moose1301
 * @date 5/1/2024
 */
public class SettingInfoButton extends Button {
    private final Setting<?> setting;

    public SettingInfoButton(Setting<?> setting) {
        this.setting = setting;
    }

    @Override
    public ItemStack getItem(Player player) {
        ItemStack stack = new ItemStack(Material.WRITABLE_BOOK);

        ItemMeta meta = stack.getItemMeta();
        meta.displayName(CoreBukkit.DEFAULT_COMPONENT.append(setting.getName()));
        stack.setItemMeta(meta);
        return stack;
    }
}
