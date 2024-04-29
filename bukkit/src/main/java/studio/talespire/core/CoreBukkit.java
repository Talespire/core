package studio.talespire.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.placeholder.PlaceholderService;
import studio.talespire.core.profile.BukkitProfileService;
import studio.talespire.core.server.BukkitServerProvider;
import studio.talespire.core.server.ServerService;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
public class CoreBukkit extends Core {
    public static final Component DEFAULT_COMPONENT = Component.text("").style(Style.style().decoration(TextDecoration.ITALIC, false));
    public CoreBukkit(JavaPlugin plugin) {
        super(plugin.getDataFolder().toPath());


        Universe.get().getRegistry().put(PlaceholderService.class, new PlaceholderService());
        Universe.get(ServerService.class).registerProvider(new BukkitServerProvider());
        Universe.get().getRegistry().put(BukkitProfileService.class, new BukkitProfileService(plugin));

    }
}
