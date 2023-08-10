package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ReportManager {

    private final StaffUtilities plugin;

    public ReportManager(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    public void createReport(@NonNull Player reporter, OfflinePlayer target, @NonNull String reason) {
        if (reporter == target) {
            reporter.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou can't report yourself&7.")));
            return;
        }

        plugin.getStaffUtilsConfig().get().set("reports-system." + target.getName() + ".reporter", reporter.getName());
        plugin.getStaffUtilsConfig().get().set("reports-system." + target.getName() + ".reason", reason);
        plugin.getStaffUtilsConfig().get().set("reports-system." + target.getName() + ".uuid", target.getUniqueId().toString());
        plugin.getStaffUtilsConfig().save();

        reporter.sendMessage(Message.fixColor("&7Successfully reported &6" + target.getName() + " &7for " + reason + "&7."));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + target.getName() + " &7has been reported for the reason " + reason + "&7.")), "staffutils.staff.notify");
    }

    public void clearReport(@NonNull Player staff, OfflinePlayer target) {
        plugin.getStaffUtilsConfig().get().set("reports-system." + target.getName(), null);
        plugin.getStaffUtilsConfig().save();

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully removed the report(s) on &6" + target.getName() + "&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7cleared all reports of &6" + target.getName() + "&7.")), "staffutils.staff.notify");

    }

    public void clearReports(@NonNull Player staff) {
        plugin.getStaffUtilsConfig().get().set("reports-system", null);
        plugin.getStaffUtilsConfig().save();

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully cleared all reports.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7cleared all reports.")), "staffutils.staff.notify");
    }

    public boolean hasReports(@NonNull Player staff, OfflinePlayer target) {
        if (plugin.getStaffUtilsConfig().get().getConfigurationSection("reports-system") == null) {
            staff.sendMessage(Message.getPrefix().append(Message.fixColor("&cThere is not any active reports currently&7.")));
            return false;
        }

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("reports-system").getKeys(false)) {
            if (!key.equalsIgnoreCase(target.getName())) continue;

            return key.equalsIgnoreCase(target.getName());
        }

        return false;
    }
}
