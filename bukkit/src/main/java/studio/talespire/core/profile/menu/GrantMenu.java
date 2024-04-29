package studio.talespire.core.profile.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.lunarlabs.universe.util.ItemBuilder;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.menu.ranks.RankGrantMenu;

import java.util.Map;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class GrantMenu extends Menu {
    private final Profile profile;

    public GrantMenu(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + "Granting " + profile.getUsername();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Map.of(
                3, new PermissionGrantButton(),
                5, new RankGrantButton()
        );
    }

    private class PermissionGrantButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PAPER).setName(ChatColor.WHITE + "Permission Grant").toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();

        }
    }

    private class RankGrantButton extends Button {

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.EMERALD_BLOCK).setName(ChatColor.WHITE + "Rank Grant").toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            new RankGrantMenu(profile).openAsync(player);
        }
    }
}
