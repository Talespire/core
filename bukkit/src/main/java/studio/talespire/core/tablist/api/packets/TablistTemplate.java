package studio.talespire.core.tablist.api.packets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@Setter @Getter
@RequiredArgsConstructor
public class TablistTemplate {
    private Component header = Component.empty();
    private Component footer = Component.empty();
    private final PlaceholderCallback placeholderCallback;

    public void setHeader(List<Component> componentList) {
        header = reduce(componentList);
    }

    public void setFooter(List<Component> componentList) {
        footer = reduce(componentList);
    }

    public Component appendFooter(Component component) {
        return footer = footer.append(component);
    }

    public Component appendHeader(Component component) {
        return header = header.append(component);
    }

    public Component replaceHeader(String pattern, Component replacement) {
        String headerString = LegacyComponentSerializer.legacyAmpersand().serialize(header);
        headerString = headerString.replace(pattern, LegacyComponentSerializer.legacyAmpersand().serialize(replacement));
        return header = LegacyComponentSerializer.legacyAmpersand().deserialize(headerString);
    }

    public Component replaceFooter(String pattern, Component replacement) {
        String footerString = LegacyComponentSerializer.legacyAmpersand().serialize(footer);
        footerString = footerString.replace(pattern, LegacyComponentSerializer.legacyAmpersand().serialize(replacement));
        return footer = LegacyComponentSerializer.legacyAmpersand().deserialize(footerString);
    }

    public void replace(String pattern, Component replacement) {
        replaceHeader(pattern, replacement);
        replaceFooter(pattern, replacement);
    }

    public Component reduce(List<Component> componentList) {
        return componentList.stream().reduce(Component.empty(), Component::append);
    }

    public static TablistTemplate fromLists(List<Component> header, List<Component> footer) {
        var template = new TablistTemplate((_a, _b) -> {});
        template.setHeader(header);
        template.setFooter(footer);
        return template;
    }

    public static TablistTemplate empty() {
        return new TablistTemplate((_a, _b) -> {});
    }
}
