package studio.talespire.core.profile;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.util.CommandUtil;
import studio.talespire.core.CoreBukkit;
import studio.talespire.core.CorePlugin;
import studio.talespire.core.profile.command.GrantCommand;
import studio.talespire.core.profile.listener.ProfileChatListener;
import studio.talespire.core.profile.listener.ProfileLoadListener;

/**
 * @date 4/25/2024
 * @author Moose1301
 */
public class BukkitProfileService {

    public BukkitProfileService(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(new ProfileChatListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ProfileLoadListener(), plugin);

        CommandUtil.registerAll(new GrantCommand());
    }
}
