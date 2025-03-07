package com.matheusdev.selocoplugin.selecoHub.commands;

import com.matheusdev.selocoplugin.selecoHub.listeners.JumpPads;
import com.matheusdev.selocoplugin.selecoHub.listeners.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PluginReload {
    private final JavaPlugin plugin;
    private final JumpPads jumpPads;
    private final GameSettings gameSettings;

    public PluginReload(JavaPlugin plugin, JumpPads jumpPads, GameSettings gameSettings) {
        this.plugin = plugin;
        this.jumpPads = jumpPads;
        this.gameSettings = gameSettings;
    }

    public void execute(CommandSender sender, boolean overwriteChanges) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
            sender.sendMessage("§aArquivo config.yml restaurado com sucesso!");
        } else {
            if (fixMissingConfigValues(configFile, overwriteChanges)) {
                sender.sendMessage("§aConfig.yml corrigido e reorganizado!");
            }
        }

        // Recarrega as configurações do plugin
        plugin.reloadConfig();

        // Recarrega as configurações dos JumpPads e GameSettings
        jumpPads.reloadConfig();
        gameSettings.reloadConfig();

        sender.sendMessage("§aConfigurações do plugin recarregadas com sucesso!");
    }

    private boolean fixMissingConfigValues(File configFile, boolean overwriteChanges) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        boolean changed = false;

        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(plugin.getResource("config.yml"), StandardCharsets.UTF_8)
        );

        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key) || (config.get(key) instanceof String && ((String) config.get(key)).isEmpty())) {
                config.set(key, defaultConfig.get(key));
                changed = true;
            } else if (overwriteChanges) {
                Object currentValue = config.get(key);
                Object defaultValue = defaultConfig.get(key);

                if (currentValue != null && defaultValue != null && !currentValue.getClass().equals(defaultValue.getClass())) {
                    config.set(key, defaultValue);
                    changed = true;
                }

                if (defaultValue instanceof Boolean) {
                    if (currentValue instanceof String) {
                        String currentValueStr = ((String) currentValue).toLowerCase();
                        if (!currentValueStr.equals("true") && !currentValueStr.equals("false")) {
                            config.set(key, defaultValue);
                            changed = true;
                        }
                    }
                }
            }
        }

        if (changed) {
            try {
                config.save(configFile);
                Bukkit.getLogger().info("Config.yml atualizado! Valores ausentes ou inválidos foram corrigidos.");
            } catch (IOException e) {
                Bukkit.getLogger().warning("Erro ao salvar config.yml!");
            }
        }

        return changed;
    }
}