package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class FreezeManager {

    private final StaffUtilities plugin;
    private final ConfigurationSection freezeSection;
    private final List<String> freezedPlayers;

    public FreezeManager() {
        this.plugin = StaffUtilities.getInstance();
        this.freezeSection = plugin.getConfig().getConfigurationSection("freeze");
        this.freezedPlayers = freezeSection.getStringList("freezed-players");
    }

    public void executeFreeze(@NonNull Player player) {
        freezedPlayers.add(player.getUniqueId().toString());
        freezeSection.set("freezed-players", freezedPlayers);
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully freezed &6" + player.getName() + "&7.")));
        plugin.saveConfig();
    }

    public void executeUnfreeze(@NonNull Player player) {
        freezedPlayers.remove(player.getUniqueId().toString());
        freezeSection.set("freezed-players", freezedPlayers);
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully unfreezed &6" + player.getName() + "&7.")));
        plugin.saveConfig();
    }

    public boolean hasBypass(@NonNull Player player) {
        return player.hasPermission("staffutils.freeze.bypass");
    }

    public boolean isFrozen(@NonNull Player player) {
        return freezedPlayers.contains(player.getUniqueId().toString());
    }
}
