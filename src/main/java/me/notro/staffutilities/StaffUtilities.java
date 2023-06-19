package me.notro.staffutilities;

import lombok.Getter;
import lombok.Setter;
import me.notro.staffutilities.commands.StaffModeCommand;
import me.notro.staffutilities.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class StaffUtilities extends JavaPlugin {

    @Getter
    @Setter
    private static StaffUtilities instance;

    @Override
    public void onEnable() {
        setInstance(this);
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
