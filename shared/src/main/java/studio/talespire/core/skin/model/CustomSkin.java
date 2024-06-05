package studio.talespire.core.skin.model;

import studio.lunarlabs.universe.util.Skin;

/**
 * @author Moose1301
 * @date 5/25/2024
 */
public class CustomSkin extends Skin {

    public CustomSkin(String name, String value, String signature) {
        super("custom-" + name, value, signature);
    }
}
