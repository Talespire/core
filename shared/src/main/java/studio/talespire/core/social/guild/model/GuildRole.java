package studio.talespire.core.social.guild.model;

/**
 * @author Moose1301
 * @date 5/4/2024
 */
public enum GuildRole {
    LEADER,
    CAPTAIN,
    OFFICER,
    MEMBER;


    public GuildRole getNext() {
        GuildRole[] ranks = GuildRole.values();

        int currentIndex = this.ordinal();

        int nextIndex = (currentIndex - 1) % ranks.length;
        if(nextIndex == -1) {
            nextIndex = 3;
        }
        return ranks[nextIndex];
    }
    public GuildRole getLast() {
        GuildRole[] ranks = GuildRole.values();

        int currentIndex = this.ordinal();

        int nextIndex = (currentIndex + 1) % ranks.length;

        return ranks[nextIndex];
    }
}
