package me.notro.staffutilities;

import lombok.Getter;
import me.notro.staffutilities.commands.*;
import me.notro.staffutilities.listeners.*;
import me.notro.staffutilities.managers.*;
import me.notro.staffutilities.objects.Punishment;
import me.notro.staffutilities.objects.Report;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@Getter
public final class StaffUtilities extends JavaPlugin {

    @Getter
    private static StaffUtilities instance;

    private StaffModeManager staffModeManager;
    private GUIManager guiManager;
    private VanishManager vanishManager;
    private FreezeManager freezeManager;
    private PunishmentManager punishmentManager;
    private ReportManager reportManager;
    private CPSManager cpsManager;
    private Report report;
    private Punishment punishment;
    private CustomConfig staffUtilsConfig;

    @Override
    public void onEnable() {
        instance = this;
        staffUtilsConfig = new CustomConfig(this, "staffutils");

        loadManagers();
        loadObjects();
        loadCommands();
        loadListeners();
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getLogger().info("has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled.");
    }

    private void loadManagers() {
        staffModeManager = new StaffModeManager(this);
        guiManager = new GUIManager();
        vanishManager = new VanishManager(this);
        freezeManager = new FreezeManager(this);
        punishmentManager = new PunishmentManager(this);
        reportManager = new ReportManager(this);
        cpsManager = new CPSManager(new HashMap<>());
    }

    private void loadObjects() {
        report = new Report();
        punishment = new Punishment();
    }

    private void loadCommands() {
        getCommand("staffmode").setExecutor(new StaffModeCommand(this));
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("reports").setExecutor(new ReportsCommand(this));
        getCommand("punish").setExecutor(new PunishCommand(this));
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this)   ;
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerReportListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerFreezeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerFlightListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerPunishListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerVanishListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCPSListener(this), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(this), this);
    }
}
