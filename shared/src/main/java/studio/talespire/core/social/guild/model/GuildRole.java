package studio.talespire.core.social.guild.model;

/**
 * @author Moose1301
 * @date 5/4/2024
 */
public enum GuildRole {
    MEMBER, OFFICER, CAPTAIN, LEADER;

    public GuildRole getNext() {
        GuildRole[] ranks = GuildRole.values();

        int currentIndex = this.ordinal();

        int nextIndex = (currentIndex + 1) % ranks.length;

        return ranks[nextIndex];
    }
    public GuildRole getLast() {
        GuildRole[] ranks = GuildRole.values();


        int currentIndex = this.ordinal();

        int nextIndex = (currentIndex - 1) % ranks.length;
        if(nextIndex == -1) {
            nextIndex = 0;
        }
        return ranks[nextIndex];
    }
}
