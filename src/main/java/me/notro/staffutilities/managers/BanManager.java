package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class BanManager {

    private final ConfigurationSection banSection;
    private final List<String> bannedPlayers;
    private final BanList banList;
    private final StaffUtilities plugin;

    public BanManager() {
        this.banSection = StaffUtilities.getInstance().getConfig().getConfigurationSection("punishments.ban");
        this.bannedPlayers = banSection.getStringList("banned-players");
        this.banList = Bukkit.getBanList(BanList.Type.NAME);
        this.plugin = StaffUtilities.getInstance();
    }

    public void executeBan(@NonNull Player target, String reason) {
        bannedPlayers.add(target.getUniqueId().toString());
        banSection.set("banned-players", bannedPlayers);
        banList.addBan(target.getUniqueId().toString(), reason, null, null);

        if (reason == null) {
            target.kick(Message.fixColor("&cThe Ban hammer has been spoken&7."));
            return;
        }

        target.kick(Message.fixColor("&7You have been banned for the reason: " + reason + "&7."));
        plugin.saveConfig();
    }

    public void executeUnban(@NonNull Player target) {
        bannedPlayers.remove(target.getUniqueId().toString());
        banSection.set("banned-players", bannedPlayers);
        banList.pardon(target.getUniqueId().toString());
        plugin.saveConfig();
    }

    public boolean hasBypass(@NonNull Player player) {
        return player.hasPermission("staffutils.punishments.ban.bypass");
    }
    public boolean isBanned(@NonNull Player player) {
        return banList.isBanned(player.getUniqueId().toString());
    }
}
