package me.notro.staffutilities.listeners;

import me.notro.staffutilities.objects.Report;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.managers.ReportManager;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.utils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerReportListener implements Listener {

    private final ReportManager reportManager = StaffUtilities.getInstance().getReportManager();
    private final Report report = StaffUtilities.getInstance().getReport();
    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();

    @EventHandler
    public void onPlayerReport(InventoryClickEvent event) {
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&9Choose Reason"))) return;

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
    public void onStaffReport(InventoryClickEvent event) {
        Player staff = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (!staffModeManager.isInStaffMode(staff)) return;
        if (slot == null ) return;
        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&9Reports"))) return;

        guiManager.createMenu(staff, 36, Message.fixColor("&9Reports"));
    }
}
