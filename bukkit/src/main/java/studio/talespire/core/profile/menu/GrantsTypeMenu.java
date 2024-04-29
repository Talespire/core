package studio.talespire.core.profile.menu;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.menu.impl.PermissionGrantsMenu;
import studio.talespire.core.profile.menu.impl.RankGrantsMenu;

import java.util.Map;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
@AllArgsConstructor
public class GrantsTypeMenu extends Menu {
    private final Profile profile;

    @Override
    public String getTitle(Player player) {
        return "Pick a grant type for " + profile.getUsername();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(
                3, new PermissionGrantsButton(),
                5, new RankGrantsButton()
        );
    }

    private class PermissionGrantsButton extends Button {
        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PAPER).setName(ChatColor.WHITE + "Permission Grants").toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            new PermissionGrantsMenu(profile).openAsync(player);

        }
    }

    private class RankGrantsButton extends Button {
        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PAPER).setName(ChatColor.WHITE + "Rank Grants").toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            new RankGrantsMenu(profile).openAsync(player);
        }
    }
}
