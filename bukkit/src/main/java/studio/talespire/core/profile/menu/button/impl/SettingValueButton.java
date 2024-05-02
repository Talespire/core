package studio.talespire.core.profile.menu.button.impl;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.talespire.core.CoreBukkit;
import studio.talespire.core.setting.SettingOption;

/**
 * @author Moose1301
 * @date 5/1/2024
 */
@AllArgsConstructor
public class SettingValueButton extends Button {
    private final SettingOption<?> value;


    @Override
    public ItemStack getItem(Player player) {
        ItemStack stack = new ItemStack(Material.PAPER);

        ItemMeta meta = stack.getItemMeta();
        meta.displayName(CoreBukkit.DEFAULT_COMPONENT.append(value.name()));
        stack.setItemMeta(meta);
        return stack;
    }
}
