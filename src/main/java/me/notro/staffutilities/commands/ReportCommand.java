package me.notro.staffutilities.commands;

import me.notro.staffutilities.objects.Report;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.managers.GUIManager;
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

    private final GUIManager guiManager = StaffUtilities.getInstance().getGuiManager();
    private final Report report = StaffUtilities.getInstance().getReport();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.NO_SENDER_EXECUTOR);
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&c/" + label + " &c<&7player&c>")));
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        report.setTarget(target);
        report.setReporter(player);

        if (!target.hasPlayedBefore()) {
            player.sendMessage(Message.fixColor("&6" + target.getName() + " &cnever played this server before&7."));
            return false;
        }

        guiManager.createMenu(player, 9, Message.fixColor("&9Choose Reason"));
        guiManager.setMenuItem(3, new ItemStack(Material.NAME_TAG), "&eChat related");
        guiManager.setMenuItem(4, new ItemStack(Material.PAPER),"&cPersona related");
        guiManager.setMenuItem(5, new ItemStack(Material.LEAD),"&4Client/Server related");
        guiManager.setMenuItem(8, new ItemStack(Material.ANVIL),"&cOther");

        player.openInventory(guiManager.getInventory());
        return true;
    }
}
