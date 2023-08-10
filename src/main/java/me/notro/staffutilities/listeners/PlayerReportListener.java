package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerReportListener implements Listener {

    private final StaffUtilities plugin;

    public PlayerReportListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerReport(InventoryClickEvent event) {
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&9Choose Reason"))) return;

        switch (slot.getType()) {
            case NAME_TAG -> {
                plugin.getReport().setReason("&eChat related");
                plugin.getReportManager().createReport(plugin.getReport().getReporter(), plugin.getReport().getTarget(), plugin.getReport().getReason());
                plugin.getReport().getReporter().closeInventory();
            }

            case PAPER -> {
                plugin.getReport().setReason("&cPersona related");
                plugin.getReportManager().createReport(plugin.getReport().getReporter(), plugin.getReport().getTarget(), plugin.getReport().getReason());
                plugin.getReport().getReporter().closeInventory();
            }

            case LEAD -> {
                plugin.getReport().setReason("&4Client/Server related");
                plugin.getReportManager().createReport(plugin.getReport().getReporter(), plugin.getReport().getTarget(), plugin.getReport().getReason());
                plugin.getReport().getReporter().closeInventory();
            }

            case ANVIL -> {
                plugin.getReport().setReason("&cOther");
                plugin.getReportManager().createReport(plugin.getReport().getReporter(), plugin.getReport().getTarget(), plugin.getReport().getReason());
                plugin.getReport().getReporter().closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerOpenReport(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();
        plugin.getGuiManager().createMenu(player, 36, Message.fixColor("&9Reports"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&9Reports"))) return;

        ItemStack itemStack = new ItemStack(Material.NAME_TAG);

        if (plugin.getStaffUtilsConfig().get().getConfigurationSection("reports-system") == null) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cThere is not any active reports currently&7.")));
            return;
        }

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("reports-system").getKeys(false)) {
            ConfigurationSection reportSection = plugin.getStaffUtilsConfig().get().getConfigurationSection("reports-system." + key);
            if (reportSection == null) continue;

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Message.fixColor("&e" + key));

            List<Component> loreList = new ArrayList<>();
            loreList.add(Message.fixColor("&7Reporter: &6" + reportSection.getString("reporter")));
            loreList.add(Message.fixColor("&7UUID: &6" + reportSection.getString("uuid")));
            loreList.add(Message.fixColor("&7Reason: " + reportSection.getString("reason")));
            itemMeta.lore(loreList);

            itemStack.setItemMeta(itemMeta);
            plugin.getGuiManager().addMenuItem(itemStack);
        }

        if (plugin.getGuiManager().isEmpty()) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cThere is not any active reports currently&7.")));
            return;
        }

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onStaffReport(InventoryClickEvent event) {
        Player staff = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (!plugin.getStaffModeManager().isInStaffMode(staff)) return;
        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&9Reports"))) return;

        plugin.getGuiManager().createMenu(staff, 36, Message.fixColor("&9Reports"));
        event.setCancelled(true);
    }
}
