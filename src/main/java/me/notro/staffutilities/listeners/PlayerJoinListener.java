package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.VanishManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final VanishManager vanishManager = StaffUtilities.getInstance().getVanishManager();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (vanishManager.isVanished(player)) event.joinMessage(null);
    }
}
