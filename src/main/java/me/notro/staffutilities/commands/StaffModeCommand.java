package me.notro.staffutilities.commands;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.utils.Message;
import me.notro.staffutilities.utils.Sounds;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffModeCommand implements CommandExecutor {


    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.getPrefix().append(Message.NO_SENDER_EXECUTOR));
            return false;
        }

        if (!player.hasPermission("staffutils.staffmode")) {
            player.sendMessage(Message.getPrefix().append(Message.NO_PERMISSION));
            return false;
        }

        if (args.length > 0) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&c/&7" + label)));
            return false;
        }

        if (!staffModeManager.isInStaffMode(player)) {
            staffModeManager.joinStaffMode(player);
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&aYou have enabled staff mode&7.")));
            Sounds.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return true;
        }

        staffModeManager.quitStaffMode(player);
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have disabled staff mode&7.")));
        Sounds.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        return true;
    }
}
