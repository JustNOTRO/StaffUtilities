package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.CustomConfig;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.objects.Punishment;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PunishmentManager {

    private final CustomConfig staffUtilsFile = StaffUtilities.getInstance().getStaffUtilsConfig();
    private final HashMap<UUID, Punishment> reasonProvider = StaffUtilities.getInstance().getReasonProvider();
    private final Punishment punishment = StaffUtilities.getInstance().getPunishment();

    public void executeBan(@NonNull Player staff, @NonNull OfflinePlayer target, @NonNull String reason) {
        reasonProvider.put(target.getUniqueId(), punishment);

        staffUtilsFile.get().set("punishments-system." + target.getName() + ".reason", reason);
        staffUtilsFile.get().set("punishments-system." + target.getName() + ".uuid", target.getUniqueId().toString());
        staffUtilsFile.save();

        if (target.isOnline()) {
            Player onlineTarget = target.getPlayer();

            if (onlineTarget == null) {
                staff.sendMessage(Message.getPrefix().append(Message.NO_PLAYER_EXISTENCE));
                return;
            }

            onlineTarget.kick(Message.fixColor("&cYou have been banned for the reason &6" + reason + "&7."));
        }

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully banned &6" + target.getName() + "&7.")));
    }

    public void executeUnban(@NonNull Player staff, @NonNull OfflinePlayer target) {
        reasonProvider.remove(target.getUniqueId());

        staffUtilsFile.get().set("punishments-system." + target.getName(), null);
        staffUtilsFile.save();

        staff.sendMessage(Message.fixColor("&7Successfully unbanned &6" + target.getName() + "&7."));
    }

    public boolean isBanned(@NonNull Player staff, @NonNull OfflinePlayer target) {
        if (staffUtilsFile.get().getConfigurationSection("punishments-system") == null) {
            staff.sendMessage(Message.getPrefix().append(Message.fixColor("&cNo players are currently punished&7.")));
            staff.closeInventory();
            return false;
        }

        for (String key : staffUtilsFile.get().getConfigurationSection("punishments-system").getKeys(false))
            return key.equalsIgnoreCase(target.getName());

        return false;
    }

    public boolean hasBypass(@NonNull OfflinePlayer target) {
        if (target.isOnline()) {
            Player onlineTarget = target.getPlayer();
            if (onlineTarget == null) return false;

           return onlineTarget.hasPermission("staffutils.punishments.bypass");
        }
        return false;
    }
}
