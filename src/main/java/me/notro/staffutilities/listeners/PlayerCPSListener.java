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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerCPSListener implements Listener {


    private final StaffUtilities plugin;

    public PlayerCPSListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }
    private Player whoToCheck;

    @EventHandler
    public void onPlayerOpenMenu(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        plugin.getGuiManager().createMenu(player, 36, Message.fixColor("&eCPS Manager"));
        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&eCPS Checker"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.displayName(Message.fixColor(onlinePlayer.getName()));

            itemStack.setItemMeta(skullMeta);
            plugin.getGuiManager().addMenuItem(itemStack);
        }

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (slot.getType() != Material.PLAYER_HEAD) return;
        if (!event.getView().title().equals(Message.fixColor("&eCPS Manager"))) return;

       whoToCheck = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToCheck == null) {
            player.sendMessage(Message.getPrefix().append(Message.NO_PLAYER_EXISTENCE));
            return;
        }

        player.sendMessage(Message.getPrefix().append(Message.fixColor("&cStarted checking &6" + whoToCheck.getName() + "&7.")));
        plugin.getCpsManager().addPlayer(whoToCheck.getUniqueId());
        player.closeInventory();

        new BukkitRunnable() {
            @Override
            public void run() {
                int cps = (int) (plugin.getCpsManager().getInt(whoToCheck.getUniqueId()) / 10 * 20 / 20L);
                String state = cps > 15 ? "&7(&cdangerous&7)" : "&7(&aprobably not dangerous&7)";

                plugin.getCpsManager().removePlayer(whoToCheck.getUniqueId());
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + player.getName() + " &7has about &6" + cps + " cps " + state + ".")));
            }
        }.runTaskLater(plugin, 10 * 20);
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Action action = event.getAction();

        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!plugin.getCpsManager().containsKey(uuid)) return;
            plugin.getCpsManager().updatePlayer(uuid);
        }
    }
}
