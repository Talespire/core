package studio.talespire.core.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @date 4/24/2024
 * @author Moose1301
 */
public interface PlaceholderResolver {
    @Nullable String resolve(Player player, String key);
}
