package studio.talespire.core.skin;

import com.google.gson.JsonObject;
import studio.lunarlabs.universe.Universe;
import studio.lunarlabs.universe.util.Skin;
import studio.lunarlabs.universe.util.Statics;
import studio.talespire.core.Core;
import studio.talespire.core.skin.model.CustomSkin;
import studio.talespire.core.util.StringUtils;

import javax.swing.plaf.IconUIResource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 5/25/2024
 */
public class CustomSkinService {
    private final Map<String, CustomSkin> skins = new HashMap<>();


    public CustomSkinService() {
        this.loadFiles();
        this.addToSkinCache();
        Core.getInstance().getLogger().info("Loaded {} custom skins", this.skins.size());
    }

    public void loadFiles() {
        File skinFolder = new File(Core.getInstance().getDataFolder().toFile(), "skins");
        if (!skinFolder.exists()) {
            skinFolder.mkdirs();
            return;
        }
        if(!skinFolder.isDirectory()) {
            return;
        }
        for (File file : skinFolder.listFiles()) {
            try {
                JsonObject object = Statics.gson().fromJson(Files.readString(file.toPath()), JsonObject.class);
                String name = StringUtils.getFileExtension(file);
                this.skins.put(name, new CustomSkin(
                        name, object.get("value").getAsString(), object.get("signature").getAsString()
                ));

            } catch (IOException e) {
                Core.getInstance().getLogger().error("Failed to read {}", file.getName(), e);
            }
        }
    }

    public void addToSkinCache() {
        for (CustomSkin value : skins.values()) {
            Skin.SKINS.put(value.getName(), value);
        }
    }



}
