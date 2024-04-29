package studio.talespire.core.profile.grant.comparator;

import studio.talespire.core.profile.grant.Grant;
import studio.talespire.core.rank.Rank;

import java.util.Comparator;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class GrantDateComparator implements Comparator<Grant> {

    @Override
    public int compare(Grant rank, Grant otherRank) {
        return Long.compare(otherRank.getGrantedAt(), rank.getGrantedAt());
    }
}