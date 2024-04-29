package studio.talespire.core.server.menu.button;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.systemtype.SystemType;
import studio.talespire.core.CoreBukkit;
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
        meta.displayName(CoreBukkit.DEFAULT_COMPONENT.append(Component.text(server.getDisplayName())));
        List<Component> lore = new ArrayList<>();
        lore.add(CoreBukkit.DEFAULT_COMPONENT
                .append(Component.text("Server Id", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.GRAY))
                .append(Component.text(server.getServerId(), NamedTextColor.WHITE))
        );
        lore.add(CoreBukkit.DEFAULT_COMPONENT
                .append(Component.text("Group", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.GRAY))
                .append(Component.text(server.getGroup(), NamedTextColor.WHITE))
        );
        lore.add(CoreBukkit.DEFAULT_COMPONENT
                .append(Component.text("Region", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.GRAY))
                .append(Component.text(server.getRegion(), NamedTextColor.WHITE))
        );
        if (server.getPlatform() == SystemType.BUKKIT) {
            lore.add(MenuUtils.separator(22).append(Component.text("Bukkit", NamedTextColor.GOLD).append(MenuUtils.separator(22))));

            lore.add(CoreBukkit.DEFAULT_COMPONENT
                    .append(Component.text("IP", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.GRAY))
                    .append(Component.text(server.getMetadataValue("bukkit:ip", String.class), NamedTextColor.WHITE))
            );
            lore.add(CoreBukkit.DEFAULT_COMPONENT
                    .append(Component.text("Port", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.GRAY))
                    .append(Component.text((int) (double)server.getMetadataValue("bukkit:port", double.class), NamedTextColor.WHITE))
            );
            lore.add(CoreBukkit.DEFAULT_COMPONENT
                    .append(Component.text("Online", NamedTextColor.WHITE)).append(Component.text(": ", NamedTextColor.GRAY))
                    .append(Component.text((int) (double)server.getMetadataValue("bukkit:online", double.class), NamedTextColor.WHITE))
                    .append(Component.text("/", NamedTextColor.WHITE))
                    .append(Component.text((int) (double)server.getMetadataValue("bukkit:maxplayers", Double.class), NamedTextColor.WHITE))
            );
        }
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}
