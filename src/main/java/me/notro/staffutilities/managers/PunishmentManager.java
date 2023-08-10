package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PunishmentManager {

    private final StaffUtilities plugin;

    public PunishmentManager(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    public void executeBan(@NonNull Player staff, OfflinePlayer target, @NonNull String reason) {
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName() + ".reason", reason);
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName() + ".punishment", "Ban");
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName() + ".uuid", target.getUniqueId().toString());
        plugin.getStaffUtilsConfig().save();

        if (target.isOnline()) {
            Player onlineTarget = target.getPlayer();

            if (onlineTarget == null) {
                staff.sendMessage(Message.getPrefix().append(Message.NO_PLAYER_EXISTENCE));
                return;
            }

            onlineTarget.kick(Message.fixColor("&cYou have been banned for the reason &6" + reason + "&7."));
        }

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully banned &6" + target.getName() + " &7for " + reason + "&7.")));
    }

    public void executeUnban(@NonNull Player staff, OfflinePlayer target) {
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName(), null);
        plugin.getStaffUtilsConfig().save();

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully unbanned &6" + target.getName() + "&7.")));
    }

    public boolean isBanned(OfflinePlayer target) {
        ConfigurationSection banSection = plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system." + target.getName());
        if (banSection == null || !banSection.getString("punishment").equalsIgnoreCase("Ban")) return false;

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system").getKeys(false)) {
            if (!key.equalsIgnoreCase(target.getName())) continue;

            return key.equalsIgnoreCase(target.getName());
        }

        return false;
    }

    public void executeMute(@NonNull Player staff, OfflinePlayer target, @NonNull String reason) {
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName() + ".punishment", "Mute");
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName() + ".reason", reason);
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName() + ".uuid", target.getUniqueId().toString());
        plugin.getStaffUtilsConfig().save();

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully muted &6" + target.getName() + " &7for " + reason + "&7.")));
    }

    public void executeUnmute(@NonNull Player staff, OfflinePlayer target) {
        plugin.getStaffUtilsConfig().get().set("punishments-system." + target.getName(), null);
        plugin.getStaffUtilsConfig().save();

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully unmuted &6" + target.getName() + "&7.")));
    }

    public boolean isMuted(OfflinePlayer target) {
        ConfigurationSection muteSection = plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system." + target.getName());
        if (muteSection == null || !muteSection.getString("punishment").equalsIgnoreCase("Mute")) return false;

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system").getKeys(false)) {
            if (!key.equalsIgnoreCase(target.getName())) continue;

            return key.equalsIgnoreCase(target.getName());
        }

        return false;
    }

    public boolean hasBypass(OfflinePlayer target) {
        Player onlineTarget = target.getPlayer();
        if (onlineTarget == null) return false;

       return onlineTarget.hasPermission("staffutils.punishments.bypass");
    }
}
