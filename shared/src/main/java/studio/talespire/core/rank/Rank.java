package studio.talespire.core.rank;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 4/25/2024
 * @author Moose1301
 */
@Getter
public  enum Rank {
    OWNER("Owner", "Owner", NamedTextColor.RED, true),
    ADMIN("Admin", "Admin", NamedTextColor.RED, true),

    MOD("Mod", "Mod", NamedTextColor.DARK_GREEN, true),
    BUILD_TEAM("Build Team", "Build Team", TextColor.color(70, 179, 37), true),


    IMMORTAL("Immortal", "Immortal", NamedTextColor.GOLD),
    DIVINE("Divine", "Divine", NamedTextColor.DARK_BLUE),
    CHAMPION("Champion", "Champion", NamedTextColor.LIGHT_PURPLE),
    HERO("Hero", "Hero", NamedTextColor.AQUA),
    ADVENTURER("Adventurer", "Adventurer", NamedTextColor.GREEN),
    DEFAULT("", "", NamedTextColor.GRAY);

    private final Component prefix;
    private final Component tabPrefix;
    private final TextColor color;
    private final boolean staff;

    private final List<String> permissions = new ArrayList<>();
    private final Map<String, Boolean> compoundedPermissions = new HashMap<>();

    Rank(Component prefix, Component tabPrefix, TextColor color) {
        this(prefix, tabPrefix, color, false);
    }
    Rank(Component prefix, Component tabPrefix, TextColor color, boolean staff) {
        this.prefix = prefix.color(color).decorate(TextDecoration.BOLD);;
        this.tabPrefix = tabPrefix.color(color).decorate(TextDecoration.BOLD);;
        this.color = color;
        this.staff = staff;
    }
    Rank(String prefix, String tabPrefix, TextColor color) {
        this(prefix, tabPrefix, color, false);
    }
    Rank(String prefix, String tabPrefix, TextColor color, boolean staff) {
        this.prefix = Component.text(prefix, color).decorate(TextDecoration.BOLD);
        this.tabPrefix = Component.text(tabPrefix, color).decorate(TextDecoration.BOLD);
        this.color = color;
        this.staff = staff;
    }

    public void loadPermissions() {
        Map<String, Boolean> compoundedPermissions = new HashMap<>();

        for (String permission : this.permissions) {
            boolean negative = permission.charAt(0) == '-';
            if(negative) {
                compoundedPermissions.put(permission.substring(0, 1), false);
            } else {
                compoundedPermissions.put(permission, true);
            }
        }
        for (Rank value : values()) {
            if(value.ordinal() <= ordinal()) {
                continue;
            }
            this.compoundedPermissions.putAll(value.getCompoundedPermissions());
        }
        this.compoundedPermissions.clear();
        this.compoundedPermissions.putAll(compoundedPermissions);
    }

}
