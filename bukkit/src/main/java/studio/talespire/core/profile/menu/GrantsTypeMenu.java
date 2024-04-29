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
import studio.talespire.core.profile.menu.button.api.PlayerHeadButton;
import studio.talespire.core.profile.menu.impl.PermissionGrantsMenu;
import studio.talespire.core.profile.menu.impl.RankGrantsMenu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class GrantsTypeMenu extends Menu {
    private final Profile profile;

    public GrantsTypeMenu(Profile profile) {
        this.profile = profile;
        this.setBordered(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Viewing grants for " + profile.getUsername();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(getSlot(2, 1), new PermissionGrantsButton());
        buttons.put(getSlot(4, 1), new PlayerHeadButton(profile.getUsername(), profile.getRank()));
        buttons.put(getSlot(6, 1), new RankGrantsButton());

        return buttons;
    }

    private class PermissionGrantsButton extends Button {
        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .setName(ChatColor.GREEN + "Permission Grants")
                    .addLoreLine(ChatColor.GRAY + "Click to view permission grants for this player.")
                    .toItemStack();
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
            return new ItemBuilder(Material.GREEN_WOOL)
                    .setName(ChatColor.GREEN + "Rank Grants")
                    .addLoreLine(ChatColor.GRAY + "Click to view rank grants for this player.")
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            new RankGrantsMenu(profile).openAsync(player);
        }
    }
}
