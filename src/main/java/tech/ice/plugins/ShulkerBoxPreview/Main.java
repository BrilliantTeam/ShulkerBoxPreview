package tech.ice.plugins.ShulkerBoxPreview;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;

import static tech.ice.plugins.ShulkerBoxPreview.Config.*;

public class Main extends JavaPlugin {

    public static Main ShulkerBoxPreview;
    public static HashMap<String, JsonObject> langs = new HashMap<>();

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
        getServer().getPluginCommand("sbppreview").setTabCompleter(new Toggle());
        if (!check_update_enable || !check_update_notify_startup) return;
        String latest;
        latest = Config.check();
        if (latest == null) return;
        if (!Objects.equals(latest, getDescription().getVersion())) {
            ShulkerBoxPreview.getServer().getConsoleSender().sendMessage(String.format(check_update_notify_message, latest));
        }
        reloadLanguage();
        try {
            Class.forName(TJImplementation.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadLanguage() {
        if (client_language) return;
        File dir = new File(ShulkerBoxPreview.getDataFolder() + "/langs/");
        String[] names = dir.list();
        if (names == null) return;
        for (String name : names) {
            try {
                File file = new File(ShulkerBoxPreview.getDataFolder() + "/langs/" + name);
                if (file.length() == 0 && file.delete()) break;
                URL url = new URL(lang_lib + "/" + name);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() != 200 && file.delete()) break;
                BufferedInputStream inputStream1 = new BufferedInputStream(new FileInputStream(file));
                BufferedInputStream inputStream2 = new BufferedInputStream(connection.getInputStream());
                int data;
                while ((data = inputStream1.read()) != -1) {
                    if (data != inputStream2.read()) {
                        Files.copy(url.openStream(), Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
                        break;
                    }
                }
                Main.langs.put(name.replace(".json", ""), new Gson().fromJson(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8), JsonObject.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
