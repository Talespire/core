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
import org.bukkit.inventory.Inventory;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.rank.utils.RankSortingUtil;
import studio.talespire.core.tablist.api.config.TablistConfig;
import studio.talespire.core.tablist.api.packets.PacketSender;
import studio.talespire.core.tablist.api.packets.TablistRemovePlayerPacket;
import studio.talespire.core.tablist.api.packets.fake.FakePlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Disunion
 * @date 6/14/2024
 */
public class BukkitListener implements Listener {
    private final TablistConfig tablistConfig = Universe.get(TablistConfig.class);
    private List<FakePlayer> globalPacketList = new ArrayList<>();

    public BukkitListener() {
        this.reloadChanges();
    }

    public void reloadChanges() {
        // remove old fake players
        this.globalPacketList.forEach(fakePlayer -> Bukkit.getOnlinePlayers().forEach(onlinePlayer -> sendRemovePacket(fakePlayer, onlinePlayer)));
        this.globalPacketList.clear();
        if (tablistConfig.getTablist().isFillWithFakePlayers()) {
            this.globalPacketList = generateFakePlayerList(tablistConfig.getTablist().getFillUntil() - Bukkit.getOnlinePlayers().size());
            globalPacketList.forEach(fakePlayer -> Bukkit.getOnlinePlayers().forEach(player -> sendAddPacket(fakePlayer, player)));
        }
    }

    @EventHandler
    public void onPlayerJoinEventForFakePlayerPurposes(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (tablistConfig.getTablist().isFillWithFakePlayers()) {
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

    @EventHandler
    public void onPlayerLeaveEventForFakePlayerPurposes(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        if (tablistConfig.getTablist().isFillWithFakePlayers()) {
            if (Bukkit.getOnlinePlayers().size() - 1 >= tablistConfig.getTablist().getFillUntil()) return;
            // If its above limit, don't add any fake players

            FakePlayer newFakePlayer = FakePlayer.randomFakePlayer();
            this.globalPacketList.add(newFakePlayer);
            Bukkit.getOnlinePlayers().forEach(player -> sendAddPacket(newFakePlayer, player));
        }
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

    private void updateTablist(Player player) {
        List<Object> playerPackets = new ArrayList<>(39);

        // Get all players sorted by their ranks
        List<Player> playersSorted = new RankSortingUtil().sortPlayersByRank(new ArrayList<>(Bukkit.getOnlinePlayers()));

        // Get the top 37 players
        List<Player> playersToDisplay;
        if (playersSorted.size() > 37) {
            playersToDisplay = playersSorted.subList(0, 37);
        } else {
            playersToDisplay = playersSorted;
        }

        // Make sure that the player is included, at the very least at the bottom, of the tablist
        if (!playersToDisplay.contains(player)) {
            playersToDisplay.set(37, player);
        }

        // Set the 2 first headers of the tablist
        playerPackets.set(0, new FakePlayer("Header1", Component.text("     Players", NamedTextColor.WHITE, TextDecoration.BOLD)));
        playerPackets.set(20, new FakePlayer("Header2", Component.text("     Players", NamedTextColor.WHITE, TextDecoration.BOLD)));

        // Fill the playerPackets list with the players to display
        int playersSortedIndex = 0;

        for (Object o : playerPackets) {
            if (o == null) {
                if (!(playersToDisplay.size() <= playersSortedIndex)) {
                    playerPackets.set(playerPackets.indexOf(o), playersToDisplay.get(playersSortedIndex));
                    playersSortedIndex++;
                } else {
                    playerPackets.set(playerPackets.indexOf(o), FakePlayer.randomFakePlayer());
                }
            }
        }

        List<Object> toSend = Collections.singletonList(globalPacketList);
        for (int i = 0; i < 40; i++) {
            toSend.set(i, playerPackets.get(i));
        }

        for (Object o : toSend) {
            if (o instanceof Player) {

            }
        }
    }
}
