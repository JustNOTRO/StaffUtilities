package me.notro.staffutilities.objects;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter
@Setter
@NonNull
public class Punishment {

    private Player requester;
    private OfflinePlayer target;
    private String reason;
}
