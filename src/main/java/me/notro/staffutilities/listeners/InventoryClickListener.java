package me.notro.staffutilities.listeners;

import me.notro.staffutilities.objects.Punishment;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.PunishmentManager;
import me.notro.staffutilities.managers.FreezeManager;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class InventoryClickListener implements Listener {

    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final PunishmentManager punishmentManager = StaffUtilities.getInstance().getPunishmentManager();
    private final FreezeManager freezeManager = StaffUtilities.getInstance().getFreezeManager();
    private Player whoToFreeze;
    private Player whoToPunish;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (slot.getType() != Material.PLAYER_HEAD) return;
        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&bFreeze"))) return;

        whoToFreeze = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToFreeze == null) {
            player.sendMessage(Message.NO_PLAYER_EXISTENCE);
            return;
        }

        guiManager.createMenu(player, 9, Message.fixColor("&bFreeze &6" + whoToFreeze.getName()));
        guiManager.setMenuItem(0, new ItemStack(Material.GREEN_WOOL), "&aConfirm");
        guiManager.setMenuItem(8, new ItemStack(Material.RED_WOOL), "&cDeny");

        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&bFreeze &6" + whoToFreeze.getName()))) player.openInventory(guiManager.getInventory());
    }

    @EventHandler
    public void onFreezeConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getView().getPlayer();
        ItemStack secondSlot = event.getInventory().getItem(event.getSlot());

        if (secondSlot == null) return;
        if (!secondSlot.getItemMeta().hasDisplayName()) return;

        switch (secondSlot.getType()) {
            case GREEN_WOOL -> {
                if (freezeManager.hasBypass(whoToFreeze)) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + whoToFreeze.getName() + " &ccannot be freezed silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (!freezeManager.isFrozen(whoToFreeze)) {
                    freezeManager.executeFreeze(whoToFreeze);
                    player.closeInventory();
                    return;
                }

                freezeManager.executeUnfreeze(whoToFreeze);
                player.closeInventory();
            }

            case RED_WOOL -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the freeze wizard&7.")));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerPunish(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (slot.getType() != Material.PLAYER_HEAD) return;
        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&4Punish"))) return;

        whoToPunish = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToPunish == null) {
            player.sendMessage(Message.NO_PLAYER_EXISTENCE);
            return;
        }

        guiManager.createMenu(player, 9, Message.fixColor("&4Ban " + whoToPunish.getName()));
        guiManager.setMenuItem(0, new ItemStack(Material.LIME_CONCRETE), "&aConfirm");
        guiManager.setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cDeny");

        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&4Ban " + whoToPunish.getName()))) player.openInventory(guiManager.getInventory());
    }

    @EventHandler
    public void onPlayerPunishConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!slot.getItemMeta().hasDisplayName()) return;

        switch (slot.getType()) {
            case LIME_CONCRETE -> {
                Punishment punishment = new Punishment(player.getName(), whoToPunish.getUniqueId());

                if (punishmentManager.hasBypass(whoToPunish)) {
                    player.closeInventory();
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + whoToPunish.getName() + " &ccannot be punished silly&7!")));
                    return;
                }

                if (punishmentManager.isBanned(whoToPunish)) {
                    player.closeInventory();
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + whoToPunish.getName() + " &cis already banned&7.")));
                    return;
                }

                player.closeInventory();
                punishmentManager.executeBan(player, whoToPunish, punishment.getReason());
            }

            case RED_CONCRETE -> {
                player.closeInventory();
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the punishment wizard&7.")));
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (slot.getType() != Material.PLAYER_HEAD) return;

        Player whoToTeleport = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToTeleport == null) {
            player.sendMessage(Message.NO_PLAYER_EXISTENCE);
            return;
        }

        guiManager.createMenu(player, 9, Message.fixColor("&eTeleport"));
        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&eTeleport"))) return;

        player.teleport(whoToTeleport.getLocation());
        player.closeInventory();
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully teleported to &6" + whoToTeleport.getName() + "&7.")));
    }
}
