package studio.talespire.core.tablist;

import com.github.retrooper.packetevents.PacketEventsAPI;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import studio.talespire.core.tablist.adapter.TabAdapter;
import studio.talespire.core.tablist.adapter.impl.BaseAdapter;
import studio.talespire.core.tablist.listener.TabListener;
import studio.talespire.core.tablist.listener.TeamsPacketListener;
import studio.talespire.core.tablist.setup.TabLayout;
import studio.talespire.core.tablist.thread.TablistThread;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Disunion
 * @date 6/6/2024
 */

@Getter
@Log4j2
public class TabListService {

    private final Map<UUID, TabLayout> layoutMap = new ConcurrentHashMap<>();
    private final JavaPlugin plugin;
    private TabAdapter adapter;
    private TabListener listener;

    private TablistThread thread;
    private PacketEventsAPI<?> packetEvents;
    private TeamsPacketListener teamsPacketListener;
    private final boolean debug;
    @Setter private boolean hook;
    private long ticks = 20L;

    public TabListService(JavaPlugin plugin, PacketEventsAPI<?> packetEventsAPI) {
        this.plugin = plugin;
        this.debug = Boolean.getBoolean("BDebug");

        init(packetEventsAPI, new TeamsPacketListener());
        registerAdapter(new BaseAdapter(), 20L);
    }

    public void init(PacketEventsAPI<?> packetEventsAPI, TeamsPacketListener listener) {
        this.packetEvents = packetEventsAPI;
        this.adapter = new BaseAdapter();
        this.listener = new TabListener(this);

        this.teamsPacketListener = listener;
        this.packetEvents.getEventManager().registerListener(listener);
        Bukkit.getPluginManager().registerEvents(this.listener, plugin);
    }

    public void registerAdapter(TabAdapter tabAdapter, long ticks) {
        this.adapter = tabAdapter == null ? new BaseAdapter() : tabAdapter;

        if (ticks < 20) {
            log.warn("The tick rate is too low, setting it to 20");
            this.ticks = 20L;
        } else {
            this.ticks = ticks;
        }

        if (Bukkit.getMaxPlayers() < 60) {
            log.warn("The server is not optimized for a large tablist, it is recommended to have at least 60 slots");
        }

        this.thread = new TablistThread();
        this.thread.start();
    }

    public void unload() {
        if (this.listener != null) {
            HandlerList.unregisterAll(this.listener);
            this.listener = null;
        }

        for (Map.Entry<UUID, TabLayout> entry : this.layoutMap.entrySet()) {
            UUID uuid = entry.getKey();
            entry.getValue().cleanup();

            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) continue;

            Team team = player.getScoreboard().getTeam("ztab");
            if (team != null) team.unregister();

            this.layoutMap.remove(uuid);
        }

        this.thread.terminate();
        this.thread.interrupt();
        this.thread = null;
    }


}
