package me.notro.staffutilities.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.PunishmentManager;
import me.notro.staffutilities.objects.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class AsyncChatListener implements Listener {

    private final Punishment punishment = StaffUtilities.getInstance().getPunishment();
    private final PunishmentManager punishmentManager = StaffUtilities.getInstance().getPunishmentManager();
    private final HashMap<UUID, Punishment> reasonProvider = StaffUtilities.getInstance().getReasonProvider();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (!reasonProvider.containsKey(player.getUniqueId())) {
            punishment.setReason(event.signedMessage().message());
            punishmentManager.executeBan(player, punishment.getTarget(), punishment.getReason());
        }
    }
}
