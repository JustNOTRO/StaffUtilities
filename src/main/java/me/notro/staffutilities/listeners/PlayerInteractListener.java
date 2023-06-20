package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!staffModeManager.isInStaffMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&6Fly"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Component isEnabled = event.getPlayer().getAllowFlight() ? Message.fixColor("&cFlight is disabled&7.") : Message.fixColor("&aFlight is enabled&7.");

        event.getPlayer().setAllowFlight(!event.getPlayer().getAllowFlight());
        event.getPlayer().sendMessage(Message.getPrefix().append(isEnabled));
    }
}
