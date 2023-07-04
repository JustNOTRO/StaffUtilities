package me.notro.staffutilities.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.notro.staffutilities.objects.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class AsyncChatListener implements Listener {

    private final HashMap<UUID, Punishment> reasonProvider = new HashMap<>();
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Punishment punishment = new Punishment(player.getName(), player.getUniqueId());

        punishment.setReason(event.message().examinableName());
        reasonProvider.put(player.getUniqueId(), punishment);
    }
}
