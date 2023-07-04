package me.notro.staffutilities.commands;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.ReportManager;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffReportsCommand implements CommandExecutor {

    private final ReportManager reportManager = StaffUtilities.getInstance().getReportManager();

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

        if (args.length < 1 || args.length == 1) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&c/&7" + label + " &c<&7clear/clearall&c> <&7player&7&c>")));
            return false;
        }

        if (args[0].equalsIgnoreCase("clearall")) {
            reportManager.clearReports(player);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (!reportManager.hasReports(player ,target)) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + target.getName() + " &cdoes not have any active reports&7.")));
            return false;
        }

        reportManager.clearReport(player, target);
        return true;
    }
}
