package me.notro.staffutilities.listeners;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
import java.util.List;

public class PlayerPunishListener implements Listener {

    private final StaffUtilities plugin;

    public PlayerPunishListener(StaffUtilities plugin) {
        this.plugin = plugin;
    }
    private OfflinePlayer whoToPunish;

    @EventHandler
    public void onPlayerPunish(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getStaffModeManager().isInStaffMode(player)) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;

        Component itemName = event.getItem().getItemMeta().displayName();
        plugin.getGuiManager().createMenu(player, 36, Message.fixColor("&4Punishments"));

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!itemName.equals(Message.fixColor("&4Punishments"))) return;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        if (plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system") == null) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cNo players are currently punished&7.")));
            return;
        }

        for (String key : plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system").getKeys(false)) {
            ConfigurationSection punishmentSection = plugin.getStaffUtilsConfig().get().getConfigurationSection("punishments-system." + key);
            if (punishmentSection == null) continue;

            skullMeta.setOwningPlayer(plugin.getPunishment().getTarget());
            skullMeta.displayName(Message.fixColor(key));

            List<Component> loreList = new ArrayList<>();
            loreList.add(Message.fixColor("&7Punishment: " + punishmentSection.getString("punishment")));
            loreList.add(Message.fixColor("&7UUID: &6" + punishmentSection.getString("uuid")));
            loreList.add(Message.fixColor("&7Reason: " + punishmentSection.getString("reason")));
            skullMeta.lore(loreList);

            itemStack.setItemMeta(skullMeta);
            plugin.getGuiManager().addMenuItem(itemStack);
        }

        if (plugin.getGuiManager().isEmpty()) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&cNo players are currently punished&7.")));
            return;
        }

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onPunishmentsMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null || slot.getType() != Material.PLAYER_HEAD) return;
        if (!event.getView().title().equals(Message.fixColor("&4Punishments"))) return;

        whoToPunish = event.getWhoClicked().getServer().getOfflinePlayer(LegacyComponentSerializer.legacySection().serialize(slot.hasItemMeta() ? slot.getItemMeta().displayName() : slot.displayName()));

        plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&cChoose punishment to revoke"));
        plugin.getGuiManager().setMenuItem(4, new ItemStack(Material.WOODEN_AXE), "&cBan");
        plugin.getGuiManager().setMenuItem(5, new ItemStack(Material.NAME_TAG), "&eMute");
        plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cCancel");

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onPunishmentsRevoke(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&cChoose punishment to revoke"))) return;

        switch (slot.getType()) {
            case WOODEN_AXE -> {
                plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&cConfirm Ban revoke"));
                plugin.getGuiManager().setMenuItem(0, new ItemStack(Material.LIME_CONCRETE), "&aConfirm");
                plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cDeny");

                player.openInventory(plugin.getGuiManager().getInventory());
            }

            case NAME_TAG -> {
                plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&cConfirm &eMute revoke"));
                plugin.getGuiManager().setMenuItem(0, new ItemStack(Material.LIME_CONCRETE), "&aConfirm");
                plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cDeny");

                player.openInventory(plugin.getGuiManager().getInventory());
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the punishments wizard&7.")));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onBanRevokeConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&cConfirm Ban revoke"))) return;

        switch (slot.getType()) {
            case LIME_CONCRETE -> {
                if (!plugin.getPunishmentManager().isBanned(whoToPunish)) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + whoToPunish.getName() + " &cis not banned&7.")));
                    return;
                }

                plugin.getPunishmentManager().executeUnban(player, whoToPunish);
                player.closeInventory();
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the punishments wizard&7.")));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onMuteRevokeConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&cConfirm &eMute revoke"))) return;

        switch (slot.getType()) {
            case LIME_CONCRETE -> {
                if (!plugin.getPunishmentManager().isMuted(whoToPunish)) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + whoToPunish.getName() + " &cis not muted&7.")));
                    return;
                }

                plugin.getPunishmentManager().executeUnmute(player, whoToPunish);
                player.closeInventory();
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the punishments wizard&7.")));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPunishMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&4Choose &6Punishment"))) return;

        switch (slot.getType()) {
            case WOODEN_AXE -> {
                plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&cChoose Ban reason"));
                plugin.getGuiManager().setMenuItem(3, new ItemStack(Material.NAME_TAG), "&eBan Evading");
                plugin.getGuiManager().setMenuItem(4, new ItemStack(Material.DIAMOND_SWORD), "&cCheating");
                plugin.getGuiManager().setMenuItem(5, new ItemStack(Material.LADDER), "&4Abusing bugs");
                plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cCancel");

                player.openInventory(plugin.getGuiManager().getInventory());
            }

            case NAME_TAG -> {
                plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&cChoose &eMute &creason"));
                plugin.getGuiManager().setMenuItem(3, new ItemStack(Material.NAME_TAG), "&eChat related");
                plugin.getGuiManager().setMenuItem(4, new ItemStack(Material.PAPER), "&cPersona related");
                plugin.getGuiManager().setMenuItem(5, new ItemStack(Material.LADDER), "&4Swearing/Cursing");
                plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cCancel");

                player.openInventory(plugin.getGuiManager().getInventory());
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the punishment wizard&7.")));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onBanMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&cChoose Ban reason"))) return;

        switch (slot.getType()) {
            case NAME_TAG -> {
                if (plugin.getPunishmentManager().hasBypass(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &ccannot be punished silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (plugin.getPunishmentManager().isBanned(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6 " + plugin.getPunishment().getTarget().getName() + " &cis already banned&7.")));
                    player.closeInventory();
                    return;
                }

                plugin.getPunishment().setReason("&eBan Evading");
            }

            case DIAMOND_SWORD -> {
                if (plugin.getPunishmentManager().hasBypass(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &ccannot be punished silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (plugin.getPunishmentManager().isBanned(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6 " + plugin.getPunishment().getTarget().getName() + " &cis already banned&7.")));
                    player.closeInventory();
                    return;
                }

                plugin.getPunishment().setReason("&cCheating");
            }

            case LADDER -> {
                if (plugin.getPunishmentManager().hasBypass(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &ccannot be punished silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (plugin.getPunishmentManager().isBanned(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6 " + plugin.getPunishment().getTarget().getName() + " &cis already banned&7.")));
                    player.closeInventory();
                    return;
                }

                plugin.getPunishment().setReason("&eAbusing bugs");
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.getPrefix().append(Message.fixColor("&cYou have cancelled the punishment wizard&7.")));
                player.closeInventory();
                return;
            }
        }

        plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&cConfirm ban"));
        plugin.getGuiManager().setMenuItem(0, new ItemStack(Material.LIME_CONCRETE), "&aConfirm");
        plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cDeny");

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onBanConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&cConfirm ban"))) return;

        switch (slot.getType()) {
            case LIME_CONCRETE -> {
                plugin.getPunishmentManager().executeBan(player, plugin.getPunishment().getTarget(), plugin.getPunishment().getReason());
                player.closeInventory();
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.fixColor("&cYou have cancelled the punishment wizard&7."));
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onMuteMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&cChoose &eMute &creason"))) return;

        switch (slot.getType()) {
            case NAME_TAG -> {
                if (plugin.getPunishmentManager().hasBypass(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &ccannot be punished silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (plugin.getPunishmentManager().isMuted(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &cis already muted&7.")));
                    player.closeInventory();
                    return;
                }

                plugin.getPunishment().setReason("&eChat related");
            }

            case PAPER -> {
                if (plugin.getPunishmentManager().hasBypass(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &ccannot be punished silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (plugin.getPunishmentManager().isMuted(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &cis already muted&7.")));
                    player.closeInventory();
                    return;
                }

                plugin.getPunishment().setReason("&cPersona related");
            }

            case LADDER -> {
                if (plugin.getPunishmentManager().hasBypass(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &ccannot be punished silly&7!")));
                    player.closeInventory();
                    return;
                }

                if (plugin.getPunishmentManager().isMuted(plugin.getPunishment().getTarget())) {
                    player.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + plugin.getPunishment().getTarget().getName() + " &cis already muted&7.")));
                    player.closeInventory();
                    return;
                }

                plugin.getPunishment().setReason("&4Swearing/Cursing");
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.fixColor("&cYou have cancelled the punishment wizard&7."));
                player.closeInventory();
                return;
            }
        }

        plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&cConfirm &emute"));
        plugin.getGuiManager().setMenuItem(0, new ItemStack(Material.LIME_CONCRETE), "&aConfirm");
        plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cDeny");

        player.openInventory(plugin.getGuiManager().getInventory());
    }

    @EventHandler
    public void onMuteConfirmation(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack slot = event.getInventory().getItem(event.getSlot());

        if (slot == null) return;
        if (!event.getView().title().equals(Message.fixColor("&cConfirm &emute"))) return;

        switch (slot.getType()) {
            case LIME_CONCRETE -> {
                plugin.getPunishmentManager().executeMute(player, plugin.getPunishment().getTarget(), plugin.getPunishment().getReason());
                player.closeInventory();
            }

            case RED_CONCRETE -> {
                player.sendMessage(Message.fixColor("&cYou have cancelled the punishment wizard&7."));
                player.closeInventory();
            }
        }
    }
}
