package studio.talespire.core.effects.effect;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.MathUtils;
import studio.talespire.core.effects.util.VectorUtils;

public class DonutEffect extends Effect {

    /**
     * Amount of particles inside of a single vertical circle
     */
    public int particlesCircle = 10;

    /**
     * Amount of circles to build the torus
     */
    public int circles = 36;

    /**
     * Radius of the torus
     */
    public float radiusDonut = 2;

    /**
     * Radius of the tube (the circles on the outside).
     */
    public float radiusTube = 0.5F;

    /**
     * Rotation of the torus.
     */
    public double xRotation, yRotation, zRotation = 0;

    public DonutEffect(EffectService effectService) {
        super(effectService);
        type = EffectType.REPEATING;
        particle = Particle.FLAME;
        period = 10;
        iterations = 20;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Vector v = new Vector();

        if (location == null) {
            cancel();
            return;
        }

        double theta;
        double phi;
        double cosPhi;

        for (int i = 0; i < circles; i++) {
            theta = 2 * Math.PI * i / circles;
            for (int j = 0; j < particlesCircle; j++) {
                phi = 2 * Math.PI * j / particlesCircle;
                cosPhi = Math.cos(phi);
                v.setX((radiusDonut + radiusTube * cosPhi) * Math.cos(theta));
                v.setY((radiusDonut + radiusTube * cosPhi) * Math.sin(theta));
                v.setZ(radiusTube * Math.sin(phi));

                VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
                VectorUtils.rotateAroundAxisX(v, (location.getPitch() + 90) * MathUtils.degreesToRadians);
                VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

                display(particle, location.add(v));
                location.subtract(v);
            }
        }
    }

}
