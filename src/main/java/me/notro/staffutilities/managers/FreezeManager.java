package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
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

    public void executeFreeze(@NonNull Player staff, @NonNull Player target) {
        freezedPlayers.add(target.getUniqueId().toString());
        freezeSection.set("freezed-players", freezedPlayers);

        if (target.isFlying()) target.setFlySpeed(0.0F);
        target.setWalkSpeed(0.0F);

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully freezed &6" + target.getName() + "&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7froze &6" + target.getName() + "&7.")), "staffutils.staff.notify");
        plugin.saveConfig();
    }

    public void executeUnfreeze(@NonNull Player staff, @NonNull Player target) {
        freezedPlayers.remove(target.getUniqueId().toString());
        freezeSection.set("freezed-players", freezedPlayers);

        if (target.isFlying()) target.setFlySpeed(0.1F);
        target.setWalkSpeed(0.2F);

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully unfreezed &6" + target.getName() + "&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7unfroze &6" + target.getName() + "&7.")), "staffutils.staff.notify");
        plugin.saveConfig();
    }

    public boolean hasBypass(@NonNull Player player) {
        return player.hasPermission("staffutils.freeze.bypass");
    }

    public boolean isFrozen(@NonNull Player player) {
        return freezedPlayers.contains(player.getUniqueId().toString());
    }
}
