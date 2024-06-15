package studio.talespire.core.tablist.api.listeners;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.tablist.api.config.TablistConfig;
import studio.talespire.core.tablist.api.packets.PacketSender;
import studio.talespire.core.tablist.api.packets.TablistRemovePlayerPacket;
import studio.talespire.core.tablist.api.packets.fake.FakePlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class BukkitListener implements Listener {
    @Getter private static final Deque<Player> worldChanges = new ConcurrentLinkedDeque<>();
    private final TablistConfig tablistConfig = Universe.get(TablistConfig.class);
    private final HashMap<World, List<FakePlayer>> worldPacketMap = new HashMap<>();
    private List<FakePlayer> globalPacketList = new ArrayList<>();

    public BukkitListener() {
        this.reloadChanges();
    }

    public void reloadChanges() {
        // remove old fake players
        this.globalPacketList.forEach(fakePlayer -> Bukkit.getOnlinePlayers().forEach(onlinePlayer -> sendRemovePacket(fakePlayer, onlinePlayer)));
        this.worldPacketMap.forEach((world, fakePlayers) -> fakePlayers.forEach(fakePlayer -> world.getPlayers().forEach(player -> sendRemovePacket(fakePlayer, player))));
        this.globalPacketList.clear();
        this.worldPacketMap.clear();
        if(tablistConfig.getTablist().isFillWithFakePlayers()) {
            if (tablistConfig.getTablist().isTablistPerWorld()) {
                Bukkit.getWorlds().forEach(world -> {
                    worldPacketMap.put(world, generateFakePlayerList(tablistConfig.getTablist().getFillUntil() - world.getPlayers().size()));
                    worldPacketMap.get(world).forEach(fakePlayer -> world.getPlayers().forEach(player -> sendAddPacket(fakePlayer, player)));
                });
            } else {
                this.globalPacketList = generateFakePlayerList(tablistConfig.getTablist().getFillUntil() - Bukkit.getOnlinePlayers().size());
                globalPacketList.forEach(fakePlayer -> Bukkit.getOnlinePlayers().forEach(player -> sendAddPacket(fakePlayer, player)));
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (tablistConfig.getTablist().isFillWithFakePlayers() && tablistConfig.getTablist().isTablistPerWorld()) {
            this.worldPacketMap.put(event.getWorld(), generateFakePlayerList(tablistConfig.getTablist().getFillUntil()));
        }
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        if (tablistConfig.getTablist().isFillWithFakePlayers() && tablistConfig.getTablist().isTablistPerWorld()) {
            this.worldPacketMap.remove(event.getWorld());
        }
    }

    @EventHandler
    public void onPlayerJoinEventForFakePlayerPurposes(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        World world = p.getWorld();

        if (tablistConfig.getTablist().isFillWithFakePlayers()) {
            if (tablistConfig.getTablist().isTablistPerWorld()) {
                List<FakePlayer> fakePlayers = this.worldPacketMap.get(world);

                if (fakePlayers.isEmpty()) return; // No left fake players to show

                FakePlayer removedFakePlayer = fakePlayers.remove(0);

                world.getPlayers().forEach(player -> {
                    sendRemovePacket(removedFakePlayer, player);
                });

                fakePlayers.forEach(fakePlayer ->{
                    sendAddPacket(fakePlayer, p);
                });
            } else {
                if (this.globalPacketList.isEmpty()) return; // No left fake players to show
                FakePlayer removedFakePlayer = this.globalPacketList.remove(0);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    sendRemovePacket(removedFakePlayer, player);
                });
                this.globalPacketList.forEach(fakePlayer -> {
                    sendAddPacket(fakePlayer, p);
                });
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveEventForFakePlayerPurposes(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        World world = p.getWorld();

        if (tablistConfig.getTablist().isFillWithFakePlayers()) {
            if (tablistConfig.getTablist().isTablistPerWorld()) {
                List<FakePlayer> fakePlayers = this.worldPacketMap.get(world);

                if (world.getPlayers().size() - 1 >= tablistConfig.getTablist().getFillUntil()) return; // If its above limit, don't add any fake players

                FakePlayer newFakePlayer = FakePlayer.randomFakePlayer();
                fakePlayers.add(newFakePlayer);
                world.getPlayers().forEach(player -> sendAddPacket(newFakePlayer, player));
            } else {
                if (Bukkit.getOnlinePlayers().size() - 1 >= tablistConfig.getTablist().getFillUntil()) return;
                // If its above limit, don't add any fake players
                FakePlayer newFakePlayer = FakePlayer.randomFakePlayer();
                this.globalPacketList.add(newFakePlayer);
                Bukkit.getOnlinePlayers().forEach(player -> sendAddPacket(newFakePlayer, player));
            }
        }
    }

    @EventHandler
    public void onPlayerChangeWorldEventForFakePlayerPurposes(PlayerChangedWorldEvent event) {
        Player eventPlayer = event.getPlayer();
        World fromWorld = event.getFrom();
        World toWorld = eventPlayer.getWorld();

        if (tablistConfig.getTablist().isTablistPerWorld() && tablistConfig.getTablist().isFillWithFakePlayers()) {
            List<FakePlayer> oldWorldFakePlayers = this.worldPacketMap.get(fromWorld);
            List<FakePlayer> newWorldFakePlayers = this.worldPacketMap.get(toWorld);

            if (!newWorldFakePlayers.isEmpty()) {
                FakePlayer removedFakePlayer = newWorldFakePlayers.remove(0);
                toWorld.getPlayers().forEach(player -> sendRemovePacket(removedFakePlayer, player));
                newWorldFakePlayers.forEach(fakePlayer -> sendAddPacket(fakePlayer, eventPlayer));
            }

            // If under limit, add fake players
            if (fromWorld.getPlayers().size() < tablistConfig.getTablist().getFillUntil()) {
                // create new fake player for old world, put it in the list, and send packet to players from that world
                FakePlayer addedFakePlayer = FakePlayer.randomFakePlayer();
                oldWorldFakePlayers.add(addedFakePlayer);
                fromWorld.getPlayers().forEach(player -> sendAddPacket(addedFakePlayer, player));
            }

            // remove FakePlayers of old world to event player
            oldWorldFakePlayers.forEach(fakePlayer -> sendRemovePacket(fakePlayer, eventPlayer));
        }
    }

    @EventHandler
    public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent event) {
        if (!tablistConfig.getTablist().isTablistPerWorld()) {
            return;
        }

        Player eventPlayer = event.getPlayer();
        World fromWorld = event.getFrom();
        World toWorld = eventPlayer.getWorld();
        PacketSender tablistRemovePacket = new TablistRemovePlayerPacket(eventPlayer);

        // Remove the player from the old world's player's tablists
        List<Player> fromWorldPlayers = fromWorld.getPlayers();
        fromWorldPlayers.forEach(player -> {
            try {
                tablistRemovePacket.sendPacketOnce(player);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

       //TODO: Finish this logic if needed.
        /**
         * if (!this.configLoader.isTablistPerWorld()) {
         *             return;
         *         }
         *         Player evtPlayer = evt.getPlayer();
         *         World oldWorld = evt.getFrom();
         *         World newWorld = evtPlayer.getWorld();
         *         PacketSender tablistRemovePacket = new TablistRemovePlayerPacket(evtPlayer);
         *         // Remove player to old world players
         *         List<Player> oldWorldPlayers = oldWorld.getPlayers();
         *         oldWorldPlayers.forEach(tablistRemovePacket::sendPacketOnce);
         *         //Remove all players from old world to event player
         *         new TablistRemovePlayerPacket(oldWorldPlayers).sendPacketOnce(evtPlayer);
         *         PacketSender tablistAddPacket = new TablistAddPlayerPacket(evtPlayer);
         *         PacketSender tablistAddPacket2 = new TablistAddPlayerPacket(newWorld.getPlayers());
         *         newWorld.getPlayers().forEach(tablistAddPacket::sendPacketOnce);
         *         tablistAddPacket2.sendPacketOnce(evtPlayer);
         */
    }

    private static List<FakePlayer> generateFakePlayerList(int size) {
        List<FakePlayer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(FakePlayer.randomFakePlayer());
        }
        return list;
    }

    private void sendRemovePacket(FakePlayer fakePlayer, Player player) {
        try {
            fakePlayer.getTablistRemovePacket().sendPacketOnce(player);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendAddPacket(FakePlayer fakePlayer, Player player) {
        try {
            fakePlayer.getTablistAddPacket().sendPacketOnce(player);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
