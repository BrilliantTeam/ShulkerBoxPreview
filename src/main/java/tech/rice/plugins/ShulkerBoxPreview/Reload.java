package tech.rice.plugins.ShulkerBoxPreview;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

import static tech.rice.plugins.ShulkerBoxPreview.Config.*;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp() && sender.hasPermission("sbp.reload")) {
            try {
                Config.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sender.sendMessage(reload);
        } else {
            sender.sendMessage(no_per);
        }
        return true;
    }
}
