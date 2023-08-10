package me.notro.staffutilities.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {

    private final StaffUtilities plugin;

    public AsyncChatListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (plugin.getPunishmentManager().isMuted(player)) {
            event.setCancelled(true);
            player.sendMessage(Message.fixColor("&cYou have been muted by &6" + plugin.getPunishment().getRequester().getName() + "&7."));
        }
    }
}
