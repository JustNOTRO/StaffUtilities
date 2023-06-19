package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.ItemBuilder;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ConfigurationSection staffModeSection = StaffUtilities.getInstance().getConfig().getConfigurationSection("staff-mode");
        List<String> staffModeList = staffModeSection.getStringList("players");

        ItemBuilder flyItem = new ItemBuilder(Material.FEATHER);
        flyItem.setDisplayName("&6Fly");

        if (!staffModeList.contains(player.getUniqueId().toString())) return;
        if (!player.getInventory().getItemInMainHand().equals(flyItem.build())) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        String isEnabled = player.getAllowFlight() ? Message.fixColor("&cFlight is disabled&7.") : Message.fixColor("&aFlight is enabled&7.");

        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage(Message.getPrefix() + isEnabled);
    }
}
