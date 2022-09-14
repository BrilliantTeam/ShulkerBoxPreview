package tech.ice.plugins.ShulkerBoxPreview;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static tech.ice.plugins.ShulkerBoxPreview.Main.ShulkerBoxPreview;
import static tech.ice.plugins.ShulkerBoxPreview.Config.*;

public class Toggle implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(only_player);
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(usage);
            return true;
        }
        String enable = args[0];
        if (!Objects.equals(enable, "on") && !Objects.equals(enable, "off")) {
            sender.sendMessage(usage);
            return true;
        }
        String path = ShulkerBoxPreview.getDataFolder() + "/users/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(path + sender.getServer().getOfflinePlayer(sender.getName()).getUniqueId() + ".yml");
        FileConfiguration user;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            user = YamlConfiguration.loadConfiguration(file);
            user.set("enable", default_enable);
            try {
                user.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        user = YamlConfiguration.loadConfiguration(file);
        enable = enable.replace("on", "true").replace("off", "false");
        boolean b = Boolean.parseBoolean(enable);
        user.set("enable", b);
        try {
            user.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}