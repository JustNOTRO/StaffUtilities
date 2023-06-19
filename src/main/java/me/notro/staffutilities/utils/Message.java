package me.notro.staffutilities.utils;

import lombok.Getter;
import org.bukkit.ChatColor;

public class Message {

    public static final String NO_SENDER_EXECUTOR = ("&cOnly players can execute this command&7.");
    public static final String NO_PERMISSION = fixColor("&cYou don't have permission to execute this command&7.");
    public static final String NO_PLAYER_EXISTENCE = fixColor("&cdoes not exist/online&7.");

    public static String fixColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Getter
    private static final String prefix = fixColor("&8[&6Staff Mode&8] ");
}
