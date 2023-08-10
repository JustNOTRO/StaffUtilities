package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {
    private final StaffUtilities plugin;

    public AsyncPlayerPreLoginListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(event.getUniqueId().toString()));
        ConfigurationSection banSection = plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system." + target.getName());

        if (plugin.getPunishmentManager().isBanned(target)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Message.fixColor("&cYou have been banned for the reason " + banSection.getString("reason") + "&7."));
            return;
        }

        event.allow();
    }
}
