package studio.talespire.core.profile.menu.impl;

import org.bukkit.entity.Player;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.pagination.PaginatedMenu;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.grant.comparator.GrantDateComparator;
import studio.talespire.core.profile.grant.comparator.GrantRankWeightComparator;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.profile.menu.button.impl.RankGrantButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class RankGrantsMenu extends PaginatedMenu {
    private final Profile profile;
    private final List<GrantRank> grants;

    public RankGrantsMenu(Profile profile) {
        this.profile = profile;
        this.grants = profile.getGrants().stream().filter(grant -> grant instanceof GrantRank).map(grant -> (GrantRank) grant)
                .sorted(new GrantRankWeightComparator().thenComparing(new GrantDateComparator())).toList();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (GrantRank grant : grants) {
            buttons.put(buttons.size(), new RankGrantButton(profile, grant));
        }
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return profile.getUsername() + " Rank Grants";
    }
}
