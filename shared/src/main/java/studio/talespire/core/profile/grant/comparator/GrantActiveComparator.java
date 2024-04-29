package studio.talespire.core.profile.grant.comparator;

import studio.talespire.core.profile.grant.Grant;

import java.util.Comparator;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class GrantActiveComparator implements Comparator<Grant> {

    @Override
    public int compare(Grant rank, Grant otherRank) {
        return Boolean.compare(otherRank.isActive(), rank.isActive());
    }
}