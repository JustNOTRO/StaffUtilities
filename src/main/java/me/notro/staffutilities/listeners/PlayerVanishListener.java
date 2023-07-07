package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.managers.VanishManager;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerVanishListener implements Listener {

    private final VanishManager vanishManager = StaffUtilities.getInstance().getVanishManager();
    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();

    @EventHandler
    public void onPlayerVanish(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!staffModeManager.isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&cVanish"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (!vanishManager.isVanished(player)) {
            vanishManager.joinVanish(player);
            Bukkit.broadcast(Message.fixColor("&e" + player.getName() + " left the game"));
            return;
        }

        vanishManager.quitVanish(player);
        Bukkit.broadcast(Message.fixColor("&e" + player.getName() + " joined the game"));

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(vanishManager::hasBypass)
                .forEach(players -> players.showPlayer(StaffUtilities.getInstance(), player));
    }
}
