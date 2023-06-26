package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.managers.VanishManager;
import me.notro.staffutilities.utils.ItemBuilder;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerInteractListener implements Listener {

    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();
    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final VanishManager vanishManager = StaffUtilities.getInstance().getVanishManager();

    @EventHandler
    public void onPlayerFlight(PlayerInteractEvent event) {
        if (!staffModeManager.isInStaffMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&6Fly"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Component isEnabled = event.getPlayer().getAllowFlight() ? Message.fixColor("&7Flight is &cdisabled&7.") : Message.fixColor("&7Flight is &aenabled&7.");

        event.getPlayer().setAllowFlight(!event.getPlayer().getAllowFlight());
        event.getPlayer().sendMessage(Message.getPrefix().append(isEnabled));
    }

    @EventHandler
    public void onPlayerFreeze(PlayerInteractEvent event) {
        if (!staffModeManager.isInStaffMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        guiManager.createMenu(event.getPlayer(), 27, Message.fixColor("&bFreeze"));
        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&bFreeze"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        ItemBuilder itemBuilder = new ItemBuilder(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemBuilder.getItemMeta();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (skullMeta.hasOwner()) return;

            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.displayName(onlinePlayer.displayName());

            itemBuilder.setItemMeta(skullMeta);
            guiManager.addMenuItem(itemBuilder);
        }

        event.getPlayer().openInventory(guiManager.getInventory());
    }

    @EventHandler
    public void onPlayerVanish(PlayerInteractEvent event) {
        if (!staffModeManager.isInStaffMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&cVanish"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (!vanishManager.isVanished(event.getPlayer())) {
            vanishManager.joinVanish(event.getPlayer());
            return;
        }

        vanishManager.quitVanish(event.getPlayer());

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(vanishManager::hasBypass)
                .forEach(players -> players.showPlayer(StaffUtilities.getInstance(), event.getPlayer()));
    }

    @EventHandler
    public void onPlayerTeleport(PlayerInteractEvent event) {
        if (!staffModeManager.isInStaffMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        guiManager.createMenu(event.getPlayer(), 36, Message.fixColor("&eTeleport"));
        Component itemName = event.getItem().getItemMeta().displayName();

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&eTeleport"))) return;

        ItemBuilder itemBuilder = new ItemBuilder(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemBuilder.getItemMeta();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (skullMeta.hasOwner()) return;

            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.displayName(onlinePlayer.displayName());

            itemBuilder.setItemMeta(skullMeta);
            guiManager.addMenuItem(itemBuilder);
        }

        event.getPlayer().openInventory(guiManager.getInventory());
    }
}