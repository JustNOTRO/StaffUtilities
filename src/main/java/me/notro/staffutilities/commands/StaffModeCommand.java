package me.notro.staffutilities.commands;

import me.notro.staffutilities.StaffModeManager;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import me.notro.staffutilities.utils.Sounds;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StaffModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.NO_SENDER_EXECUTOR);
            return false;
        }

        if (!player.hasPermission("staffutils.staffmode")) {
            player.sendMessage(Message.NO_PERMISSION);
            return false;
        }

        if (args.length > 0) {
            player.sendMessage(Message.fixColor(Message.getPrefix() + "&c/&7" + label));
            return false;
        }

        ConfigurationSection staffModeSection = StaffUtilities.getInstance().getConfig().getConfigurationSection("staff-mode");
        List<String> staffModeList = staffModeSection.getStringList("players");
        StaffModeManager staffModeManager = new StaffModeManager(player, staffModeSection, staffModeList);

        if (!staffModeManager.isInStaffMode()) {
            staffModeManager.joinStaffMode();
            player.sendMessage(Message.fixColor(Message.getPrefix() + "&aYou have enabled staff mode&7."));
            Sounds.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return true;
        }

        staffModeManager.quitStaffMode();
        player.sendMessage(Message.fixColor(Message.getPrefix() + "&cYou have disabled staff mode&7."));
        Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        return true;
    }
}
