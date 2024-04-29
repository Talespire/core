package studio.talespire.core.profile.grant.comparator;

import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.rank.Rank;

import java.util.Comparator;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class GrantRankWeightComparator implements Comparator<GrantRank> {

    @Override
    public int compare(GrantRank rank, GrantRank otherRank) {
        return Integer.compare(otherRank.getRank().ordinal(), rank.getRank().ordinal());
    }
}