package me.notro.staffutilities;

import lombok.Getter;
import me.notro.staffutilities.commands.StaffChatCommand;
import me.notro.staffutilities.commands.StaffModeCommand;
import me.notro.staffutilities.listeners.InventoryClickListener;
import me.notro.staffutilities.listeners.PlayerInteractListener;
import me.notro.staffutilities.listeners.PlayerMoveListener;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.managers.StaffModeManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class StaffUtilities extends JavaPlugin {

    @Getter
    private static StaffUtilities instance;

    @Getter
    private StaffModeManager staffModeManager;

    @Getter
    private GUIManager guiManager;

    @Getter
    private VanishManager vanishManager;

    @Override
    public void onEnable() {
        instance = this;
        staffModeManager = new StaffModeManager();
        guiManager = new GUIManager();
        vanishManager = new VanishManager();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getLogger().info("has been enabled.");

        // Commands
        getCommand("staffmode").setExecutor(new StaffModeCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand());

        //Listeners
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled.");
    }
}
