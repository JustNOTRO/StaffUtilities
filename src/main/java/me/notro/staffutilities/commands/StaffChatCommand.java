package me.notro.staffutilities.commands;

import me.notro.staffutilities.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.NO_SENDER_EXECUTOR);
            return false;
        }

        if (!player.hasPermission("staffutils.staffchat.use")) {
            player.sendMessage(Message.NO_PERMISSION);
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(Message.getPrefix().append(Message.fixColor("&c/&7" + label + " &c<&7text&c>")));
            return false;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) message.append(arg).append(" ");

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> players.hasPermission("staffutils.staffchat.see"))
                .forEach(players -> players.sendMessage(Message.getPrefix().append(Message.fixColor("&6" + player.getName() + "&7: " + message))));
        return true;
    }
}
