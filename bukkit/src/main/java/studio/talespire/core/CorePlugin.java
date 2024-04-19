package studio.talespire.core;

import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.annotation.BukkitPlugin;

/**
 * @date 4/18/2024
 * @author Moose1301
 */

@BukkitPlugin(
        name = "Core",
        version = "${git.build.version}-${git.commit.id.abbrev}-${git.branch}",
        description = "Core",
        load = "STARTUP",
        gitReplacements = true,
        apiVersion = "1.13",
        depend = "Universe"
)
public class CorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new CoreBukkit(this);
    }

    @Override
    public void onDisable() {
        Core.getInstance().disable();
    }
}
