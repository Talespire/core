package studio.talespire.core.effects;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public enum EffectType {
    /**
     * Effect is played once instantly.
     */
    INSTANT,
    /**
     * Effect is played several times instantly. Set the interval with {@link Effect#period}.
     */
    REPEATING,
    /**
     * Effect is played once delayed. Set delay with {@link Effect#delay}.
     */
    DELAYED
}
