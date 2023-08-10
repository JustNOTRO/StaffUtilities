package me.notro.staffutilities.commands;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PunishCommand implements CommandExecutor {


    private final StaffUtilities plugin;

    public PunishCommand(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.NO_SENDER_EXECUTOR);
            return false;
        }

        if (!player.hasPermission("staffutils.punishments.use")) {
            player.sendMessage(Message.NO_PERMISSION);
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(Message.fixColor("&c/&7" + label + "&c<&7player&c>"));
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        plugin.getPunishment().setTarget(target);
        plugin.getPunishment().setRequester(player);

        plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&4Choose &6Punishment"));
        plugin.getGuiManager().setMenuItem(4, new ItemStack(Material.WOODEN_AXE), "&cBan");
        plugin.getGuiManager().setMenuItem(5, new ItemStack(Material.NAME_TAG), "&eMute");
        plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cCancel");

        player.openInventory(plugin.getGuiManager().getInventory());
        return true;
    }
}
