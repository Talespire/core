package studio.talespire.core.profile;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.util.CommandUtil;
import studio.talespire.core.profile.command.GrantCommand;
import studio.talespire.core.profile.command.SettingsCommand;
import studio.talespire.core.profile.listener.ProfileChatListener;
import studio.talespire.core.profile.listener.ProfileLoadListener;
import studio.talespire.core.profile.listener.ProfilePacketListener;

/**
 * @date 4/25/2024
 * @author Moose1301
 */
public class BukkitProfileService {

    public BukkitProfileService(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new ProfileChatListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ProfileLoadListener(), plugin);
        Universe.get(RedisService.class).registerListener(new ProfilePacketListener());
        CommandUtil.registerAll(new GrantCommand(), new SettingsCommand());
    }
}
