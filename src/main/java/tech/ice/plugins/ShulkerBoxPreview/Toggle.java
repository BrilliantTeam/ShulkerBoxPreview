package tech.ice.plugins.ShulkerBoxPreview;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        if (enable.equals("on")) {
            boolean b = true;
            user.set("enable", b);
            try {
                user.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (ItemStack itemStack : Objects.requireNonNull(sender.getServer().getPlayer(sender.getName())).getInventory()) {
                if (itemStack != null) {
                    if (itemStack.getType().toString().contains("SHULKER_BOX") & itemStack.hasItemMeta()) {
                        Lore.update(itemStack, Objects.requireNonNull(sender.getServer().getPlayer(sender.getName())));
                    }
                }
            }
            sender.sendMessage(on);
            return true;
        }
        if (enable.equals("off")) {
            boolean b = false;
            user.set("enable", b);
            try {
                user.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (force_update) {
                for (ItemStack itemStack : Objects.requireNonNull(sender.getServer().getPlayer(sender.getName())).getInventory()) {
                    if (itemStack != null) {
                        if (itemStack.getType().toString().contains("SHULKER_BOX") & itemStack.hasItemMeta()) {
                            Lore.update(itemStack, Objects.requireNonNull(sender.getServer().getPlayer(sender.getName())));
                        }
                    }
                }
            }
            sender.sendMessage(off);
            return true;
        }
        return true;
    }
}