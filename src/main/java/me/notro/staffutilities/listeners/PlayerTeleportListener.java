package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerTeleportListener implements Listener {

    private final StaffUtilities plugin;

    public PlayerTeleportListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleportMenu(PlayerInteractEvent event) {
        Player player = event.getPlayer();;

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();
        plugin.getGuiManager().createMenu(player, 36, Message.fixColor("&eTeleport"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&eTeleport"))) return;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.displayName(onlinePlayer.displayName());

            itemStack.setItemMeta(skullMeta);
            plugin.getGuiManager().addMenuItem(itemStack);
        }

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onPlayerTeleport(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (!event.getView().title().equals(Message.fixColor("&eTeleport"))) return;
        if (slot == null || slot.getType() != Material.PLAYER_HEAD) return;

        Player whoToTeleport = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToTeleport == null) {
            player.sendMessage(Message.NO_PLAYER_EXISTENCE);
            return;
        }

        player.teleport(whoToTeleport.getLocation());
        player.closeInventory();
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully teleported to &6" + whoToTeleport.getName() + "&7.")));
    }
}