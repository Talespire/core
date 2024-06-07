package studio.talespire.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Disunion
 * @date 6/7/2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdvancedReflectionUtils {

    /**
     * Checks if a class exists.
     * @param path Class path
     * @return True if class exists, false otherwise
     */
    public static boolean classExists(@NotNull String path) {
        try {
            Class.forName(path);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Checks if a method exists in a class.
     * @param clazz Class to check
     * @param method Method name
     * @param params Method parameters
     * @return True if method exists, false otherwise
     */
    public static boolean methodExists(@NotNull Class<?> clazz, @NotNull String method, @NotNull Class<?>... params) {
        try {
            clazz.getMethod(method, params);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Returns all fields from class which are of specified type.
     * @param clazz Class to get fields from
     * @param type Type of the fields
     * @return List of fields with specified type
     */
    @NotNull
    public static List<Field> getFields(@NotNull Class<?> clazz, @NotNull Class<?> type) {
        List<Field> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * Returns all methods from class which return specified class type and have specified parameter types.
     * @param clazz Class to get methods from
     * @param returnType Return type of the method
     * @param params Parameter types of the method
     * @return List of methods with specified return type and parameter types
     */
    @NotNull
    public static List<Method> getMethods(@NotNull Class<?> clazz, @NotNull Class<?> returnType, @NotNull Class<?>... params) {
        List<Method> list = new ArrayList<>();
        for (Method m : clazz.getMethods()) {
            if (!returnType.isAssignableFrom(m.getReturnType()) || m.getParameterCount() != params.length) continue;
            Class<?>[] types = m.getParameterTypes();
            boolean valid = true;
            for (int i=0; i<types.length; i++) {
                if (types[i] != params[i]) {
                    valid = false;
                    break;
                }
            }
            if (valid) list.add(m);
        }
        return list;
    }

    /**
     * Returns method with specified possible names and parameters. Throws exception if no method is found.
     * @param clazz Class to get method from
     * @param names Possible names of the method
     * @param params Parameters of the method
     * @return Method with specified names and parameters
     * @throws NoSuchMethodException If no method is found
     */
    @NotNull
    public static Method getMethod(@NotNull Class<?> clazz, @NotNull String[] names, @NotNull Class<?>... params) throws NoSuchMethodException {
        List<String> list = new ArrayList<>();
        for (Method m : clazz.getMethods()) {
            if (m.getParameterCount() != params.length) continue;
            Class<?>[] types = m.getParameterTypes();
            boolean valid = true;
            for (int i=0; i<types.length; i++) {
                if (types[i] != params[i]) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                for (String name : names) {
                    if (m.getName().equals(name)) return m;
                    String[] array = m.getName().split("_");
                    if (array.length > 2 && array[2].equals(name)) return m; // Bukkit/Forge hybrids may sometimes use these mappings
                }
                list.add(m.getName());
            }
        }
        throw new NoSuchMethodException("No method found with possible names " + Arrays.toString(names) + " with parameters " +
                Arrays.toString(params) + " in class " + clazz.getName() + ". Methods with matching parameters: " + list);
    }

    /**
     * Returns list of instance fields matching class type
     * @param clazz Class to get fields from
     * @param fieldType Field type
     * @return List of instance fields matching class type
     */
    @NotNull
    public static List<Field> getInstanceFields(@NotNull Class<?> clazz, @NotNull Class<?> fieldType) {
        List<Field> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == fieldType && !Modifier.isStatic(field.getModifiers())) {
                list.add(setAccessible(field));
            }
        }
        return list;
    }

    /**
     * Makes object accessible
     * @param o Object to make accessible
     * @return Object with accessible flag set to true
     * @param <T> Accessible object type
     */
    @NotNull
    public static <T extends AccessibleObject> T setAccessible(@NotNull T o) {
        o.setAccessible(true);
        return o;
    }
}
