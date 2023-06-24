package me.notro.staffutilities.utils;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public class Message {

    public static final Component
            NO_SENDER_EXECUTOR = fixColor("&cOnly players can execute this command&7."),
            NO_PERMISSION = fixColor("&cYou don't have permission to execute this command&7."),
            NO_PLAYER_EXISTENCE = fixColor("&cdoes not exist/online&7.");

    public static Component fixColor(@NonNull String message) {
        return LegacyComponentSerializer.legacy('&').deserialize(message);
    }

    public static String fixText(@NonNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Getter
    private static final Component prefix = fixColor("&8[&6Staff Mode&8] &7>>> ");
}
