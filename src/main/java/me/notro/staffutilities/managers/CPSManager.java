package me.notro.staffutilities.managers;

import lombok.NonNull;

import java.util.HashMap;
import java.util.UUID;

public class CPSManager {

    private final HashMap<UUID, Integer> cpsChecker;

    public CPSManager(HashMap<UUID, Integer> cpsChecker) {
        this.cpsChecker = cpsChecker;
    }

    public void addPlayer(@NonNull UUID uuid) {
        cpsChecker.put(uuid, 0);
    }

    public void removePlayer(@NonNull UUID uuid) {
        cpsChecker.remove(uuid);
    }

    public int getInt(@NonNull UUID uuid) {
        return cpsChecker.get(uuid);
    }

    public void updatePlayer(@NonNull UUID uuid) {
        cpsChecker.put(uuid, getInt(uuid) + 1);
    }

    public boolean containsKey(@NonNull UUID uuid) {
        return cpsChecker.containsKey(uuid);
    }
}
