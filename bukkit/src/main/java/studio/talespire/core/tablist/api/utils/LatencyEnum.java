package studio.talespire.core.tablist.api.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Disunion
 * @date 6/14/2024
 */
@Getter
@AllArgsConstructor
public enum LatencyEnum {
    ZERO(-1),
    ONE(1001),
    TWO(700),
    THREE(500),
    FOUR(200),
    FIVE(100);

    private final int latency;
}
