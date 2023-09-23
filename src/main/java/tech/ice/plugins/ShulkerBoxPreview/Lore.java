package tech.ice.plugins.ShulkerBoxPreview;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import tech.ice.plugins.ShulkerBoxPreview.TJImplementation.ComponentProcessor;
import tech.ice.plugins.ShulkerBoxPreview.TJImplementation.LocaleManager;

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
            return;
        }
        ItemMeta meta_ = itemStack.getItemMeta();
        if (!(meta_ instanceof BlockStateMeta meta)) return;
        int lines = 0;
        int times = 0;

        ShulkerBox shulkerBox = (ShulkerBox) meta.getBlockState();
        Inventory inventory = shulkerBox.getInventory();

        if (client_language) {
            List<BaseComponent> lore = new ArrayList<>();

            String cachedFormat = "";
            for (ItemStack item : inventory) {
                if (item == null) continue;
                if (item.getItemMeta() == null) continue;
                times++;

                if (lore.isEmpty()) cachedFormat = ComponentProcessor.refreshFormat(cachedFormat + first_per_n_line);
                else if ((times <= item_per_n_line))
                    cachedFormat = ComponentProcessor.refreshFormat(cachedFormat + item_per_n_append);
                else
                    cachedFormat = ComponentProcessor.refreshFormat(cachedFormat + first_per_n_line);

                String msg;
                String toSplit = item.getItemMeta().hasDisplayName() ? "%2\\$s" : "%s";
                if (item.getItemMeta().hasDisplayName())
                    msg = item.getAmount() == 1 ? String.format(format_display_item, item.getItemMeta().getDisplayName(), "%2$s") : String.format(format_display_items, item.getItemMeta().getDisplayName(), "%2$s", item.getAmount());
                else msg = item.getAmount() == 1 ? format_item : String.format(format_items, "%s", item.getAmount());

                BaseComponent component = new TextComponent();
                String[] split = msg.split(toSplit, -1);
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    component.addExtra(ComponentProcessor.parse(cachedFormat + s));
                    cachedFormat = ComponentProcessor.refreshFormat(cachedFormat + s);
                    if (i != split.length - 1)
                        component.addExtra(ComponentProcessor.applyLastFormat(new TranslatableComponent(LocaleManager.queryItemStack(item)), cachedFormat));
                }

                if (lore.isEmpty()) {
                    lore.add(ComponentProcessor.join(ComponentProcessor.parse(first_per_n_line), component));
                } else if (times <= item_per_n_line) {
                    lore.set(lines, ComponentProcessor.join(lore.get(lines), ComponentProcessor.parse(cachedFormat + item_per_n_append), component));
                } else {
                    cachedFormat = "";
                    lore.add(ComponentProcessor.join(ComponentProcessor.parse(first_per_n_line), component));
                    lines++;
                    times = 1;
                }
            }

            ItemMeta new_ = TJImplementation.asLoreSet(itemStack, lore).getItemMeta();
            assert new_ != null;
            new_.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemStack.setItemMeta(new_);
            return;
        }
        List<String> lore = new ArrayList<>();
        JsonObject locale = Main.langs.get(player.getLocale().toLowerCase());

        for (ItemStack item : inventory) {
            if (item == null) continue;
            if (item.getItemMeta() == null) continue;
            times++;

            String str;
            if (locale.get(LocaleManager.queryItemStack(item)) != null)
                str = locale.get(LocaleManager.queryItemStack(item)).getAsString();
            else str = locale.get("argument.id.invalid").getAsString();

            String msg;
            if (item.getItemMeta().hasDisplayName())
                msg = item.getAmount() == 1 ? String.format(format_display_item, item.getItemMeta().getDisplayName(), str) : String.format(format_display_items, item.getItemMeta().getDisplayName(), str, item.getAmount());
            else
                msg = item.getAmount() == 1 ? String.format(format_item, str) : String.format(format_items, str, item.getAmount());

            if (lore.isEmpty()) {
                lore.add(first_per_n_line + msg);
            } else if (times <= item_per_n_line) {
                lore.set(lines, lore.get(lines) + item_per_n_append + msg);
            } else {
                lore.add(first_per_n_line + msg);
                lines++;
                times = 1;
            }
        }

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(meta);
    }

    public static void clear(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.hasLore()) {
                itemMeta.setLore(null);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }
}
