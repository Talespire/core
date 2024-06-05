package studio.talespire.core.effects.math;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Disunion
 * @date 6/4/2024
 */
public class EquationStore {

    private static final String DEFAULT_VARIABLE = "x";
    private static EquationStore instance;
    private final Map<String, EquationTransform> transforms = new HashMap<>();

    public EquationTransform getTransform(String equation) {
        return getTransform(equation, DEFAULT_VARIABLE);
    }

    public EquationTransform getTransform(String equation, String variable) {
        EquationTransform transform = transforms.get(equation);
        if (transform == null) {
            transform = new EquationTransform(equation, variable);
            transforms.put(equation, transform);
        }

        return transform;
    }

    public EquationTransform getTransform(String equation, String... variables) {
        String equationKey = equation + ":" + StringUtils.join(variables, ",");
        EquationTransform transform = transforms.get(equationKey);
        if (transform == null) {
            transform = new EquationTransform(equation, variables);
            transforms.put(equationKey, transform);
        }

        return transform;
    }

    public EquationTransform getTransform(String equation, Collection<String> variables) {
        String equationKey = equation + ":" + StringUtils.join(variables, ",");
        EquationTransform transform = transforms.get(equationKey);
        if (transform == null) {
            transform = new EquationTransform(equation, variables);
            transforms.put(equationKey, transform);
        }

        return transform;
    }

    public static void clear() {
        if (instance != null) instance.transforms.clear();
    }

    public static EquationStore getInstance() {
        if (instance == null) instance = new EquationStore();

        return instance;
    }

}