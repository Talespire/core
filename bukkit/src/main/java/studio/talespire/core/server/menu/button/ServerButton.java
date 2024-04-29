package studio.talespire.core.server.menu.button;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.talespire.core.server.model.Server;
import studio.talespire.core.utils.BukkitUtils;
import studio.talespire.core.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
@AllArgsConstructor
public class ServerButton extends Button {
    private final Server server;
    @Override
    public ItemStack getItem(Player player) {
        ItemStack stack = new ItemStack(BukkitUtils.toMaterial(server.getPlatform()));
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(server.getDisplayName()));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text()
                        .append(Component.text("Server Id", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.DARK_GRAY))
                        .append(Component.text(server.getServerId())).build()
        );
        lore.add(Component.text()
                .append(Component.text("Group", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(Component.text(server.getGroup())).build()
        );
        lore.add(Component.text()
                .append(Component.text("Region", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(Component.text(server.getRegion())).build()
        );
        if (server.getPlatform() == SystemType.BUKKIT){
            lore.add(MenuUtils.separator(22).append(Component.text("Bukkit", NamedTextColor.GOLD).append(MenuUtils.separator(22))));

            lore.add(Component.text()
                    .append(Component.text("IP", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(server.getMetadataValue("bukkit:ip", String.class))).build()
            );
            lore.add(Component.text()
                    .append(Component.text("Port", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(server.getMetadataValue("bukkit:port", String.class))).build()
            );
        }
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}
