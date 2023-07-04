package me.notro.staffutilities.listeners;

import me.notro.staffutilities.CustomConfig;
import me.notro.staffutilities.objects.Report;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.managers.VanishManager;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteractListener implements Listener {

    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();
    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final VanishManager vanishManager = StaffUtilities.getInstance().getVanishManager();
    private final Report report = StaffUtilities.getInstance().getReport();
    private final CustomConfig staffUtilsFile = StaffUtilities.getInstance().getStaffUtilsConfig();

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

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.displayName(onlinePlayer.displayName());

            itemStack.setItemMeta(skullMeta);
            guiManager.addMenuItem(itemStack);
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
            Bukkit.broadcast(Message.fixColor("&e" + event.getPlayer().getName() + " left the game"));
            return;
        }

        vanishManager.quitVanish(event.getPlayer());
        Bukkit.broadcast(Message.fixColor("&e" + event.getPlayer().getName() + " joined the game"));

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

        Component itemName = event.getItem().getItemMeta().displayName();
        guiManager.createMenu(event.getPlayer(), 36, Message.fixColor("&eTeleport"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&eTeleport"))) return;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.displayName(onlinePlayer.displayName());

            itemStack.setItemMeta(skullMeta);
            guiManager.addMenuItem(itemStack);
        }

        event.getPlayer().openInventory(guiManager.getInventory());
    }

    @EventHandler
    public void onPlayerPunish(PlayerInteractEvent event) {
        if (!staffModeManager.isInStaffMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();
        guiManager.createMenu(event.getPlayer(), 36, Message.fixColor("&4Punish"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&4Punish"))) return;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.displayName(onlinePlayer.displayName());

            itemStack.setItemMeta(skullMeta);;
            guiManager.addMenuItem(itemStack);
        }

        event.getPlayer().openInventory(guiManager.getInventory());
    }

    @EventHandler
    public void onPlayerOpenReport(PlayerInteractEvent event) {
        if (!staffModeManager.isInStaffMode(event.getPlayer())) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();
        guiManager.createMenu(event.getPlayer(), 36, Message.fixColor("&9Reports"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&9Reports"))) return;

        ItemStack itemStack = new ItemStack(Material.PAPER);

        if (staffUtilsFile.get().getConfigurationSection("reports-system") == null) {
            event.getPlayer().sendMessage(Message.fixColor("&cThere is not any active reports currently&7."));
            return;
        }

        for (String key : staffUtilsFile.get().getConfigurationSection("reports-system").getKeys(false)) {

            ConfigurationSection reportSection = staffUtilsFile.get().getConfigurationSection("reports-system." + key);
            if (reportSection == null) continue;

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Message.fixColor("&6" + key));

            List<Component> lore = new ArrayList<>();
            lore.add(Message.fixColor(reportSection.getString("reason")));
            lore.add(Message.fixColor("&e" + reportSection.getString("uuid")));
            itemMeta.lore(lore);

            itemStack.setItemMeta(itemMeta);
            guiManager.addMenuItem(itemStack);
        }

        event.getPlayer().openInventory(guiManager.getInventory());
    }
}