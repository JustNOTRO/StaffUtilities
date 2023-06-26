package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ConfigurationSection freezeSection = StaffUtilities.getInstance().getConfig().getConfigurationSection("freeze");
        List<String> freezedPlayers = freezeSection.getStringList("freezed-players");

        if (freezedPlayers.contains(player.getUniqueId().toString())) event.setCancelled(true);
    }
}
