package studio.talespire.core.profile.menu.button.api;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.util.ItemBuilder;

public class ExitButton extends Button {

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.BARRIER)
                .setName(ChatColor.translateAlternateColorCodes('&', "&c&lExit"))
                .addLoreLine(ChatColor.GRAY + "Click to exit the menu.")
                .toItemStack();
    }

    @Override
    public void clicked (Player player, ClickType clickType) {
        player.closeInventory();
    }
}
