package studio.talespire.core;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
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
        depend = "Universe",
        author = "Talespire Development Team",
        softDepend = {
                "Citizens"
        }
)
public class CorePlugin extends JavaPlugin {
    @Getter
    private static CorePlugin instance;
    private PacketEventsAPI<?> packetEventsAPI;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));

        this.packetEventsAPI = PacketEvents.getAPI();
        this.packetEventsAPI.getSettings().bStats(false).checkForUpdates(false);

        this.packetEventsAPI.load();
    }

    @Override
    public void onEnable() {
        instance = this;
        new CoreBukkit(this, packetEventsAPI);
    }

    @Override
    public void onDisable() {
        Core.getInstance().disable();
        CoreBukkit.getInstance().disable();
    }
}
