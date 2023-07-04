package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.CustomConfig;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ReportManager {
    private final CustomConfig staffUtilsFile = StaffUtilities.getInstance().getStaffUtilsConfig();

    public void createReport(@NonNull Player reporter, @NonNull OfflinePlayer target, @NonNull String reason) {
        if (reporter == target) {
            reporter.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou can't report yourself&7.")));
            return;
        }

        staffUtilsFile.get().set("reports-system." + target.getName() + ".reason", reason);
        staffUtilsFile.get().set("reports-system." + target.getName() + ".uuid", target.getUniqueId().toString());

        reporter.sendMessage(Message.fixColor("&7Successfully reported &6" + target.getName() + " &7for " + reason + "&7."));
        staffUtilsFile.save();
    }

    public void clearReport(@NonNull Player staff, @NonNull OfflinePlayer target) {
        staffUtilsFile.get().set("reports-system." + target.getName(), null);
        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully removed the report(s) on &6" + target.getName() + "&7.")));
        staffUtilsFile.save();
    }

    public void clearReports(@NonNull Player staff) {
        staffUtilsFile.get().set("reports-system", null);
        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully cleared all reports.")));
        staffUtilsFile.save();
    }

    public boolean hasPermission(@NonNull Player player) {
        return player.hasPermission("staffutils.report.use");
    }

    public boolean hasReports(@NonNull Player staff, @NonNull OfflinePlayer target) {
        if (staffUtilsFile.get().getConfigurationSection("reports-system") == null) {
            staff.sendMessage(Message.getPrefix().append(Message.fixColor("&cThere is not any active reports currently&7.")));
            return false;
        }

        for (String key : staffUtilsFile.get().getConfigurationSection("reports-system").getKeys(false)) {
            ConfigurationSection reportSection = staffUtilsFile.get().getConfigurationSection("reports-system" + key);

            if (staffUtilsFile.get().getConfigurationSection("reports-system") == null) return false;
            if (reportSection == null) return false;

            return reportSection.contains(key);
        }

        return false;
    }
}
