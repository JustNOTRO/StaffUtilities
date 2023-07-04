package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
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

    public void joinStaffMode(@NonNull Player player) {
        staffModeList.add(player.getUniqueId().toString());
        configurationSection.set("players", staffModeList);
        loadStaffTools(player);
        plugin.saveConfig();
    }

    public void quitStaffMode(@NonNull Player player) {
        staffModeList.remove(player.getUniqueId().toString());
        configurationSection.set("players", staffModeList);
        player.getInventory().clear();
        plugin.saveConfig();
    }

    public boolean isInStaffMode(@NonNull Player player) {
        return staffModeList.contains(player.getUniqueId().toString());
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
        createStaffTool(staff, Material.STONE_AXE, "&4Punish", 0);
    }
}
