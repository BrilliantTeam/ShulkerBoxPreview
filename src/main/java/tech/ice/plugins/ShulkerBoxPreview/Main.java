package tech.ice.plugins.ShulkerBoxPreview;

import org.bstats.bukkit.Metrics;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

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
    }
}