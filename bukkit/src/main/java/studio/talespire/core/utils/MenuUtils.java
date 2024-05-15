package studio.talespire.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.w3c.dom.Text;

import java.awt.*;

/**
 * @author Moose1301
 * @date 4/29/2024
 */
public class MenuUtils {
    public static final Component DEFAULT_COMPONENT = Component.text("").style(Style.style().decoration(TextDecoration.ITALIC, false));


    public static String repeat(String string, int times) {
        return new String(new char[times]).replace("\0", string);
    }

    public static Component separator(int times, TextColor color) {
        return DEFAULT_COMPONENT.append(
                Component.text(repeat(" ", times), style(color, TextDecoration.STRIKETHROUGH))
        );
    }

    public static Component chatSeparator(NamedTextColor color) {
        return separator(75, color);
    }
    public static Component centerSeparator(int index, Component component) {
        int textLength = LegacyComponentSerializer.legacySection().serialize(component).length();
        double textHalfLength = textLength / 2D;

        int halfLength = index / 2;
        halfLength -= textHalfLength;

        int diff = 0;
        if ((int)textHalfLength == textHalfLength) {
            diff = 1;
        }
        return separator(halfLength, NamedTextColor.GRAY).append(component).append(separator(halfLength + diff, NamedTextColor.GRAY));
    }
    public static Component menuSeparator() {
        return separator(50, NamedTextColor.GRAY);
    }

    public static Component scoreboardSeparator() {
        return separator(32, NamedTextColor.GRAY);
    }
    public static Style style(TextColor color, TextDecoration... decorations) {
        return Style.style(color, decorations).decoration(TextDecoration.ITALIC, false);
    }

}
