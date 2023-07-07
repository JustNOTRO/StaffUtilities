package me.notro.staffutilities;

import me.notro.staffutilities.utils.Message;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private final File file;
    private FileConfiguration configuration;

    public CustomConfig(StaffUtilities plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        this.configuration = new YamlConfiguration();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name + ".yml", false);
        }

        configuration = new YamlConfiguration();

        try {
            configuration.load(file);
        } catch (InvalidConfigurationException | IOException exception) {
            throw new RuntimeException("The config could not be loaded");
        }
    }

    public FileConfiguration get() {
        return configuration;
    }

    public void reloadConfig() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            throw new RuntimeException("The file could not be saved");
        }
    }

    public String getString(String path) {
        if (!get().isSet(path)) return Message.fixColor("&cError path not found&7.").examinableName();

        return get().getString(path);
    }
}
