package tech.ice.plugins.ShulkerBoxPreview;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;

import static tech.ice.plugins.ShulkerBoxPreview.Main.ShulkerBoxPreview;

public class Config {

    public static String version;
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
    public static String only_player;
    public static String usage;
    public static boolean force_update;
    public static boolean default_enable;
    public static boolean enable_open;
    public static boolean enable_close;
    public static boolean enable_pickup;
    public static boolean enable_held;
    public static boolean check_update_enable;
    public static boolean check_update_notify_startup;
    public static boolean check_update_notify_login;
    public static String check_update_notify_message;

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
        if (config.get("config-version") == null) {
            file.delete();
            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream in = ShulkerBoxPreview.getResource("config.yml");
            in.transferTo(outputStream);
        }
        version = config.getString("config-version");
        item_per_n_line = config.getInt("messages.item-per-n-line", 4);
        item_per_n_append = config.getString("messages.item-per-n-append", " , ");
        first_per_n_line = config.getString("messages.first-per-n-line", "  ");
        reload = config.getString("messages.command.reload", "§b已重新讀取所有配置檔");
        no_per = config.getString("messages.command.no-per", "§b已重新讀取所有配置檔案");
        format_display_item = config.getString("messages.format.display-item", "§f%1$s (%2$s)");
        format_item = config.getString("messages.format.item", "§f%s");
        format_display_items = config.getString("messages.format.display-items", "§f%1$s (%2$s) x%3$d");
        format_items = config.getString("messages.format.items", "§f%s x%d");
        lang_lib = config.getString("lang_lib", "https://raw.githubusercontent.com/YTiceice/LangLib/main");
        close_whitelist = config.getStringList("whitelist.close.list");
        close_whitelist_enable = config.getBoolean("whitelist.close.enable", true);
        open_whitelist = config.getStringList("whitelist.open.list");
        open_whitelist_enable = config.getBoolean("whitelist.open.enable", true);
        update(file, version);
        on = config.getString("toggle-command.messages.when-on", "§b你已開啟界伏盒預覽功能");
        off = config.getString("toggle-command.messages.when-off", "§b你已關閉界伏盒預覽功能");
        only_player = config.getString("toggle-command.messages.only-player", "§c只有玩家能夠使用此指令");
        usage = config.getString("toggle-command.messages.usage", "§c用法：/sbppreview <on/off>");
        force_update = config.getBoolean("force-update", true);
        default_enable = config.getBoolean("toggle-command.default", true);
        enable_open = config.getBoolean("enable.when-open", true);
        enable_close = config.getBoolean("enable.when-close", true);
        enable_pickup = config.getBoolean("enable.pickup-item", true);
        enable_held = config.getBoolean("enable.held-item", true);
        update(file, version);
        check_update_enable = config.getBoolean("check_update.enable", true);
        check_update_notify_startup = config.getBoolean("check_update.notify.startup", true);
        check_update_notify_login = config.getBoolean("check_update.notify.login", true);
        check_update_notify_message = config.getString("check_update.notify.message", "§bShulkerBoxPreview 已推出 %s, 請在此下載更新:§6https://www.spigotmc.org/resources/shulkerboxpreview.105258");
    }

    private static void update(File file, String version) throws IOException {
        switch (version) {
            case "1": {
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
            case "1.1": {
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
                temp.set("toggle-command.messages.when-on", on);
                temp.set("toggle-command.messages.when-off", off);
                temp.set("toggle-command.messages.only-player", only_player);
                temp.set("toggle-command.messages.usage", usage);
                temp.set("force-update", force_update);
                temp.set("toggle-command.default", default_enable);
                temp.set("enable.when-open", enable_open);
                temp.set("enable.when-close", enable_close);
                temp.set("enable.pickup-item", enable_pickup);
                temp.set("enable.held-item", enable_held);
                temp.save(file);
            }
            case "1.2": {
                FileConfiguration temp = YamlConfiguration.loadConfiguration(file);
                temp.set("config-version", 1.3);
                temp.set("lang_lib", "https://raw.githubusercontent.com/YTiceice/LangLib/main");
                temp.save(file);
            }
        }
    }

    public static String check() {
        try {
            HttpURLConnection connection;
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=105258");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            return new BufferedReader(new InputStreamReader(inputStream)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
