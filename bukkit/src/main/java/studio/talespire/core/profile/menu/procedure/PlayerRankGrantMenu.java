package studio.talespire.core.profile.menu.procedure;

import org.bukkit.entity.Player;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.Menu;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.menu.button.api.ExitButton;
import studio.talespire.core.profile.menu.button.impl.SelectRankButton;
import studio.talespire.core.rank.Rank;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class PlayerRankGrantMenu extends Menu {
    private final Profile profile;

    public PlayerRankGrantMenu(Profile profile) {
        this.profile = profile;
        this.setBordered(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Which player Rank?";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Rank rank : Rank.values()) {
            if(rank.isStaff() || rank == Rank.DEFAULT) {
                continue;
            }
            buttons.put(buttons.size() + 11, new SelectRankButton(rank, callback -> {
                player.closeInventory();
                new TimeGrantMenu(profile, callback).openAsync(player);
            }));
        }

        buttons.put(getSlot(4, 2), new ExitButton());
        return buttons;
    }
}
