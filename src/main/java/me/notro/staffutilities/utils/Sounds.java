package me.notro.staffutilities.utils;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sounds {

    public static void playSound(CommandSender sender, Sound sound, float volume, float pitch) {
        if (!(sender instanceof Player player)) return;
        if (volume <= 0 ) return;

        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
