package studio.talespire.core.tablist.api.packets.fake;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import studio.talespire.core.tablist.api.utils.FakePlayerUtil;

import java.util.UUID;

/**
 * @author Disunion
 * @date 6/14/2024
 */

@Getter
public class FakePlayer {
    private final TablistAddFakePlayerPacket tablistAddPacket;
    private final TablistRemoveFakePlayerPacket tablistRemovePacket;

    public FakePlayer(String name, Component displayName) {
        UUID fakeUUID = UUID.randomUUID();
        this.tablistAddPacket = new TablistAddFakePlayerPacket(fakeUUID, name, displayName);
        this.tablistRemovePacket = new TablistRemoveFakePlayerPacket(fakeUUID, name, displayName);
    }

    public FakePlayer(String name) {
        this(name, Component.empty());
    }

    public static FakePlayer randomFakePlayer() {
        return new FakePlayer(FakePlayerUtil.randomName());
    }
}
