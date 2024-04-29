package studio.talespire.core.profile.menu.list;

import org.bukkit.entity.Player;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.menus.api.pagination.PaginatedMenu;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.grant.comparator.GrantDateComparator;
import studio.talespire.core.profile.grant.comparator.GrantRankWeightComparator;
import studio.talespire.core.profile.grant.types.GrantPermission;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.profile.menu.button.PermissionGrantButton;
import studio.talespire.core.profile.menu.button.RankGrantButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class PermissionGrantsMenu extends PaginatedMenu {
    private final Profile profile;
    private final List<GrantPermission> grants;

    public PermissionGrantsMenu(Profile profile) {
        this.profile = profile;
        this.grants = profile.getGrants().stream().map(grant -> (GrantPermission) grant)
                .sorted(new GrantDateComparator()).toList();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (GrantPermission grant : grants) {
            buttons.put(buttons.size(), new PermissionGrantButton(grant));
        }
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return profile.getUsername() + " Permission Grants";
    }
}
