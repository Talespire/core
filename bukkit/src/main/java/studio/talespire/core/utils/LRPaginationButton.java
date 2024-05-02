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
public class LRPaginationButton extends Button {

    private final boolean isNext = true;
    private final int pageNumber;

    private final Menu menu;

    @Override
    public ItemStack getItem(Player player) {

        return new ItemBuilder(Material.ARROW)
                .setName(isNext ? ChatColor.GREEN + "Next Page"  : ChatColor.GREEN + "Previous Page")
                .addLoreLine(ChatColor.GRAY + "Go to page " + pageNumber + ".")
                .toItemStack();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.closeInventory();
        Universe.get(MenuHandler.class).openMenuAsync(player, menu);
    }
}
