package me.notro.staffutilities;

import lombok.Getter;
import me.notro.staffutilities.commands.StaffChatCommand;
import me.notro.staffutilities.commands.StaffModeCommand;
import me.notro.staffutilities.listeners.*;
import me.notro.staffutilities.managers.BanManager;
import me.notro.staffutilities.managers.GUIManager;
import me.notro.staffutilities.managers.StaffModeManager;
import me.notro.staffutilities.managers.VanishManager;
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

    @Getter
    private BanManager banManager;

    @Override
    public void onEnable() {
        instance = this;
        staffModeManager = new StaffModeManager();
        guiManager = new GUIManager();
        vanishManager = new VanishManager();
        banManager = new BanManager();

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
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled.");
    }
}
