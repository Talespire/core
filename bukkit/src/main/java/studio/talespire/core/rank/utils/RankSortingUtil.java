package studio.talespire.core.rank.utils;

import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.talespire.core.profile.ProfileService;

import java.util.List;

/**
 * @author Disunion
 * @date 6/15/2024
 */
@NoArgsConstructor
public class RankSortingUtil {
    public List<Player> sortPlayersByRank(List<Player> players) {
        sort(players, 0, players.size() - 1);
        return players;
    }

    void merge(List<Player> players, int leftBound, int middlePoint, int rightBound) {
        ProfileService profileService = Universe.get(ProfileService.class);

        // Find sizes of two sublist to be merged
        int n1 = middlePoint - leftBound + 1;
        int n2 = rightBound - middlePoint;

        // Create temp lists
        List<Player> leftList = players.subList(leftBound, middlePoint + 1);
        List<Player> rightList = players.subList(middlePoint + 1, rightBound + 1);

        // Merge the temp lists
        int i = 0, j = 0;

        // Initial index of merged sublist
        int k = leftBound;
        while (i < n1 && j < n2) {
            if (profileService.getProfile(leftList.get(i).getUniqueId()).getRank().ordinal() <= profileService.getProfile(rightList.get(j).getUniqueId()).getRank().ordinal()) {
                players.set(k, leftList.get(i));
                i++;
            } else {
                players.set(k, rightList.get(j));
                k++;
            }
        }

        while (k < n1) {
            players.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (k < n2) {
            players.set(k, rightList.get(j));
            j++;
            k++;
        }
    }

    void sort(List<Player> players, int leftBound, int rightBound) {
        if (leftBound < rightBound) {
            // Find the middle point
            int middlePoint = (leftBound + rightBound) / 2;

            // Sort first and second halves
            sort(players, leftBound, middlePoint);
            sort(players, middlePoint + 1, rightBound);

            // Merge the sorted halves
            merge(players, leftBound, middlePoint, rightBound);
        }
    }
}
