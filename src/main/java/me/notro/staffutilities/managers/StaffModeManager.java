package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StaffModeManager {

    private final ConfigurationSection configurationSection;
    private final List<String> staffModeList;

    public StaffModeManager() {
        this.configurationSection = StaffUtilities.getInstance().getConfig().getConfigurationSection("staff-mode");
        this.staffModeList = configurationSection.getStringList("players");
    }

    public void joinStaffMode(@NonNull Player player) {
        staffModeList.add(player.getUniqueId().toString());
        createStaffTool(player, Material.FEATHER, "&6Fly", 7);
        createStaffTool(player, Material.DIAMOND_HOE, "&bFreeze", 2);
        configurationSection.set("players", staffModeList);
        StaffUtilities.getInstance().saveConfig();
    }

    public void quitStaffMode(@NonNull Player player) {
        staffModeList.remove(player.getUniqueId().toString());
        player.getInventory().clear();
        configurationSection.set("players", null);
        StaffUtilities.getInstance().saveConfig();
    }

    public boolean isInStaffMode(@NonNull Player player) {
        return staffModeList.contains(player.getUniqueId().toString());
    }

    private void createStaffTool(@NonNull Player staff, @NonNull Material material, @NonNull String displayName, int index) {
        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.setDisplayName(displayName);

        staff.getInventory().setItem(index, itemBuilder.build());
    }

    private void restoreItems(@NonNull Player player) {
        ItemStack[] itemRestorer = player.getInventory().getContents();

        for (ItemStack content : itemRestorer) {
            if (content == null) return;
            player.getInventory().addItem(content);
        }
    }
}
