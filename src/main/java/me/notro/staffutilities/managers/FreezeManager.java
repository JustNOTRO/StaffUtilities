package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FreezeManager {

    private final StaffUtilities plugin;

    public FreezeManager(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    public void executeFreeze(@NonNull Player staff, @NonNull Player target) {
        plugin.getStaffUtilsConfig().get().set("freeze-system." + target.getName() + ".uuid", target.getUniqueId().toString());
        plugin.getStaffUtilsConfig().save();

        if (target.isFlying()) target.setFlySpeed(0.0F);
        target.setWalkSpeed(0.0F);

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully freezed &6" + target.getName() + "&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7froze &6" + target.getName() + "&7.")), "staffutils.staff.notify");
    }

    public void executeUnfreeze(@NonNull Player staff, @NonNull Player target) {
        plugin.getStaffUtilsConfig().get().set("freeze-system." + target.getName(), null);
        plugin.getStaffUtilsConfig().save();

        if (target.isFlying()) target.setFlySpeed(0.1F);
        target.setWalkSpeed(0.2F);

        staff.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully unfreezed &6" + target.getName() + "&7.")));
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7unfroze &6" + target.getName() + "&7.")), "staffutils.staff.notify");
    }

    public boolean hasBypass(@NonNull Player target) {
        return target.hasPermission("staffutils.freeze.bypass");
    }

    public boolean isFrozen(@NonNull Player target) {
        if (plugin.getStaffUtilsConfig().get().getConfigurationSection("freeze-system." + target.getName()) == null) return false;

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("freeze-system").getKeys(false)) {
            if (!key.equalsIgnoreCase(target.getName())) continue;

            return key.equalsIgnoreCase(target.getName());
        }

        return false;
    }
}
