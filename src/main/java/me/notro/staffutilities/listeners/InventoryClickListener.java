package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.utils.ItemBuilder;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryClickListener implements Listener {

    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final ConfigurationSection freezeSection = StaffUtilities.getInstance().getConfig().getConfigurationSection("freeze");
    private final List<String> freezedPlayers = freezeSection.getStringList("freezed-players");
    private Player whoToFreeze;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&bFreeze"))) return;

        ItemStack slot = event.getInventory().getItem(event.getSlot());
        if (slot == null) return;
        if (slot.getType() != Material.PLAYER_HEAD) return;

        whoToFreeze = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToFreeze == null) {
            player.sendMessage(Message.NO_PLAYER_EXISTENCE);
            return;
        }

        Component isFrozen = freezedPlayers.contains(whoToFreeze.getUniqueId().toString()) ? Message.fixColor("&cUnfreeze &6" + whoToFreeze.getName()) : Message.fixColor("&cFreeze &6" + whoToFreeze.getName());

        guiManager.createMenu(player, 9, isFrozen);
        guiManager.setMenuItem(0, new ItemBuilder(Material.GREEN_WOOL).setDisplayName("&aConfirm"));
        guiManager.setMenuItem(8, new ItemBuilder(Material.RED_WOOL).setDisplayName("&cDeny"));
        player.openInventory(guiManager.getInventory());

        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText(isFrozen.toString()))) return;
    }

    @EventHandler
    public void onFreezeConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getView().getPlayer();
        ItemStack secondSlot = event.getInventory().getItem(event.getSlot());

        if (secondSlot == null) return;
        if (!secondSlot.getItemMeta().hasDisplayName()) return;

        switch (secondSlot.getType()) {
            case GREEN_WOOL -> {
                if (!freezedPlayers.contains(whoToFreeze.getUniqueId().toString())) {
                    freezedPlayers.add(whoToFreeze.getUniqueId().toString());
                    freezeSection.set("freezed-players", freezedPlayers);
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully freezed &6" + whoToFreeze.getName() + "&7.")));
                    StaffUtilities.getInstance().saveConfig();
                    player.closeInventory();
                    return;
                }
                freezedPlayers.remove(whoToFreeze.getUniqueId().toString());
                freezeSection.set("freezed-players", freezedPlayers);
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully unfreezed &6" + whoToFreeze.getName() + "&7.")));
                StaffUtilities.getInstance().saveConfig();
                player.closeInventory();
            }

            case RED_WOOL -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the freeze wizard&7.")));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (!event.getView().getOriginalTitle().equalsIgnoreCase(Message.fixText("&eTeleport"))) return;
        if (slot == null) return;
        if (slot.getType() != Material.PLAYER_HEAD) return;

        Player whoToTeleport = event.getWhoClicked().getServer().getPlayerExact(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        if (whoToTeleport == null) {
            player.sendMessage(Message.NO_PLAYER_EXISTENCE);
            return;
        }

        guiManager.createMenu(player, 36, Message.fixColor("&eTeleport"));

        player.teleport(whoToTeleport.getLocation());
        player.closeInventory();
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully teleported to &6" + whoToTeleport.getName() + "&7.")));
    }
}
