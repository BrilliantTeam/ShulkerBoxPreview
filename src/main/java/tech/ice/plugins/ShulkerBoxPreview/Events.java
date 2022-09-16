package tech.ice.plugins.ShulkerBoxPreview;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerItemHeldEvent;
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

import static tech.ice.plugins.ShulkerBoxPreview.Config.*;
import static tech.ice.plugins.ShulkerBoxPreview.Main.ShulkerBoxPreview;

public class Events implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (!enable_open) return;
        if (event.getPlayer() instanceof Player) {
            if (open_whitelist_enable & !open_whitelist.contains(event.getView().getTitle())) return;
            for (ItemStack itemStack : event.getPlayer().getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().contains("SHULKER_BOX") & itemStack.hasItemMeta()) {
                        Lore.update(itemStack, (Player) event.getPlayer());
                    }
                }
            }
            String path = ShulkerBoxPreview.getDataFolder() + "/users/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(path + event.getPlayer().getUniqueId() + ".yml");
            FileConfiguration user;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                user = YamlConfiguration.loadConfiguration(file);
                user.set("enable", default_enable);
                try {
                    user.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user = YamlConfiguration.loadConfiguration(file);
            for (ItemStack itemStack : event.getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().contains("SHULKER_BOX") & itemStack.hasItemMeta()) {
                        if (!force_update) {
                            if (!user.getBoolean("enable")) {
                                Lore.clear(itemStack);
                            } else {
                                Lore.update(itemStack, (Player) event.getPlayer());
                            }
                            return;
                        }
                        Lore.update(itemStack, (Player) event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!enable_close) return;
        if (event.getPlayer() instanceof Player) {
            if (close_whitelist_enable & !close_whitelist.contains(event.getView().getTitle())) return;
            for (ItemStack itemStack : event.getPlayer().getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().contains("SHULKER_BOX") & itemStack.hasItemMeta()) {
                        Lore.update(itemStack, (Player) event.getPlayer());
                    }
                }
            }
            String path = ShulkerBoxPreview.getDataFolder() + "/users/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(path + event.getPlayer().getUniqueId() + ".yml");
            FileConfiguration user;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                user = YamlConfiguration.loadConfiguration(file);
                user.set("enable", default_enable);
                try {
                    user.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user = YamlConfiguration.loadConfiguration(file);
            for (ItemStack itemStack : event.getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().contains("SHULKER_BOX") & itemStack.hasItemMeta()) {
                        if (!force_update) {
                            if (!user.getBoolean("enable")) {
                                Lore.clear(itemStack);
                            } else {
                                Lore.update(itemStack, (Player) event.getPlayer());
                            }
                            return;
                        }
                        Lore.update(itemStack, (Player) event.getPlayer());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!enable_pickup) return;
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getType().toString().contains("SHULKER_BOX") & itemStack.hasItemMeta() & event.getEntityType().equals(EntityType.PLAYER)) {
            Lore.update(itemStack, (Player) event.getEntity());
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        if (!enable_held) return;
        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (itemStack != null) {
            if (itemStack.toString().contains("AIR")) return;
            if (itemStack.toString().contains("SHULKER_BOX") & itemStack.hasItemMeta()) {
                Lore.update(itemStack, event.getPlayer());
            }
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