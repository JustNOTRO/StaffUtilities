package me.notro.staffutilities;

import lombok.Getter;
import me.notro.staffutilities.commands.*;
import me.notro.staffutilities.listeners.*;
import me.notro.staffutilities.managers.*;
import me.notro.staffutilities.objects.Punishment;
import me.notro.staffutilities.objects.Report;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

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
    private FreezeManager freezeManager;

    @Getter
    private PunishmentManager punishmentManager;

    @Getter
    private ReportManager reportManager;

    @Getter
    private Report report;

    @Getter
    private Punishment punishment;

    @Getter
    private HashMap<UUID, Punishment> reasonProvider;

    @Getter
    private CustomConfig staffUtilsConfig;

    @Override
    public void onEnable() {
        instance = this;
        staffUtilsConfig = new CustomConfig(instance, "staffutils");

        loadManagers();
        loadObjects();
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getLogger().info("has been enabled.");

        getCommand("staffmode").setExecutor(new StaffModeCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ReportsCommand());
        getCommand("punish").setExecutor(new PunishCommand());

        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerReportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFreezeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFlightListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPunishListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerVanishListener(), this);
        }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled.");
    }

    private void loadManagers() {
        staffModeManager = new StaffModeManager();
        guiManager = new GUIManager();
        vanishManager = new VanishManager();
        freezeManager = new FreezeManager();
        punishmentManager = new PunishmentManager();
        reportManager = new ReportManager();
    }

    private void loadObjects() {
        report = new Report();
        reasonProvider = new HashMap<>();
        punishment = new Punishment();
    }
}
