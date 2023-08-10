package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerFlightListener implements Listener {

    private final StaffUtilities plugin;

    public PlayerFlightListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFlight(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&6Fly"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Component isEnabled = player.getAllowFlight() ? Message.fixColor("&7Flight is &cDisabled&7.") : Message.fixColor("&7Flight is &aEnabled&7.");

        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage(Message.getPrefix().append(isEnabled));
    }
}
