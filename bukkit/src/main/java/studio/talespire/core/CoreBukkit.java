package studio.talespire.core;

import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.placeholder.PlaceholderService;
import studio.talespire.core.profile.BukkitProfile;
import studio.talespire.core.profile.BukkitProfileService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.rank.BukkitRankService;
import studio.talespire.core.server.BukkitServerProvider;
import studio.talespire.core.server.ServerService;

import java.lang.reflect.Type;
import java.util.UUID;

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
        Universe.get().getRegistry().put(BukkitRankService.class, new BukkitRankService(plugin));
        Universe.get().getRegistry().put(BukkitProfileService.class, new BukkitProfileService(plugin));


    }

    @Override
    public Type getProfileType() {
        return new TypeToken<BukkitProfile>() {}.getType();
    }

    @Override
    public Profile createProfile(UUID playerId, String username) {
        return new BukkitProfile(playerId, username);
    }
}
