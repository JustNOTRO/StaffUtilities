package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StaffModeManager {

    private final StaffUtilities plugin;

    public StaffModeManager(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    public void joinStaffMode(@NonNull Player staff) {
        plugin.getStaffUtilsConfig().get().set("staff-mode." + staff.getName() + ".uuid", staff.getUniqueId().toString());
        plugin.getStaffUtilsConfig().save();

        saveItems(staff);
        staff.getInventory().clear();
        loadStaffTools(staff);
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7joined staff mode.")), "staffutils.staff.notify");
    }

    public void quitStaffMode(@NonNull Player staff) {
        plugin.getStaffUtilsConfig().get().set("staff-mode." + staff.getName(), null);
        plugin.getStaffUtilsConfig().save();
        
        staff.getInventory().clear();
        loadItems(staff);
        Bukkit.broadcast(Message.getPrefix().append(Message.fixColor("&6" + staff.getName() + " &7quit staff mode.")), "staffutils.staff.notify");
    }

    public boolean isInStaffMode(@NonNull Player staff) {
        if (plugin.getStaffUtilsConfig().get().getConfigurationSection("staff-mode." + staff.getName()) == null) return false;

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("staff-mode").getKeys(false)) {
            if (!key.equalsIgnoreCase(staff.getName())) continue;
            
            return key.equalsIgnoreCase(staff.getName());
        }
        
        return false;
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
        createStaffTool(staff, Material.CLOCK, "&eCPS Checker", 4);
        createStaffTool(staff, Material.PAPER, "&9Reports", 3);
        createStaffTool(staff, Material.DIAMOND_HOE, "&bFreeze", 2);
        createStaffTool(staff, Material.ARROW, "&cVanish", 1);
        createStaffTool(staff, Material.STONE_AXE, "&4Punishments", 0);
    }
    
    private void saveItems(@NonNull Player staff) {
        if (staff.getInventory().isEmpty() && plugin.getStaffUtilsConfig().get().getList("items." + staff.getName()) == null) {
            plugin.getStaffUtilsConfig().get().set("items." + staff.getName(), null);
            return;
        }

        ItemStack[] stacks = staff.getInventory().getStorageContents();
        List<ItemStack> items = new ArrayList<>();

        for (ItemStack stack : stacks) {
            if (stack == null || stack.getType() == Material.AIR) continue;

            items.add(stack);
            plugin.getStaffUtilsConfig().get().set("items." + staff.getName(), items);
            plugin.getStaffUtilsConfig().save();
        }
    }

    private void loadItems(@NonNull Player staff) {
        List<ItemStack> stacks = (List<ItemStack>) plugin.getStaffUtilsConfig().get().getList("items." + staff.getName());
        if (stacks == null) return;

        staff.getInventory().setStorageContents(stacks.toArray(new ItemStack[0]));
    }
}
