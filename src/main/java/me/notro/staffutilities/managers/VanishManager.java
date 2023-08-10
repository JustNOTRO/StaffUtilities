package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishManager {

    private final StaffUtilities plugin;

    public VanishManager(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    public void joinVanish(@NonNull Player staff) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> staff.hasPermission("staffutils.vanish"))
                .forEach(players -> players.hidePlayer(plugin, staff));

        plugin.getStaffUtilsConfig().get().set("vanish-system." + staff.getName() + ".uuid", staff.getUniqueId().toString());
        plugin.getStaffUtilsConfig().save();

        staff.setInvulnerable(true);
        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7You are now &aVanished&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7has &aEnabled &7vanish mode.")), "staffutils.staff.notify");
    }

    public void quitVanish(@NonNull Player staff) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> staff.hasPermission("staffutils.vanish"))
                .forEach(players -> players.showPlayer(plugin, staff));

        plugin.getStaffUtilsConfig().get().set("vanish-system." + staff.getName(), null);
        plugin.getStaffUtilsConfig().save();

        staff.setInvulnerable(false);
        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7You are now &cUnvanished&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7has &cDisabled &7vanish mode.")), "staffutils.staff.notify");
    }

    public boolean isVanished(@NonNull Player staff) {
        if (plugin.getStaffUtilsConfig().get().getConfigurationSection("vanish-system." + staff.getName()) == null) return false;

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("vanish-system").getKeys(false)) {
            if (!key.equalsIgnoreCase(staff.getName())) continue;

            return key.equalsIgnoreCase(staff.getName());
        }

        return false;
    }

    public boolean hasBypass(@NonNull Player player) {
        return player.hasPermission("staffutils.vanish.bypass");
    }
}
