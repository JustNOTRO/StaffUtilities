package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class VanishManager {

    private final StaffUtilities plugin;
    private final ConfigurationSection vanishSection;
    private final List<String> vanishedPlayers;

    public VanishManager() {
        this.plugin = StaffUtilities.getInstance();
        this.vanishSection = plugin.getConfig().getConfigurationSection("vanish");
        this.vanishedPlayers = vanishSection.getStringList("vanished-players");
    }

    public void joinVanish(@NonNull Player staff) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> staff.hasPermission("staffutils.vanish"))
                .forEach(players -> players.hidePlayer(plugin, staff));

        vanishedPlayers.add(staff.getUniqueId().toString());
        vanishSection.set("players", vanishedPlayers);

        staff.setInvulnerable(true);
        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7You are now &aVanished&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7has &aEnabled &7vanish mode.")), "staffutils.staff.notify");
        plugin.saveConfig();
    }

    public void quitVanish(@NonNull Player staff) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> staff.hasPermission("staffutils.vanish"))
                .forEach(players -> players.showPlayer(plugin, staff));

        vanishedPlayers.remove(staff.getUniqueId().toString());
        vanishSection.set("players", vanishedPlayers);

        staff.setInvulnerable(false);
        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7You are now &cUnvanished&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7has &cDisabled &7vanish mode.")), "staffutils.staff.notify");
        plugin.saveConfig();
    }

    public boolean isVanished(@NonNull Player staff) {
        return vanishedPlayers.contains(staff.getUniqueId().toString());
    }

    public boolean hasBypass(@NonNull Player player) {
        return player.hasPermission("staffutils.vanish.bypass");
    }
}
