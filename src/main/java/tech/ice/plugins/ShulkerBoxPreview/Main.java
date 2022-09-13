package tech.ice.plugins.ShulkerBoxPreview;

import org.bstats.bukkit.Metrics;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.util.List;

public class Main extends JavaPlugin {

    public static Main ShulkerBoxPreview;
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

    @Override
    public void onEnable() {
        int ID = 16423;
        Metrics metrics = new Metrics(this, ID);
        ShulkerBoxPreview = this;
        try {
            this.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginCommand("sbpreload").setExecutor(new Reload());
    }

    public void load() throws IOException {

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
    }
}