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

public class ReportCommand implements CommandExecutor {

    private final StaffUtilities plugin;

    public ReportCommand(StaffUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.NO_SENDER_EXECUTOR);
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&c/&7" + label + " &c<&7player&c>")));
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        plugin.getReport().setTarget(target);
        plugin.getReport().setReporter(player);

        if (!target.hasPlayedBefore()) {
            player.sendMessage(Message.fixColor("&6" + target.getName() + " &cnever played this server before&7."));
            return false;
        }

        plugin.getGuiManager().createMenu(player, 9, Message.fixColor("&9Choose Reason"));
        plugin.getGuiManager().setMenuItem(3, new ItemStack(Material.NAME_TAG), "&eChat related");
        plugin.getGuiManager().setMenuItem(4, new ItemStack(Material.PAPER),"&cPersona related");
        plugin.getGuiManager().setMenuItem(5, new ItemStack(Material.LEAD),"&4Client/Server related");
        plugin.getGuiManager().setMenuItem(8, new ItemStack(Material.ANVIL),"&cOther");

        player.openInventory(plugin.getGuiManager().getInventory());
        return true;
    }
}
