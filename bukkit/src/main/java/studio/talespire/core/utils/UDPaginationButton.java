package studio.talespire.core.utils;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.menus.api.MenuHandler;
import studio.lunarlabs.universe.util.ItemBuilder;

@RequiredArgsConstructor
public class UDPaginationButton extends Button {

    private final boolean isUp = true;
    private final Menu menu;

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .setName(isUp ? ChatColor.GREEN +  "Up" :  ChatColor.GREEN + "Down")
                .addLoreLine(isUp ? ChatColor.GRAY + "Go up a page." : ChatColor.GRAY + "Go down a page.")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();
        Universe.get(MenuHandler.class).openMenuAsync(player, menu);
    }
}
