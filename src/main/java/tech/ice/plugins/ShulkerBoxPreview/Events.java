package tech.ice.plugins.ShulkerBoxPreview;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static tech.ice.plugins.ShulkerBoxPreview.Config.*;
import static tech.ice.plugins.ShulkerBoxPreview.Main.ShulkerBoxPreview;

public class Events implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
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
    public void onInventoryClose(InventoryCloseEvent event) {
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
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!enable_pickup) return;
        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta() && event.getEntityType().equals(EntityType.PLAYER)) {
            Lore.update(itemStack, (Player) event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (!enable_held) return;
        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (itemStack != null && !itemStack.getType().equals(Material.AIR) && itemStack.getType().toString().endsWith("SHULKER_BOX") && itemStack.hasItemMeta()) {
            Lore.update(itemStack, event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event) {
        if (client_language) return;
        String locale = event.getLocale().toLowerCase();
        File dir = new File(ShulkerBoxPreview.getDataFolder() + "/langs");
        File file = new File(ShulkerBoxPreview.getDataFolder() + "/langs/" + locale + ".json");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                URL url = new URL(lang_lib + "/" + locale + ".json");
                Files.copy(url.openStream(), Paths.get(file.getPath()));
                Main.langs.put(locale, new Gson().fromJson(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8), JsonObject.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!check_update_enable || !check_update_notify_login) return;
        if (!event.getPlayer().isOp() && !event.getPlayer().hasPermission("sbp.notify")) return;
        String latest;
        latest = Config.check();
        if (latest == null) return;
        if (!Objects.equals(latest, ShulkerBoxPreview.getDescription().getVersion())) {
            event.getPlayer().sendMessage(String.format(check_update_notify_message, latest));
        }
    }
}
