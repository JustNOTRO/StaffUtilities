package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final StaffUtilities plugin;

    public PlayerJoinListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getVanishManager().isVanished(player)) event.joinMessage(null);
    }
}
