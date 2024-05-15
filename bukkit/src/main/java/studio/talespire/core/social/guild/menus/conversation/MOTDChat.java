package studio.talespire.core.social.guild.menus.conversation;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NullConversationPrefix;
import org.bukkit.entity.Player;
import studio.lunarlabs.universe.UniversePlugin;
import studio.talespire.core.social.guild.menus.conversation.MOTDInputPrompt;
import studio.talespire.core.social.guild.model.Guild;
import studio.talespire.core.utils.BukkitUtils;
import studio.talespire.core.utils.MenuUtils;

@RequiredArgsConstructor
public class MOTDChat {
    private final Guild guild;
    private final Player player;

    public void displayCurrentMOTD() {


        Component component = Component.text()
            .append(Component.newline())
            .append(Component.text("Guild MOTD", NamedTextColor.DARK_GREEN))
            .append(Component.newline())
            .build();

        for (String motd : guild.getMotd()) {
            component = component.appendNewline().append(Component.text("[+] - ", NamedTextColor.YELLOW)
                    .hoverEvent(HoverEvent.showText(Component.text("Click to edit.", NamedTextColor.GRAY)))
                    .clickEvent(ClickEvent.callback((player) -> {
                        Conversation conversation = new ConversationFactory(UniversePlugin.get())
                                .withFirstPrompt(new MOTDInputPrompt((input) -> {
                                    if (input.equalsIgnoreCase("delete")) {
                                        guild.getMotd().remove(motd);
                                        displayCurrentMOTD();
                                    } else if (input.equalsIgnoreCase("cancel")) {
                                        displayCurrentMOTD();
                                    } else {
                                        guild.getMotd().set(guild.getMotd().indexOf(motd), input);
                                        displayCurrentMOTD();
                                    }
                                }))
                                .withLocalEcho(false)
                                .withPrefix(new NullConversationPrefix())
                                .buildConversation((Player) player);
                        conversation.begin();
                    })));
            component = component.append(Component.text(motd));
        }

        int remainingLines = 5 - guild.getMotd().size();
        for (int i = 0; i < remainingLines; i++) {
            component = component.appendNewline().append(Component.text("[+]", NamedTextColor.GREEN)
                .hoverEvent(HoverEvent.showText(Component.text("Click to add.", NamedTextColor.GRAY)))
                .clickEvent(ClickEvent.callback((player) -> {
                    Conversation conversation = new ConversationFactory(UniversePlugin.get())
                        .withFirstPrompt(new MOTDInputPrompt((input) -> {
                            if (!input.equalsIgnoreCase("cancel")) {
                                guild.getMotd().add(input);
                                displayCurrentMOTD();
                            } else {
                                displayCurrentMOTD();
                            }
                        }))
                        .withLocalEcho(false)
                        .withPrefix(new NullConversationPrefix())
                        .buildConversation((Player) player);
                    conversation.begin();
                })));
        }

        Component toSend = Component.text()
            .append(MenuUtils.chatSeparator(NamedTextColor.AQUA))
            .append(component)
            .appendNewline().append(MenuUtils.chatSeparator(NamedTextColor.AQUA))
            .build();
        player.sendMessage(toSend);
    }
}