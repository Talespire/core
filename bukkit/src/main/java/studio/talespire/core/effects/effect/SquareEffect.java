package studio.talespire.core.effects.effect;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import studio.talespire.core.effects.Effect;
import studio.talespire.core.effects.EffectType;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.RandomUtils;

public class SquareEffect extends Effect {

	/**
	 * Radius of the square
	 */
	public double radius = 0.6;

	/**
	 * y-offset of the square
	 */
	public double yOffset = 0;

	/**
	 * Amount of particles to display
	 */
	public int particles = 50;

	/**
	 * Amount to increase the radius per tick
	 */
	public double radiusIncrease = 0;

	/**
	 * Amount to increase the particles per tick
	 */
	public int particleIncrease = 0;

	public SquareEffect(EffectService effectService) {
		super(effectService);
		type = EffectType.REPEATING;
		particle = Particle.SPELL_MOB;
		iterations = 500;
		period = 1;
	}

	@Override
	public void onRun() {
		if (radiusIncrease != 0) radius += radiusIncrease;
		if (particleIncrease != 0) particles += particleIncrease;

		Location location = getLocation();

		if (location == null) {
			cancel();
			return;
		}

		location.add(0, yOffset, 0);
		Vector v;

		for (int i = 0; i < particles; i++) {
			v = RandomUtils.getRandomFlatVector().multiply(radius);
			location.add(v);
			display(particle, location);
			location.subtract(v);
		}
		location.subtract(0, yOffset, 0);
	}

}
