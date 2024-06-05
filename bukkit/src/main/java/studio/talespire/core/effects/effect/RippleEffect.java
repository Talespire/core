package studio.talespire.core.effects.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.EffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public class RippleEffect extends Effect {

    public int circleParticles = 50;

    public int numRipples = 5;

    protected float step = 0;

    public RippleEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.WAX_ON;
        iterations = 100;
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

        if (step == numRipples) {
            cancel();
            return;
        }

        double y = location.getY();
        double radius = step;

        for (Vector v : createCircle(y, radius)) {
            location.add(v);
            display(particle, location);
            step++;
        }

    }

    public List<Vector> createCircle(double y, double radius) {
        double amount = radius * circleParticles;
        double inc = (2 * Math.PI) / amount;
        List<Vector> vectors = new ArrayList<>();

        double angle;
        double x;
        double z;
        Vector v;

        for (int i = 0; i < amount; i++) {
            angle = i * inc;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            v = new Vector(x, y, z);
            vectors.add(v);
        }

        return vectors;
    }
}
