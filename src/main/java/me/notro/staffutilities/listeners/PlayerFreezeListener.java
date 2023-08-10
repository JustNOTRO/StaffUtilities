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

public class PlayerFreezeListener implements Listener {

    private final StaffUtilities plugin;
    private Player whoToFreeze;

    public PlayerFreezeListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFreeze(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        plugin.getGuiManager().createMenu(player, 36, Message.fixColor("&bFreeze"));
        Component itemName = event.getItem().getItemMeta().displayName();

        if (!itemName.equals(Message.fixColor("&bFreeze"))) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

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
    public void onPlayerClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (slot.getType() != Material.PLAYER_HEAD) return;
        if (!event.getView().title().equals(Message.fixColor("&bFreeze"))) return;

        whoToFreeze = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToFreeze == null) {
            player.sendMessage(Message.getPrefix().append(Message.NO_PLAYER_EXISTENCE));
            return;
        }

        plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&aConfirm &bFreeze"));
        plugin.getGuiManager().setMenuItem(0, new ItemStack(Material.GREEN_WOOL), "&aConfirm");
        plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_WOOL), "&cDeny");

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onFreezeConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getView().getPlayer();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&aConfirm &bFreeze"))) return;

        switch (slot.getType()) {
            case GREEN_WOOL -> {
                if (plugin.getFreezeManager().hasBypass(whoToFreeze)) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + whoToFreeze.getName() + " &ccannot be freezed silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (!plugin.getFreezeManager().isFrozen(whoToFreeze)) {
                    plugin.getFreezeManager().executeFreeze(player, whoToFreeze);
                    player.closeInventory();
                    return;
                }

                plugin.getFreezeManager().executeUnfreeze(player, whoToFreeze);
                player.closeInventory();
            }

            case RED_WOOL -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the freeze wizard&7.")));
                player.closeInventory();
            }
        }
    }
}
