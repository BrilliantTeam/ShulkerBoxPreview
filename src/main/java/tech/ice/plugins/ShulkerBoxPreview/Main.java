package tech.ice.plugins.ShulkerBoxPreview;

import org.bstats.bukkit.Metrics;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import java.util.Objects;

import static tech.ice.plugins.ShulkerBoxPreview.Config.*;

public class Main extends JavaPlugin {

    public static Main ShulkerBoxPreview;

    @Override
    public void onEnable() {
        int ID = 16423;
        Metrics metrics = new Metrics(this, ID);
        ShulkerBoxPreview = this;
        try {
            Config.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginCommand("sbpreload").setExecutor(new Reload());
        getServer().getPluginCommand("sbppreview").setExecutor(new Toggle());
        getServer().getPluginCommand("sbppreview").setTabCompleter(new TabToggle());
        if (!check_update_enable || !check_update_notify_startup) return;
        String latest;
        latest = Config.check();
        if (latest == null) return;
        if (!Objects.equals(latest, getDescription().getVersion())) {
            ShulkerBoxPreview.getServer().getConsoleSender().sendMessage(String.format(check_update_notify_message, latest));
        }
    }
}