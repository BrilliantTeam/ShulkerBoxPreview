package tech.ice.plugins.ShulkerBoxPreview;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static tech.ice.plugins.ShulkerBoxPreview.Main.ShulkerBoxPreview;

public class Config {

    public static String reload;
    public static String no_per;
    public static String format_display_item;
    public static String format_item;
    public static String format_display_items;
    public static String format_items;
    public static FileConfiguration config;
    public static String lang_lib;
    public static int item_per_n_line;
    public static String item_per_n_append;
    public static String first_per_n_line;
    public static List<String> open_whitelist;
    public static boolean open_whitelist_enable;
    public static List<String> close_whitelist;
    public static boolean close_whitelist_enable;
    public static String on;
    public static String off;
    public static String usage;
    public static String only_player;
    public static Boolean default_enable;
    public static Boolean force_update;

    public static void load() throws IOException {

        if (!ShulkerBoxPreview.getDataFolder().exists()) {
            ShulkerBoxPreview.getDataFolder().mkdir();
        }

        File file = new File(ShulkerBoxPreview.getDataFolder(), "config.yml");

        if (!file.exists()) {
            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream in = ShulkerBoxPreview.getResource("config.yml");
            in.transferTo(outputStream);
        }
        config = YamlConfiguration.loadConfiguration(file);
        item_per_n_line = config.getInt("messages.item-per-n-line", 4);
        item_per_n_append = config.getString("messages.item-per-n-append", " , ");
        first_per_n_line = config.getString("messages.first-per-n-line", "  ");
        reload = config.getString("messages.command.reload", "§b已重新讀取所有配置檔");
        no_per = config.getString("messages.command.no-per", "§b已重新讀取所有配置檔案");
        format_display_item = config.getString("messages.format.display-item", "§f%1s (%2s)");
        format_item = config.getString("messages.format.item", "§f%s");
        format_display_items = config.getString("messages.format.display-items", "§f%1s (%2s) x%d");
        format_items = config.getString("messages.format.items", "§f%s x%d");
        lang_lib = config.getString("lang_lib", "https://raw.githubusercontent.com/YTiceice/LangLib/main/lang");
        close_whitelist = config.getStringList("whitelist.close.list");
        close_whitelist_enable = config.getBoolean("whitelist.close.enable", true);
        open_whitelist = config.getStringList("whitelist.open.list");
        open_whitelist_enable = config.getBoolean("whitelist.open.enable", true);
        if (Objects.equals(config.getString("config-version"), "1")) {
            file.delete();
            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream in = ShulkerBoxPreview.getResource("config.yml");
            in.transferTo(outputStream);
            FileConfiguration temp = YamlConfiguration.loadConfiguration(file);
            temp.set("messages.item-per-n-line", item_per_n_line);
            temp.set("messages.item-per-n-append", item_per_n_append);
            temp.set("messages.first-per-n-line", first_per_n_line);
            temp.set("messages.command.reload", reload);
            temp.set("messages.command.no-per", no_per);
            temp.set("messages.format.display-item", format_display_item);
            temp.set("messages.format.item", format_item);
            temp.set("messages.format.display-items", format_display_items);
            temp.set("messages.format.items", format_items);
            temp.set("lang_lib", lang_lib);
            temp.set("whitelist.close.list", close_whitelist);
            temp.set("whitelist.close.enable", close_whitelist_enable);
            temp.set("whitelist.open.list", open_whitelist);
            temp.set("whitelist.open.enable", open_whitelist_enable);
            temp.save(file);
        }
        on = config.getString("toggle-command.messages.when-on", "§b你已開啟界伏盒預覽功能");
        off = config.getString("toggle-command.messages.when-off", "§b你已關閉界伏盒預覽功能");
        usage = config.getString("toggle-command.messages.usage", "§c用法：/sbppreview <on/off>");
        only_player = config.getString("toggle-command.messages.only-player", "§c只有玩家能夠使用此指令");
        default_enable = config.getBoolean("toggle-command.default", true);
    }
}
