package studio.talespire.core.tablist.setup;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.util.Skin;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.tablist.TabListService;
import studio.talespire.core.tablist.adapter.TabAdapter;
import studio.talespire.core.tablist.util.PacketUtils;
import studio.talespire.core.tablist.util.TabStringUtils;
import studio.talespire.core.util.StringUtils;

import java.util.*;

/**
 * @author Disunion
 * @date 6/7/2024
 */

@Log4j2
public class TabLayout {

    public static Component[] TAB_NAMES = new Component[80];
    private final Map<Integer, TabEntryInfo> entries = new Int2ObjectArrayMap<>();

    @Getter private final int mod;

    @Getter private final int maxEntries;

    @Getter private final Player player;

    private boolean isFirstJoin = true;

    @Getter private final Scoreboard scoreboard;

    private Component header, footer;

    public TabLayout(Player player) {
        this.mod = 4;
        this.maxEntries = 80;
        this.player = player;

        if (Universe.get(TabListService.class).isHook() || !(player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard())) {
            this.scoreboard = player.getScoreboard();
        } else {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        player.setScoreboard(this.scoreboard);
    }

    public void create() {
        PacketEventsAPI<?> packetEvents = Universe.get(TabListService.class).getPacketEvents();
        ServerManager manager = packetEvents.getServerManager();

        List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> dataList = new ArrayList<>();
        for (int index = 0; index < 80; index++) {
            int x = index % mod;

            int y = index / mod;
            int i = y * mod + x;

            UserProfile gameProfile = this.generateProfile(i);
            TabEntryInfo info = new TabEntryInfo(gameProfile);
            this.entries.put(i, info);

            dataList.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                    gameProfile,
                    true,
                    0,
                    GameMode.SURVIVAL,
                    AdventureSerializer.fromLegacyFormat(getTeamAt(i)),
                    null
            ));

            WrapperPlayServerPlayerInfoUpdate packetInfo = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER, dataList);
            WrapperPlayServerPlayerInfoUpdate list = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED, dataList);
            WrapperPlayServerPlayerInfoUpdate gamemode = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE, dataList);

            this.sendPacket(packetInfo);
            this.sendPacket(list);
            this.sendPacket(gamemode);
        }
    }

    public void refresh() {
        TabListService service = Universe.get(TabListService.class);
        List<TabEntry> entries = service.getAdapter().getLines(player);
        if (entries.isEmpty()) {
            for (int i = 0; i < 80; i++) {
                this.update(i, Component.empty(), 0, Skin.DEFAULT_SKIN);
            }
            return;
        }

        for (int i = 0; i < 80; i++) {
            TabEntry entry = i < entries.size() ? entries.get(i) : null;

            if (entry == null) {
                this.update(i, Component.empty(), 0, Skin.DEFAULT_SKIN);
                continue;
            }

            final int x = entry.getX();
            final int y = entry.getY();

            final int index = y * mod + x;

            try {
                this.update(index, entry.getText(), entry.getPing(), entry.getSkin());
            } catch (NullPointerException e) {
                log.fatal("[{}] There was an error while updating the tablist for {}", Universe.get(TabListService.class).getPlugin().getName(), player.getName());
                log.error(e);
                e.printStackTrace();
            } catch (Exception e) {
                log.error("[{}] There was an error while updating the tablist for {}", Universe.get(TabListService.class).getPlugin().getName(), player.getName());
                log.error(e);
                e.printStackTrace();
            }
        }

        this.setHeaderAndFooter();

        if (player.getScoreboard() != scoreboard &&  !Universe.get(TabListService.class).isHook()) {
            player.setScoreboard(scoreboard);
        }
    }

    public void cleanup() {
        for (int index = 0; index < 80; index++) {
            String displayName = getTeamAt(index);
            String team = "$" + displayName;

            Team bukkitTeam = player.getScoreboard().getTeam(team);
            if (bukkitTeam != null) {
                bukkitTeam.unregister();
            }

            TabEntryInfo entry = this.entries.get(index);
            if (entry == null) continue;

            UserProfile profile = entry.getProfile();
            PacketWrapper<?> playerInfoRemove = new WrapperPlayServerPlayerInfoRemove(profile.getUUID());
            this.sendPacket(playerInfoRemove);
        }

        if (!Universe.get(TabListService.class).isHook()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    public void setHeaderAndFooter() {
        TabAdapter tablistAdapter = Universe.get(TabListService.class).getAdapter();
        if (tablistAdapter == null) {
            return;
        }

        Component header = tablistAdapter.getHeader(player);
        Component footer = tablistAdapter.getFooter(player);

        if (header.equals(this.header) && footer.equals(this.footer)){
            return;
        }

        this.header = header;
        this.footer = footer;

        WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter = new WrapperPlayServerPlayerListHeaderAndFooter(header, footer);
        this.sendPacket(headerAndFooter);
    }

    public void update(int index, Component text, int ping, Skin skin) {
        String convertedText = LegacyComponentSerializer.legacyAmpersand().serialize(text);
        String[] splitString = TabStringUtils.split(convertedText);

        String prefix = splitString[0];
        String suffix = splitString[1];

        String displayName = getTeamAt(index);
        String team = "$" + displayName;

        if (team.length() > 16 || displayName.length() > 16) {
            log.error("Team name is too long! Team: " + team + " Display Name: " + displayName);
            return;
        }

        if (prefix.length() > 16 || suffix.length() > 16) {
            log.error("Prefix or suffix is too long! Prefix: " + prefix + " Suffix: " + suffix);
            return;
        }

        TabEntryInfo entry = this.entries.get(index);
        if (entry == null) return;

        Component prefixComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(prefix);
        Component suffixComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(suffix);

        boolean changed = false;
        if (!prefixComponent.equals(entry.getPrefix())) {
            entry.setPrefix(prefixComponent);
            changed = true;
        }

        if (!prefixComponent.equals(entry.getSuffix())) {
            entry.setSuffix(suffixComponent);
            changed = true;
        }

        boolean updated = this.updateSkin(entry, skin, text);
        this.updatePing(entry, ping);

        if (!updated && (changed || this.isFirstJoin)) {
            updateDisplayName(entry, convertedText.isEmpty() ? LegacyComponentSerializer.legacyAmpersand().deserialize(getTeamAt(index)) : text);

            if (this.isFirstJoin) {
                this.isFirstJoin = false;
            }
        }

    }

    private void updatePing(TabEntryInfo info, int ping) {
        PacketEventsAPI<?> packetEvents = Universe.get(TabListService.class).getPacketEvents();

        int lastConection = info.getPing();
        if (ping == lastConection) {
            return;
        }
        info.setPing(ping);

        UserProfile gameProfile = info.getProfile();
        PacketWrapper<?> playerInfo = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LATENCY,
                new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile, true, ping, GameMode.SURVIVAL, null, null));

        this.sendPacket(playerInfo);
    }

    private boolean updateSkin(TabEntryInfo info, Skin skin, Component text) {
        PacketEventsAPI<?> packetEvents = Universe.get(TabListService.class).getPacketEvents();
        ServerManager manager = packetEvents.getServerManager();

        if (skin == null) {
            skin = Skin.DEFAULT_SKIN;
        }

        Skin lastSkin = info.getSkin();
        if (skin.equals(lastSkin)) {
            return false;
        }

        info.setSkin(skin);

        UserProfile userProfile = info.getProfile();
        TextureProperty textureProperty = new TextureProperty("textures", skin.getValue(), skin.getSignature());
        userProfile.setTextureProperties(Collections.singletonList(textureProperty));

        int ping = info.getPing();

        PacketWrapper<?> playerInfoRemove = new WrapperPlayServerPlayerInfoRemove(userProfile.getUUID());
        WrapperPlayServerPlayerInfoUpdate.PlayerInfo data = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                userProfile,
                true,
                ping,
                GameMode.SURVIVAL,
                text,
                null
        );

        WrapperPlayServerPlayerInfoUpdate add = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER, data);
        WrapperPlayServerPlayerInfoUpdate list = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED, data);

        this.sendPacket(playerInfoRemove);

        return true;
    }

    private void sendPacket(PacketWrapper<?> packetWrapper) {
        if (player == null) return;

        PacketUtils.sendPacket(player, packetWrapper);
    }

    private UserProfile generateProfile(int index) {
        Skin defaultSkin = Skin.DEFAULT_SKIN;

        UserProfile gameProfile = new UserProfile(UUID.randomUUID(), getTeamAt(index));
        TextureProperty textureProperty = new TextureProperty("textures", defaultSkin.getValue(), defaultSkin.getSignature());
        gameProfile.setTextureProperties(Collections.singletonList(textureProperty));

        return gameProfile;
    }

    private void updateDisplayName(TabEntryInfo entry, Component text) {
        PacketEventsAPI<?> packetEvents = PacketEvents.getAPI();
        ServerManager manager = packetEvents.getServerManager();

        UserProfile profile = entry.getProfile();
        PacketWrapper<?> display;

        WrapperPlayServerPlayerInfoUpdate.PlayerInfo data = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                profile,
                true,
                0,
                null,
                text,
                null
        );

        display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, data);
        this.sendPacket(display);
    }

    public String getTeamAt(int index) {

        Component name = TAB_NAMES[index];

        return LegacyComponentSerializer.legacyAmpersand().serialize(name);
    }

    static {
        for (int i = 0; i < TAB_NAMES.length; i++) {
            int x = i % 4;
            int y = i / 4;
            String name = "§0§" + x
                    + (y > 9 ? "§" + String.valueOf(y).toCharArray()[0]
                    + "§" + String.valueOf(y).toCharArray()[1] :
                    "§0§" + String.valueOf(y).toCharArray()[0])
                    + ChatColor.RESET;
            Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(name);
            TAB_NAMES[i] = component;
        }
    }

}
