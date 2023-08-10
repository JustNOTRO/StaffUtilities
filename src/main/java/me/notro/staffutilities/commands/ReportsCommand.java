package me.notro.staffutilities.commands;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReportsCommand implements CommandExecutor {

    private final StaffUtilities plugin;

    public ReportsCommand(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.NO_SENDER_EXECUTOR);
            return false;
        }

        if (!player.hasPermission("staffutils.reports.use")) {
            player.sendMessage(Message.NO_PERMISSION);
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("clearall")) {
            plugin.getReportManager().clearReports(player);
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&c/&7" + label + " &c<&7clear/clearall&c> <&7player&7&c>")));
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (!plugin.getReportManager().hasReports(player ,target)) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + target.getName() + " &cdoes not have any active reports&7.")));
            return false;
        }

        plugin.getReportManager().clearReport(player, target);
        return true;
    }
}
