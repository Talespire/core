package studio.talespire.core.profile.menu.button;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.UniversePlugin;
import studio.lunarlabs.universe.data.redis.RedisService;
import studio.lunarlabs.universe.menus.api.Button;
import studio.lunarlabs.universe.util.Constants;
import studio.lunarlabs.universe.util.general.Tasks;
import studio.lunarlabs.universe.util.time.TimeUtil;
import studio.talespire.core.profile.Profile;
import studio.talespire.core.profile.ProfileService;
import studio.talespire.core.profile.grant.types.GrantRank;
import studio.talespire.core.profile.menu.conversation.ReasonInputPrompt;
import studio.talespire.core.profile.packet.ProfileGrantPacket;
import studio.talespire.core.profile.utils.BukkitProfileUtils;
import studio.talespire.core.utils.ColorUtils;
import studio.talespire.core.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Moose1301
 * @date 4/28/2024
 */
@AllArgsConstructor
public class RankGrantButton extends Button {
    private Profile profile;
    private GrantRank grant;
    @Override
    public ItemStack getItem(Player player) {
        ItemStack stack = ColorUtils.toLeatherArmor(grant.getRank().getColor(), Material.LEATHER_CHESTPLATE);
        ItemMeta meta = stack.getItemMeta();
        if(grant.isActive()) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
        }
        meta.displayName(Component.text()
                .append(grant.getRank().getPrefix())
                .append(Component.text("(" + grant.getId().toString() + ")", NamedTextColor.DARK_GRAY))
                .build()
        );

        List<Component> lore = new ArrayList<>();
        lore.add(MenuUtils.menuSeparator());
        lore.add(Component.text().append(
                Component.text("Added On: ", NamedTextColor.DARK_GRAY),
                Component.text(TimeUtil.formatDate(grant.getGrantedAt()), NamedTextColor.WHITE)
        ).build());
        lore.add(Component.text().append(
                Component.text("Added By: ", NamedTextColor.DARK_GRAY),
                BukkitProfileUtils.getFormatedName(grant.getGrantedBy())
        ).build());
        lore.add(Component.text().append(
                Component.text("Reason: ", NamedTextColor.DARK_GRAY),
                Component.text(grant.getReason(), NamedTextColor.WHITE)
        ).build());
        lore.add(Component.text().append(
                Component.text("Duration: ", NamedTextColor.DARK_GRAY),
                Component.text(TimeUtil.formatTimeDetailed(grant.getDuration()), NamedTextColor.WHITE)
        ).build());
        if (!grant.isPermanent() && grant.isActive()) {
            lore.add(Component.text().append(
                    Component.text("Remaining: ", NamedTextColor.DARK_GRAY),
                    Component.text(TimeUtil.formatTimeDetailed(grant.getRemaining()), NamedTextColor.WHITE)
            ).build());
        }
        if(grant.isRemoved()) {
            lore.add(Component.text().append(
                    Component.text("Removed On: ", NamedTextColor.DARK_GRAY),
                    Component.text(TimeUtil.formatDate(grant.getRemovedAt()), NamedTextColor.WHITE)
            ).build());
            lore.add(Component.text().append(
                    Component.text("Removed By: ", NamedTextColor.DARK_GRAY),
                    BukkitProfileUtils.getFormatedName(grant.getRemovedBy())
            ).build());
            lore.add(Component.text().append(
                    Component.text("Removed Reason: ", NamedTextColor.DARK_GRAY),
                    Component.text(grant.getRemovedReason(), NamedTextColor.WHITE)
            ).build());
        }
        if (grant.isActive()){
            lore.add(Component.text("Click to remove", NamedTextColor.WHITE));
        }

        meta.lore(lore);

        stack.setItemMeta(meta);
        stack.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        return stack;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if(!grant.isActive()) {
            return;
        }
        player.closeInventory();
        Conversation conversation = new ConversationFactory(UniversePlugin.get())
                .withModality(true)
                .withPrefix(new NullConversationPrefix())
                .withLocalEcho(false)
                .withEscapeSequence("cancel")
                .withTimeout(60)
                .withFirstPrompt(new ReasonInputPrompt(reason -> {
                    Tasks.runAsync(() -> {
                        grant.remove(player.getUniqueId(), System.currentTimeMillis(), reason);
                        Universe.get(RedisService.class).publish(new ProfileGrantPacket(profile.getUuid(), grant));
                        Universe.get(ProfileService.class).saveProfile(profile);
                    });

                }))
                .buildConversation(player);
        player.beginConversation(conversation);

    }
}
