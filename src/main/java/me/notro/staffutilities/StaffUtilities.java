package me.notro.staffutilities;

import lombok.Getter;
import me.notro.staffutilities.commands.ReportCommand;
import me.notro.staffutilities.commands.StaffChatCommand;
import me.notro.staffutilities.commands.StaffModeCommand;
import me.notro.staffutilities.commands.StaffReportsCommand;
import me.notro.staffutilities.listeners.*;
import me.notro.staffutilities.managers.*;
import me.notro.staffutilities.objects.Report;
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
    private PunishmentManager punishmentManager;

    @Getter
    private FreezeManager freezeManager;

    @Getter
    private ReportManager reportManager;

    @Getter
    private Report report;

    @Getter
    private CustomConfig staffUtilsConfig;

    @Override
    public void onEnable() {
        instance = this;
        staffUtilsConfig = new CustomConfig(instance, "staff");

        loadManagers();
        loadObjects();
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getLogger().info("has been enabled.");

        getCommand("staffmode").setExecutor(new StaffModeCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new StaffReportsCommand());

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerReportListener(), this);
        }

    @Override
    public void onDisable() {
        getLogger().info("has been disabled.");
    }

    private void loadManagers() {
        staffModeManager = new StaffModeManager();
        guiManager = new GUIManager();
        vanishManager = new VanishManager();
        punishmentManager = new PunishmentManager();
        freezeManager = new FreezeManager();
        reportManager = new ReportManager();
    }

    private void loadObjects() {
        report = new Report();
    }
}
