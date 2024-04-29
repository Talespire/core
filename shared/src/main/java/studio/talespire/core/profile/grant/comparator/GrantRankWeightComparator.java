package studio.talespire.core.profile.grant.comparator;

import org.jetbrains.annotations.NotNull;
import studio.talespire.core.rank.Rank;

import java.util.Comparator;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class RankWeightComparator implements Comparator<Rank> {

    @Override
    public int compare(Rank rank, Rank otherRank) {
        return Integer.compare(otherRank.ordinal(), rank.ordinal());
    }
}