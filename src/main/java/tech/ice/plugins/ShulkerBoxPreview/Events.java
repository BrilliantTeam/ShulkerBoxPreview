package tech.ice.plugins.ShulkerBoxPreview;

import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.bukkit.Material.SHULKER_BOX;
import static tech.ice.plugins.ShulkerBoxPreview.Main.*;

public class Events implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (open_whitelist_enable & !open_whitelist.contains(event.getView().getTitle())) {
                return;
            }
            for (ItemStack itemStack : event.getPlayer().getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().equals(SHULKER_BOX) & itemStack.hasItemMeta()) {
                        Lore.set(itemStack, (Player) event.getPlayer());
                    }
                }
            }
            for (ItemStack itemStack : event.getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().equals(SHULKER_BOX) & itemStack.hasItemMeta()) {
                        Lore.set(itemStack, (Player) event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (close_whitelist_enable & !close_whitelist.contains(event.getView().getTitle())) {
            return;
        }
        if (event.getPlayer() instanceof Player) {
            for (ItemStack itemStack : event.getPlayer().getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().equals(SHULKER_BOX) & itemStack.hasItemMeta()) {
                        Lore.set(itemStack, (Player) event.getPlayer());
                    }
                }
            }
            for (ItemStack itemStack : event.getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().equals(SHULKER_BOX) & itemStack.hasItemMeta()) {
                        Lore.set(itemStack, (Player) event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getType().equals(SHULKER_BOX) & itemStack.hasItemMeta() & event.getEntityType().equals(EntityType.PLAYER)) {
            Lore.set(itemStack, (Player) event.getEntity());
        }
    }

    @EventHandler
    public void getLocal(PlayerLocaleChangeEvent event) {
        String locale = event.getLocale().toLowerCase();
        File dir = new File(ShulkerBoxPreview.getDataFolder() + "/langs");
        File file = new File(ShulkerBoxPreview.getDataFolder() + "/langs/" + locale + ".json");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                URL url = new URL(lang_lib + "/" + locale + ".json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                Files.copy(inputStream, Paths.get(file.getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}