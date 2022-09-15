package tech.ice.plugins.ShulkerBoxPreview;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
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
        if (force_update & !user.getBoolean("enable")) {
            Lore.clear(itemStack);
        } else {
            File file = new File(ShulkerBoxPreview.getDataFolder() + "/langs/" + player.getLocale().toLowerCase() + ".json");
            InputStream is;
            try {
                is = new DataInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            JsonObject locale = new Gson().fromJson(new InputStreamReader(is), JsonObject.class);
            ItemMeta meta = itemStack.getItemMeta();
            NBTItem nbtItem = new NBTItem(itemStack);
            NBTCompoundList list = nbtItem.getCompound("BlockEntityTag").getCompoundList("Items");
            int lines = 0;
            int times = 0;
            List<String> lore = new ArrayList<>();
            for (int i = 5; i < list.size(); i++) {
                times++;
                NBTCompound nbt = list.get(i);
                boolean display = nbt.toString().contains("display") & nbt.toString().contains("Name") & nbt.toString().contains("text");
                if (nbt.toString().contains("Potion")) {
                    if (display) {
                        String json = nbt.getCompound("tag").getCompound("display").getString("Name");
                        StringBuilder tag = new StringBuilder();
                        if (json.startsWith("[") & json.endsWith("]")) {
                            JsonArray jsonArray = new Gson().fromJson(json, JsonArray.class);
                            for (int tags = 0; tags < jsonArray.size(); tags++) {
                                JsonObject jsonObject = new Gson().fromJson(jsonArray.get(tags).toString(), JsonObject.class);
                                tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                            }
                        } else if (json.contains("extra")) {
                            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                            tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                            JsonArray jsonArray = new Gson().fromJson(jsonObject.get("extra").toString(), JsonArray.class);
                            for (int tags = 0; tags < jsonArray.size(); tags++) {
                                jsonObject = new Gson().fromJson(jsonArray.get(tags).toString(), JsonObject.class);
                                tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                            }
                        } else if (json.startsWith("{") & json.endsWith("}")) {
                            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                            tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                        }
                        if (nbt.getDouble("Count") == 1) {
                            String item = nbt.getString("id") + ".effect." + nbt.getCompound("tag").getString("Potion").replace("minecraft:", "").replace("strong_", "").replace("long_", "");
                            String str;
                            if (locale.get(item) != null) {
                                str = locale.get(item).toString();
                            } else {
                                str = locale.get("argument.id.invalid").toString();
                            }
                            String msg = String.format(format_display_item, tag, str.substring(1, str.length() - 1));
                            if (lore.size() == 0) {
                                lore.add(first_per_n_line + msg);
                            } else if (times <= item_per_n_line) {
                                lore.set(lines, lore.get(lines) + item_per_n_append + msg);
                            } else {
                                lore.add(first_per_n_line + msg);
                                lines++;
                                times = 1;
                            }
                        } else {
                            String item = nbt.getString("id") + ".effect." + nbt.getCompound("tag").getString("Potion").replace("minecraft:", "").replace("strong_", "").replace("long_", "");
                            String str;
                            if (locale.get(item) != null) {
                                str = locale.get(item).toString();
                            } else {
                                str = locale.get("argument.id.invalid").toString();
                            }
                            String msg = String.format(format_display_items, tag, str.substring(1, str.length() - 1), nbt.getInteger("Count"));
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
                    } else {
                        if (nbt.getDouble("Count") == 1) {
                            String item = nbt.getString("id") + ".effect." + nbt.getCompound("tag").getString("Potion").replace("minecraft:", "").replace("strong_", "").replace("long_", "");
                            String str;
                            if (locale.get(item) != null) {
                                str = locale.get(item).toString();
                            } else {
                                str = locale.get("argument.id.invalid").toString();
                            }
                            String msg = String.format(format_item, str.substring(1, str.length() - 1));
                            if (lore.size() == 0) {
                                lore.add(first_per_n_line + msg);
                            } else if (times <= item_per_n_line) {
                                lore.set(lines, lore.get(lines) + item_per_n_append + msg);
                            } else {
                                lore.add(first_per_n_line + msg);
                                lines++;
                                times = 1;
                            }
                        } else {
                            String item = nbt.getString("id") + ".effect." + nbt.getCompound("tag").getString("Potion").replace("minecraft:", "").replace("strong_", "").replace("long_", "");
                            String str;
                            if (locale.get(item) != null) {
                                str = locale.get(item).toString();
                            } else {
                                str = locale.get("argument.id.invalid").toString();
                            }
                            String msg = String.format(format_items, str.substring(1, str.length() - 1), nbt.getInteger("Count"));
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
                } else if (display) {
                    String json = nbt.getCompound("tag").getCompound("display").getString("Name");
                    StringBuilder tag = new StringBuilder();
                    if (json.startsWith("[") & json.endsWith("]")) {
                        JsonArray jsonArray = new Gson().fromJson(json, JsonArray.class);
                        for (int tags = 0; tags < jsonArray.size(); tags++) {
                            JsonObject jsonObject = new Gson().fromJson(jsonArray.get(tags).toString(), JsonObject.class);
                            tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                        }
                    } else if (json.contains("extra")) {
                        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                        tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                        JsonArray jsonArray = new Gson().fromJson(jsonObject.get("extra").toString(), JsonArray.class);
                        for (int tags = 0; tags < jsonArray.size(); tags++) {
                            jsonObject = new Gson().fromJson(jsonArray.get(tags).toString(), JsonObject.class);
                            tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                        }
                    } else if (json.startsWith("{") & json.endsWith("}")) {
                        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                        tag.append(jsonObject.get("text").toString(), 1, jsonObject.get("text").toString().length() - 1);
                    }
                    if (nbt.getDouble("Count") == 1) {
                        String item = (nbt.getString("id"));
                        String str;
                        if (locale.get(item) != null) {
                            str = locale.get(item).toString();
                        } else {
                            str = locale.get("argument.id.invalid").toString();
                        }
                        String msg = String.format(format_display_item, tag, str.substring(1, str.length() - 1));
                        if (lore.size() == 0) {
                            lore.add(first_per_n_line + msg);
                        } else if (times <= item_per_n_line) {
                            lore.set(lines, lore.get(lines) + item_per_n_append + msg);
                        } else {
                            lore.add(first_per_n_line + msg);
                            lines++;
                            times = 1;
                        }
                    } else {
                        String item = (nbt.getString("id"));
                        String str;
                        if (locale.get(item) != null) {
                            str = locale.get(item).toString();
                        } else {
                            str = locale.get("argument.id.invalid").toString();
                        }
                        String msg = String.format(format_display_items, tag, str.substring(1, str.length() - 1), nbt.getInteger("Count"));
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
                } else {
                    if (nbt.getDouble("Count") == 1) {
                        String item = (nbt.getString("id"));
                        String str;
                        if (locale.get(item) != null) {
                            str = locale.get(item).toString();
                        } else {
                            str = locale.get("argument.id.invalid").toString();
                        }
                        String msg = String.format(format_item, str.substring(1, str.length() - 1));
                        if (lore.size() == 0) {
                            lore.add(first_per_n_line + msg);
                        } else if (times <= item_per_n_line) {
                            lore.set(lines, lore.get(lines) + item_per_n_append + msg);
                        } else {
                            lore.add(first_per_n_line + msg);
                            lines++;
                            times = 1;
                        }
                    } else {
                        String item = (nbt.getString("id"));
                        String str;
                        if (locale.get(item) != null) {
                            str = locale.get(item).toString();
                        } else {
                            str = locale.get("argument.id.invalid").toString();
                        }
                        String msg = String.format(format_items, str.substring(1, str.length() - 1), nbt.getInteger("Count"));
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
            }
            if (meta != null) {
                meta.setLore(lore);
            }
            itemStack.setItemMeta(meta);
        }
    }

    public static void clear(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
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
