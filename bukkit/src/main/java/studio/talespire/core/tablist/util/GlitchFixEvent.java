package studio.talespire.core.tablist.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * @author Disunion
 * @date 6/7/2024
 */

@Getter
@RequiredArgsConstructor
public class GlitchFixEvent extends BaseEvent{

    private final Player player;
}
