package studio.talespire.core;

import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.plugin.java.JavaPlugin;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.config.ConfigService;
import studio.talespire.core.chat.BukkitChatService;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.npc.CitizensNPCService;
import studio.talespire.core.placeholder.PlaceholderService;
import studio.talespire.core.profile.BukkitProfile;
import studio.talespire.core.profile.BukkitProfileService;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.rank.BukkitRankService;
import studio.talespire.core.server.BukkitServerProvider;
import studio.talespire.core.server.ServerService;
import studio.talespire.core.social.global.BukkitGlobalService;
import studio.talespire.core.social.guild.BukkitGuildService;
import studio.talespire.core.tablist.api.TablistService;
import studio.talespire.core.tablist.api.config.TablistConfig;
import studio.talespire.core.tablist.bukkit.TablistBukkitService;
//import studio.talespire.core.tablist.TabListService;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * @date 4/18/2024
 * @author Moose1301
 */
public class CoreBukkit extends Core {
    public static final Component DEFAULT_COMPONENT = Component.text("").style(Style.style().decoration(TextDecoration.ITALIC, false));

    public CoreBukkit(JavaPlugin plugin) {
        super(plugin.getDataFolder().toPath(), plugin.getSLF4JLogger());

        //-- Server
        Universe.get().getRegistry().put(PlaceholderService.class, new PlaceholderService());
        Universe.get(ServerService.class).registerProvider(new BukkitServerProvider());

        //-- Profile
        Universe.get().getRegistry().put(BukkitRankService.class, new BukkitRankService(plugin));
        Universe.get().getRegistry().put(BukkitProfileService.class, new BukkitProfileService(plugin));

        //-- Social
        Universe.get().getRegistry().put(BukkitGlobalService.class, new BukkitGlobalService(plugin));
        Universe.get().getRegistry().put(BukkitGuildService.class, new BukkitGuildService(plugin));

        //-- Chat
        Universe.get().getRegistry().put(BukkitChatService.class, new BukkitChatService(plugin));

        //-- Effects
        Universe.get().getRegistry().put(EffectService.class, new EffectService(plugin));
        Universe.get().getRegistry().put(CitizensNPCService.class, new CitizensNPCService(plugin));

        Universe.get().getRegistry().put(
                TablistConfig.class,
                Universe.get().getRegistry().get(ConfigService.class).loadConfiguration(TablistConfig.class, "tablist", plugin.getDataFolder().toPath())
        );

        Universe.get().getRegistry().put(TablistService.class, new TablistService(plugin));
        Universe.get().getRegistry().put(TablistBukkitService.class, new TablistBukkitService(plugin));
    }

    @Override
    public Type getProfileType() {
        return new TypeToken<BukkitProfile>() {}.getType();
    }

    @Override
    public Profile createProfile(UUID playerId, String username) {
        return new BukkitProfile(playerId, username);
    }
}
