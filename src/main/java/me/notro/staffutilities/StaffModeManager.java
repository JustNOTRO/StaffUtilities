package me.notro.staffutilities;

import me.notro.staffutilities.utils.ItemBuilder;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class StaffModeManager {

    private final Player player;
    private final ConfigurationSection configurationSection;
    private final List<String> staffModeList;

    public StaffModeManager(Player player, ConfigurationSection configurationSection, List<String> staffModeList) {
        this.player = player;
        this.configurationSection = configurationSection;
        this.staffModeList = staffModeList;
    }

    public void joinStaffMode() {
        staffModeList.add(player.getUniqueId().toString());
        configurationSection.set("players", staffModeList);
        createStaffTool(Material.FEATHER, "&6Fly", 7);
        createStaffTool(Material.DIAMOND_HOE, "&bFreeze", 2);
        StaffUtilities.getInstance().saveConfig();
    }

    public void quitStaffMode() {
        staffModeList.remove(player.getUniqueId().toString());
        configurationSection.set("players", null);
        player.getInventory().clear();
        StaffUtilities.getInstance().saveConfig();
    }

    public boolean isInStaffMode() {
        return staffModeList.contains(player.getUniqueId().toString());
    }

    public void createStaffTool(Material material, String displayName, int index) {
        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.setDisplayName(displayName);

        if (player.getInventory().getItem(index) != null) {
            player.sendMessage(Message.fixColor("&cThis slot already contains an item&7."));
            return;
        }

        player.getInventory().setItem(index, itemBuilder.build());
    }
}
