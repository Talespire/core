package studio.talespire.core.tablist.adapter.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.util.Skin;
import studio.talespire.core.tablist.adapter.TabAdapter;
import studio.talespire.core.tablist.setup.TabEntry;

import java.util.List;

/**
 * @author Disunion
 * @date 6/7/2024
 */
public class BaseAdapter implements TabAdapter {
    @Override
    public Component getHeader(Player player) {
        return Component.text("Talespire", NamedTextColor.GOLD);
    }

    @Override
    public Component getFooter(Player player) {
        return Component.text("mc.talespire.net", NamedTextColor.YELLOW);
    }

    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> entries = new ObjectArrayList<>();

        for (int i = 0; i < 4; i++) {
            int ping = player.getPing();
            Skin skin = Skin.getSkinByName(player.getName()).getNow(Skin.DEFAULT_SKIN);
            TabEntry tabEntry = new TabEntry(i, 0, Component.text("Sprinting"), ping, skin);
            entries.add(tabEntry);
        }

        return entries;
    }
}
