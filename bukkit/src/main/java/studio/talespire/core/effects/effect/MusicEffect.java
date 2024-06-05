package studio.talespire.core.effects.effect;

import org.bukkit.Particle;
import org.bukkit.Location;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;

public class MusicEffect extends Effect {

    /**
     * Radials to spawn next note.
     */
    public double radialsPerStep = Math.PI / 8;

    /**
     * Radius of circle above head
     */
    public float radius = 0.4F;

    /**
     * Current step. Works as a counter
     */
    protected float step = 0;

    public MusicEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.NOTE;
        iterations = 400;
        period = 1;
    }

    @Override
    public void reset() {
        step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        location.add(0, 1.9F, 0);
        location.add(Math.cos(radialsPerStep * step) * radius, 0, Math.sin(radialsPerStep * step) * radius);

        display(particle, location);
        step++;
    }

}
