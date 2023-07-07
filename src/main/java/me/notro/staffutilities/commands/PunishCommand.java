package me.notro.staffutilities.commands;

import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.objects.Punishment;
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


    private final Punishment punishment = StaffUtilities.getInstance().getPunishment();
    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();

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
        punishment.setTarget(target);
        punishment.setRequester(player);

        guiManager.createMenu(player, 9, Message.fixColor("&4Punish &6" + target.getName()));
        guiManager.setMenuItem(0, new ItemStack(Material.LIME_CONCRETE), "&aConfirm");
        guiManager.setMenuItem(8, new ItemStack(Material.RED_CONCRETE), "&cDeny");

        player.openInventory(guiManager.getInventory());
        return true;
    }
}
