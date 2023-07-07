package me.notro.staffutilities.listeners;

import me.notro.staffutilities.CustomConfig;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.managers.PunishmentManager;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.objects.Punishment;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerPunishListener implements Listener {

    private final StaffModeManager staffModeManager = StaffUtilities.getInstance().getStaffModeManager();
    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final CustomConfig staffUtilsFile = StaffUtilities.getInstance().getStaffUtilsConfig();
    private final PunishmentManager punishmentManager = StaffUtilities.getInstance().getPunishmentManager();
    private final Punishment punishment = StaffUtilities.getInstance().getPunishment();
    private final HashMap<UUID, Punishment> reasonProvider = StaffUtilities.getInstance().getReasonProvider();

    @EventHandler
    public void onPlayerPunish(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!staffModeManager.isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();
        guiManager.createMenu(player, 36, Message.fixColor("&4Punishments"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&4Punishments"))) return;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        if (staffUtilsFile.get().getConfigurationSection("punishments-system") == null) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cNo players are currently punished&7.")));
            return;
        }

        for (String key : staffUtilsFile.get().getConfigurationSection("punishments-system").getKeys(false)) {
            ConfigurationSection punishmentSection = staffUtilsFile.get().getConfigurationSection("punishments-system" + key);
            if (punishmentSection == null) continue;

            skullMeta.setOwningPlayer(punishment.getTarget());
            skullMeta.displayName(Message.fixColor("&e" + key));

            List<Component> loreList = new ArrayList<>();
            loreList.add(Message.fixColor("&e" + reasonProvider.get(punishment.getTarget().getUniqueId())));
            loreList.add(Message.fixColor("&b" + punishment.getTarget().getUniqueId()));
            skullMeta.lore(loreList);

            itemStack.setItemMeta(skullMeta);
        }

        if (guiManager.isEmpty()) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cNo players are currently punished&7.")));
            return;
        }

        player.openInventory(guiManager.getInventory());
    }

    @EventHandler
    public void onPunishConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&4Punish &6" + punishment.getTarget().getName()))) return;

        switch (slot.getType()) {
            case LIME_CONCRETE -> {
                if (punishmentManager.hasBypass(punishment.getTarget())) {
                    punishment.getRequester().sendMessage(Message.getPrefix().append(Message.fixColor("&6" + punishment.getTarget().getName() + " &ccannot be punished silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (punishmentManager.isBanned(player ,punishment.getTarget())) {
                    punishment.getRequester().sendMessage(Message.getPrefix().append(Message.fixColor("&6" + punishment.getTarget().getName() + " &cis already banned&7.")));
                    player.closeInventory();
                    return;
                }

                player.sendTitlePart(TitlePart.TITLE, Message.fixColor("&cType reason in chat"));
                player.closeInventory();
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the punishment table&7.")));
                player.closeInventory();
            }
        }
    }
}
