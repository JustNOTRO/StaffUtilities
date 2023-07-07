package me.notro.staffutilities.listeners;

import me.notro.staffutilities.CustomConfig;
import me.notro.staffutilities.objects.Report;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.managers.ReportManager;
import me.notro.staffutilities.managers.StaffModeManager;
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

    private final ReportManager reportManager = StaffUtilities.getInstance().getReportManager();
    private final Report report = StaffUtilities.getInstance().getReport();
    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();
    private final CustomConfig staffUtilsFile = StaffUtilities.getInstance().getStaffUtilsConfig();

    @EventHandler
    public void onPlayerReport(InventoryClickEvent event) {
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&9Choose Reason"))) return;

        switch (slot.getType()) {
            case NAME_TAG -> {
                report.setReason("&eChat related");
                reportManager.createReport(report.getReporter(), report.getTarget(), report.getReason());
                report.getReporter().closeInventory();
            }

            case PAPER -> {
                report.setReason("&cPersona related");
                reportManager.createReport(report.getReporter(), report.getTarget(), report.getReason());
                report.getReporter().closeInventory();
            }

            case LEAD -> {
                report.setReason("&4Client/Server related");
                reportManager.createReport(report.getReporter(), report.getTarget(), report.getReason());
                report.getReporter().closeInventory();
            }

            case ANVIL -> {
                report.setReason("&cOther");
                reportManager.createReport(report.getReporter(), report.getTarget(), report.getReason());
                report.getReporter().closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerOpenReport(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!staffModeManager.isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();
        guiManager.createMenu(player, 36, Message.fixColor("&9Reports"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&9Reports"))) return;

        ItemStack itemStack = new ItemStack(Material.NAME_TAG);

        if (staffUtilsFile.get().getConfigurationSection("reports-system") == null) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cThere is not any active reports currently&7.")));
            return;
        }

        for (String key : staffUtilsFile.get().getConfigurationSection("reports-system").getKeys(false)) {
            ConfigurationSection reportSection = staffUtilsFile.get().getConfigurationSection("reports-system." + key);
            if (reportSection == null) continue;

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Message.fixColor("&6" + key));

            List<Component> lore = new ArrayList<>();
            lore.add(Message.fixColor(reportSection.getString("reason")));
            lore.add(Message.fixColor("&e" + reportSection.getString("uuid")));
            itemMeta.lore(lore);

            itemStack.setItemMeta(itemMeta);
            guiManager.addMenuItem(itemStack);
        }

        if (guiManager.isEmpty()) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cThere is not any active reports currently&7.")));
            return;
        }

        player.openInventory(guiManager.getInventory());
    }

    @EventHandler
    public void onStaffReport(InventoryClickEvent event) {
        Player staff = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (!staffModeManager.isInStaffMode(staff)) return;
        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&9Reports"))) return;

        guiManager.createMenu(staff, 36, Message.fixColor("&9Reports"));
        event.setCancelled(true);
    }
}
