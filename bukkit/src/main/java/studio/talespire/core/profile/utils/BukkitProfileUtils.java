package studio.talespire.core.profile.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.uuid.UUIDCache;
import studio.lunarlabs.universe.uuid.UUIDCacheService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.rank.Rank;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
public class BukkitProfileUtils {
    public static void updatePlayerDisplay(Player player, Profile profile) {
        player.playerListName(Component.text()
                .append(Component.text("["+profile.getGuild().getTag()+"]", NamedTextColor.nearestTo(profile.getGuild().getColor())))
                .append(Component.space())
                .append(profile.getRank().getTabPrefix())
                .append(profile.getRank() == Rank.DEFAULT ? Component.empty() : Component.space())
                .append(profile.getFormattedName())
                .build()
        );
    }
    public static Component getFormatedName(UUID playerId) {
        Profile profile = Universe.get(ProfileService.class).getProfile(playerId);
        if(profile != null) {
            return profile.getFormattedName();
        }
        String displayName = null;
        try {
            displayName = Universe.get(UUIDCache.class).getName(playerId).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            displayName = playerId.toString();
        }
        if(displayName == null) {
            displayName = playerId.toString();
        }
        return Component.text(displayName, NamedTextColor.WHITE);
    }
}
