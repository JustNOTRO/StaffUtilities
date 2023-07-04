package me.notro.staffutilities.managers;

import lombok.NonNull;
import me.notro.staffutilities.objects.Punishment;
import me.notro.staffutilities.StaffUtilities;
import me.notro.staffutilities.utils.Message;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PunishmentManager {

    private final StaffUtilities plugin;
    private final ConfigurationSection banSection;
    private final List<String> bannedPlayers;
    private final BanList banList;

    private final HashMap<UUID, Punishment> reasonProvider = new HashMap<>();

    public PunishmentManager() {
        this.plugin = StaffUtilities.getInstance();
        this.banSection = plugin.getConfig().getConfigurationSection("punishments.ban");
        this.bannedPlayers = banSection.getStringList("banned-players");
        this.banList = Bukkit.getBanList(BanList.Type.NAME);
    }

    public void executeBan(@NonNull Player player ,@NonNull Player target, @NonNull String reason) {
        Punishment punishment = new Punishment(player.getName(), target.getUniqueId());
        player.sendTitlePart(TitlePart.TITLE, Message.fixColor("&cType reason in chat"));
        reasonProvider.put(target.getUniqueId(), punishment);

        bannedPlayers.add(target.getUniqueId().toString());
        banSection.set("banned-players", bannedPlayers);
        banList.addBan(target.getUniqueId().toString(), reason, null, null);

        target.kick(Message.fixColor("&7You have been banned for the reason: " + reason + "&7."));
        player.sendMessage(Message.getPrefix().append(Message.fixColor("&7Successfully banned &6" + target.getName() + "&7.")));
        plugin.saveConfig();
    }

    public void executeUnban(@NonNull Player target) {
        reasonProvider.remove(target.getUniqueId());
        bannedPlayers.remove(target.getUniqueId().toString());
        banSection.set("banned-players", bannedPlayers);
        banList.pardon(target.getUniqueId().toString());
        plugin.saveConfig();
    }

    public boolean hasBypass(@NonNull Player player) {
        return player.hasPermission("staffutils.punishments.ban.bypass");
    }
    public boolean isBanned(@NonNull Player player) {
        return banList.isBanned(player.getUniqueId().toString());
    }
}
