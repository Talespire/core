package studio.talespire.core.effects.math;

import org.bukkit.configuration.ConfigurationSection;
import studio.talespire.core.effects.EffectService;
import studio.talespire.core.effects.util.ConfigUtils;

import java.util.*;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public class Transforms {

    private static final String TRANSFORM_BUILTIN_CLASSPATH = "de.slikey.effectlib.math";
    private static Map<String, Class<?>> transformClasses = new HashMap<>();
    private static List<EffectService> effectServices = EffectService.getManagers();

    public static Transform loadTransform(ConfigurationSection base, String value) {
        if (base.isConfigurationSection(value)) return loadTransform(ConfigUtils.getConfigurationSection(base, value));
        if (base.isDouble(value) || base.isInt(value)) return new ConstantTransform(base.getDouble(value));
        if (base.isString(value)) {
            String equation = base.getString(value);
            if (equation.equalsIgnoreCase("t") || equation.equalsIgnoreCase("time")) return new EchoTransform();
            EquationTransform transform = EquationStore.getInstance().getTransform(equation, "t");
            Exception ex = transform.getException();
            if (ex != null && !effectServices.isEmpty()) {
                for (EffectService effectService : effectServices) {
                    if (effectService == null) continue;
                    effectService.onError("Error parsing equation: " + equation, ex);
                    break;
                }
            }
            return transform;
        }
        return new ConstantTransform(0);
    }

    public static Collection<Transform> loadTransformList(ConfigurationSection base, String value) {
        Collection<ConfigurationSection> transformConfigs = ConfigUtils.getNodeList(base, value);
        List<Transform> transforms = new ArrayList<>();

        for (ConfigurationSection transformConfig : transformConfigs) {
            transforms.add(loadTransform(transformConfig));
        }

        return transforms;
    }

    public static Transform loadTransform(ConfigurationSection parameters) {
        Transform transform = null;
        if (parameters != null && parameters.contains("class")) {
            String className = parameters.getString("class");
            if (className != null) {
                try {
                    if (!className.contains(".")) className = TRANSFORM_BUILTIN_CLASSPATH + "." + className;

                    Class<?> genericClass = transformClasses.get(className);
                    if (genericClass == null) {
                        try {
                            genericClass = Class.forName(className + "Transform");
                        } catch (Exception ex) {
                            genericClass = Class.forName(className);
                        }

                        if (!Transform.class.isAssignableFrom(genericClass)) throw new Exception("Must extend Transform");
                        transformClasses.put(className, genericClass);
                    }

                    @SuppressWarnings("unchecked")
                    Class<? extends Transform> transformClass = (Class<? extends Transform>)genericClass;
                    transform = transformClass.newInstance();
                    parameters.set("class", null);
                    transform.load(parameters);
                } catch (Exception ex) {
                    if (!effectServices.isEmpty()) {
                        for (EffectService effectService : effectServices) {
                            if (effectService == null) continue;
                            effectService.onError("Error loading class " + className, ex);
                            break;
                        }
                    }
                }
            }
        }

        return transform == null ? new ConstantTransform(0) : transform;
    }

}
