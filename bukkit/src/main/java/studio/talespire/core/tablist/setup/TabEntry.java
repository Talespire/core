package studio.talespire.core.tablist.setup;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import studio.lunarlabs.universe.util.Skin;

/**
 * @author Disunion
 * @date 6/7/2024
 */

@Getter
@Setter
@Accessors( chain = true)
public class TabEntry {
    private final int x, y;
    private Component text;
    private int ping = 0;
    private Skin skin = Skin.DEFAULT_SKIN;

    public TabEntry(int x, int y, Component text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public TabEntry(int x, int y, Component text, int ping) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.ping = ping;
    }

    public TabEntry(int x, int y, Component text, Skin skin) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.skin = skin;
    }

    public TabEntry(int x, int y, Component text, int ping, Skin skin) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.ping = ping;
        this.skin = skin;
    }
}
