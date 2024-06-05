package studio.talespire.core.effects.effect;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.CustomSound;

public class SoundEffect extends Effect {

    /**
     * Sound effect to play
     * Format: <soundName>,<volume>,<pitch>,<range>
     */
    public CustomSound sound;

    public SoundEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 1;
    }

    @Override
    public void onRun() {
        if (sound == null) return;
        sound.play(effectService.getOwningPlugin(), getLocation());
    }

}
