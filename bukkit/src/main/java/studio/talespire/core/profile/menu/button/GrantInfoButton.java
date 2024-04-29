package studio.talespire.core.profile.menu.button;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.util.general.WoolUtil;
import studio.lunarlabs.universe.util.time.TimeUtil;
import studio.talespire.core.rank.Rank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class GrantInfoButton extends Button {
    private String username;
    private @Nullable Rank rank = null;
    private @Nullable String permission = null;
    private @Nullable Long time = null;

    public GrantInfoButton(String username, Rank rank) {
        this.username = username;
        this.rank = rank;
    }

    public GrantInfoButton(String username, String permission) {
        this.username = username;
        this.permission = permission;
    }

    public GrantInfoButton(String username, Rank rank, long time) {
        this(username, rank);
        this.time = time;
    }

    public GrantInfoButton(String username, String permission, long time) {
        this(username, permission);
        this.time = time;
    }

    @Override
    public ItemStack getItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Grant Info", NamedTextColor.WHITE));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text()
                .append(Component.text("User", NamedTextColor.WHITE))
                .append(Component.text(":", NamedTextColor.GRAY))
                .append(Component.space())
                .append(Component.text(username, NamedTextColor.WHITE))
                .build());

        if(rank != null) {
            lore.add(Component.text()
                    .append(Component.text("Rank", NamedTextColor.WHITE))
                    .append(Component.text(":", NamedTextColor.GRAY))
                    .append(Component.space())
                    .append(rank.getPrefix())
                    .build());
        } else if(permission != null) {
            lore.add(Component.text()
                    .append(Component.text("Permission", NamedTextColor.WHITE))
                    .append(Component.text(":", NamedTextColor.GRAY))
                    .append(Component.space())
                    .append(Component.text(permission, NamedTextColor.WHITE))
                    .build());
        }
        if(this.time != null) {
            lore.add(Component.text()
                    .append(Component.text("Time", NamedTextColor.WHITE))
                    .append(Component.text(":", NamedTextColor.GRAY))
                    .append(Component.space())
                    .append(Component.text(TimeUtil.formatTimeDetailed(time), NamedTextColor.WHITE))
                    .build());
        }
        meta.lore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
