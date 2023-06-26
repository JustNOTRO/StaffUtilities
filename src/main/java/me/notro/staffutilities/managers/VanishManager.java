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
        this.vanishSection = StaffUtilities.getInstance().getConfig().getConfigurationSection("vanish");
        this.vanishedPlayers = vanishSection.getStringList("vanished-players");
    }

    public void joinVanish(@NonNull Player player) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> player.hasPermission("staffutils.vanish"))
                .forEach(players -> players.hidePlayer(plugin, player));
        vanishedPlayers.add(player.getUniqueId().toString());
        vanishSection.set("players", vanishedPlayers );
        plugin.saveConfig();
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7You are now &aVanished&7.")));
    }

    public void quitVanish(@NonNull Player player) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> player.hasPermission("staffutils.vanish"))
                .forEach(players -> players.showPlayer(plugin, player));
        vanishedPlayers.remove(player.getUniqueId().toString());
        vanishSection.set("players", vanishedPlayers);
        plugin.saveConfig();
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7You are now &cUnvanished&7.")));
    }

    public boolean isVanished(@NonNull Player player) {
        return vanishedPlayers.contains(player.getUniqueId().toString());
    }

    public boolean hasBypass(@NonNull Player player) {
        return player.hasPermission("staffutils.vanish.bypass");
    }
}
