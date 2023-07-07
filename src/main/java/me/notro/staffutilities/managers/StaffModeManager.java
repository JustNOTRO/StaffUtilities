package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class StaffModeManager {

    private final StaffUtilities plugin;
    private final ConfigurationSection configurationSection;
    private final List<String> staffModeList;

    public StaffModeManager() {
        this.plugin = StaffUtilities.getInstance();
        this.configurationSection = plugin.getConfig().getConfigurationSection("staff-mode");
        this.staffModeList = configurationSection.getStringList("players");
    }

    public void joinStaffMode(@NonNull Player staff) {
        staffModeList.add(staff.getUniqueId().toString());
        configurationSection.set("players", staffModeList);
        loadStaffTools(staff);
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7joined staff mode.")), "staffutils.staff.notify");
        plugin.saveConfig();
    }

    public void quitStaffMode(@NonNull Player staff) {
        staffModeList.remove(staff.getUniqueId().toString());
        configurationSection.set("players", staffModeList);
        staff.getInventory().clear();
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7quit staff mode.")), "staffutils.staff.notify");
        plugin.saveConfig();
    }

    public boolean isInStaffMode(@NonNull Player staff) {
        return staffModeList.contains(staff.getUniqueId().toString());
    }

    private void createStaffTool(@NonNull Player staff, @NonNull Material material, @NonNull String displayName, int index) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(Message.fixColor(displayName));
        itemStack.setItemMeta(itemMeta);

        staff.getInventory().setItem(index, itemStack);
    }

    private void loadStaffTools(@NonNull Player staff) {
        createStaffTool(staff, Material.FEATHER, "&6Fly", 7);
        createStaffTool(staff, Material.BLAZE_ROD, "&eTeleport", 6);
        createStaffTool(staff, Material.PAPER, "&9Reports", 3);
        createStaffTool(staff, Material.DIAMOND_HOE, "&bFreeze", 2);
        createStaffTool(staff, Material.ARROW, "&cVanish", 1);
        createStaffTool(staff, Material.STONE_AXE, "&4Punishments", 0);
    }
}
