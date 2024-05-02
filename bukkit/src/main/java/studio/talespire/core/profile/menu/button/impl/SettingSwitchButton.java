package studio.talespire.core.profile.menu.button.impl;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.talespire.core.setting.Setting;
import studio.talespire.core.setting.SettingOption;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moose1301
 * @date 5/1/2024
 */
@AllArgsConstructor
public class SettingSwitchButton extends Button {
    private Setting<?> setting;
    private SettingOption<?> option;
    @Override
    public ItemStack getItem(Player player) {
        ItemStack stack = new ItemStack(Material.OAK_BUTTON);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(setting.getName().color(NamedTextColor.GREEN));
        List<Component> lores = new ArrayList<>();
        lores.add(setting.getDescription().color(NamedTextColor.GRAY));
        lores.add(Component.empty());
        lores.add(
                Component.text()
                        .append(Component.text("Current: "))
                        .append(option.name())
                        .color(NamedTextColor.GRAY).build()
        );
        lores.add(
                Component.text()
                        .append(Component.text("Next: "))
                        .append(setting.getRawNext(option).name())
                        .color(NamedTextColor.GRAY).build()
        );
        meta.lore(lores);
        stack.setItemMeta(meta);

        return stack;
    }
}
