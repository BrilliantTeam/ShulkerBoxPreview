package tech.ice.plugins.ShulkerBoxPreview;

import com.google.gson.JsonObject;

import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static tech.ice.plugins.ShulkerBoxPreview.Config.*;
import static tech.ice.plugins.ShulkerBoxPreview.Main.ShulkerBoxPreview;

public class Lore {

    public static void update(ItemStack itemStack, Player player) {
        UUID uuid = player.getUniqueId();
        String path = ShulkerBoxPreview.getDataFolder() + "/users/";
        File data = new File(path + uuid + ".yml");
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        FileConfiguration user;
        if (!data.exists()) {
            try {
                data.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user = YamlConfiguration.loadConfiguration(data);
            user.set("enable", default_enable);
            try {
                user.save(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        user = YamlConfiguration.loadConfiguration(data);

        if (force_update && !user.getBoolean("enable")) {
            Lore.clear(itemStack);
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) return;
            int lines = 0;
            int times = 0;
            List<String> lore = new ArrayList<>();
            JsonObject locale = Main.langs.get(player.getLocale().toLowerCase());

            if (itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta) {
                ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
                Inventory inventory = shulkerBox.getInventory();

                for (ItemStack item : inventory) {
                    if (item == null) continue;
                    times++;

                    String str;
                    if (locale.get(item.getTranslationKey()) != null)
                        str = locale.get(item.getTranslationKey()).getAsString();
                    else
                        str = locale.get("argument.id.invalid").getAsString();

                    String msg;
                    if (meta.hasDisplayName())
                        msg = item.getAmount() == 1 ? String.format(format_display_item, meta.getDisplayName(), str) : String.format(format_display_items, meta.getDisplayName(), str, item.getAmount());
                    else
                        msg = item.getAmount() == 1 ? String.format(format_item, str) : String.format(format_items, str, item.getAmount());

                    if (lore.size() == 0) {
                        lore.add(first_per_n_line + msg);
                    } else if (times <= item_per_n_line) {
                        lore.set(lines, lore.get(lines) + item_per_n_append + msg);
                    } else {
                        lore.add(first_per_n_line + msg);
                        lines++;
                        times = 1;
                    }
                }
            }

            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemStack.setItemMeta(meta);
        }
    }

    public static void clear(ItemStack itemStack) {
        if (itemStack.getItemMeta() != null && itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasLore()) {
                List<String> lore = itemStack.getItemMeta().getLore();
                lore.clear();
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }
}
