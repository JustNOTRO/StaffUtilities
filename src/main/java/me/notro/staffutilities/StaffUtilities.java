package me.notro.staffutilities;

import lombok.Getter;
import me.notro.staffutilities.commands.StaffModeCommand;
import me.notro.staffutilities.listeners.PlayerInteractListener;
import me.notro.staffutilities.managers.StaffModeManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class StaffUtilities extends JavaPlugin {

    @Getter
    private static StaffUtilities instance;

    @Getter
    private StaffModeManager staffModeManager;

    @Override
    public void onEnable() {
        instance = this;

        staffModeManager = new StaffModeManager();
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getLogger().info("has been enabled.");

        // Commands
        getCommand("staffmode").setExecutor(new StaffModeCommand());

        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled.");
    }
}
