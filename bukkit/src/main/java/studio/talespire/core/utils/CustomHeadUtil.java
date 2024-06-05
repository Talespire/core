package studio.talespire.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

/**
 * @author Disunion
 * @date 5/28/2024
 */
public class CustomHeadUtil {

    /**
     * Get a custom head from a URL
     * @param url {@link String} URL of the head
     * @return {@link ItemStack} Custom head
     * @throws MalformedURLException If the URL is malformed
     */
    public ItemStack getCustomHead(String url) throws MalformedURLException {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta) {
            var playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
            playerProfile.getTextures().setSkin(URI.create(url).toURL());
            skullMeta.setOwnerProfile(playerProfile);
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }

    public ItemStack getCustomHead(String url, String displayName) throws MalformedURLException {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta) {
            var playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
            playerProfile.getTextures().setSkin(URI.create(url).toURL());
            skullMeta.setOwnerProfile(playerProfile);
            skullMeta.setDisplayName(displayName);
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }

    public ItemStack getCustomHead(String url, String displayName, String... lore) throws MalformedURLException {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        if (itemStack.getItemMeta() instanceof SkullMeta skullMeta) {
            var playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
            playerProfile.getTextures().setSkin(URI.create(url).toURL());
            skullMeta.setOwnerProfile(playerProfile);
            skullMeta.setDisplayName(displayName);
            skullMeta.setLore(java.util.Arrays.asList(lore));
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }
}