package tech.ice.plugins.ShulkerBoxPreview;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.server.ServerLoadEvent;
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
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static tech.ice.plugins.ShulkerBoxPreview.Config.*;
import static tech.ice.plugins.ShulkerBoxPreview.Main.ShulkerBoxPreview;

public class Events implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (!enable_open) return;
        if (event.getPlayer() instanceof Player) {
            if (open_whitelist_enable && !open_whitelist.contains(event.getView().getTitle())) return;
            for (ItemStack itemStack : event.getPlayer().getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta()) {
                        Lore.update(itemStack, (Player) event.getPlayer());
                    }
                }
            }
            for (ItemStack itemStack : event.getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta()) {
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
            if (close_whitelist_enable && !close_whitelist.contains(event.getView().getTitle())) return;
            for (ItemStack itemStack : event.getPlayer().getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta()) {
                        Lore.update(itemStack, (Player) event.getPlayer());
                    }
                }
            }
            for (ItemStack itemStack : event.getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta()) {
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
        if (itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta() && event.getEntityType().equals(EntityType.PLAYER)) {
            Lore.update(itemStack, (Player) event.getEntity());
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        if (!enable_held) return;
        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (itemStack != null && !itemStack.getType().equals(Material.AIR) && itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta()) {
            Lore.update(itemStack, event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLocalChange(PlayerLocaleChangeEvent event) {
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
                connection.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Main.langs.containsKey(locale)) {
            try {
                Main.langs.put(locale, new Gson().fromJson(new InputStreamReader(new DataInputStream(new FileInputStream(file))), JsonObject.class));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        if (!check_update_enable || !check_update_notify_login) return;
        if (!event.getPlayer().isOp() && !event.getPlayer().hasPermission("sbp.notify")) return;
        String latest;
        latest = Config.check();
        if (latest == null) return;
        if (!Objects.equals(latest, ShulkerBoxPreview.getDescription().getVersion())) {
            event.getPlayer().sendMessage(String.format(check_update_notify_message, latest));
        }
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        if (event.getType().equals(ServerLoadEvent.LoadType.STARTUP)) {
            File dir = new File(ShulkerBoxPreview.getDataFolder() + "/langs");
            String[] names = dir.list();
            if (names == null) return;
            for (String name : names) {
                try {
                    File file = new File(ShulkerBoxPreview.getDataFolder() + "/langs/" + name);
                    if (file.length() == 0 && file.delete()) break;
                    URL url = new URL(lang_lib + "/" + file);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode() != 200 && file.delete()) break;
                    BufferedInputStream inputStream1 = new BufferedInputStream(new FileInputStream(file));
                    BufferedInputStream inputStream2 = new BufferedInputStream(connection.getInputStream());
                    int data;
                    while ((data = inputStream1.read()) != -1) {
                        if (data != inputStream2.read()) {
                            inputStream1.close();
                            Files.copy(inputStream2, Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}