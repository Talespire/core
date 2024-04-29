package studio.talespire.core.server.menu;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.pagination.PaginatedMenu;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.talespire.core.server.ServerService;
import studio.talespire.core.server.menu.button.ServerButton;
import studio.talespire.core.server.model.Server;
import studio.talespire.core.utils.BukkitUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class ServerListMenu extends PaginatedMenu {
    private SystemType filteredType;
    {
        setUpdateAfterClick(true);
    }
    @Override
    public String getTitle(Player player) {
        return "Server List";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (Server server : Universe.get(ServerService.class).getServers()) {
            if (filteredType == null) {
                buttons.put(buttons.size(), new ServerButton(server));
                continue;
            }
            if (filteredType == server.getPlatform()) {
                buttons.put(buttons.size(), new ServerButton(server));
                continue;
            }
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (SystemType value : SystemType.values()) {
            buttons.put(2 + (buttons.size() * 2), new SystemTypeButton(value));
        }
        return buttons;
    }

    @AllArgsConstructor
    private class SystemTypeButton extends Button {
        private final SystemType type;

        @Override
        public ItemStack getItem(Player player) {
            ItemStack stack = new ItemStack(BukkitUtils.toMaterial(type));
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Component.text(WordUtils.capitalize(type.name().toLowerCase())));

            stack.setItemMeta(meta);
            return stack;
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            filteredType = type;
        }
    }


}
