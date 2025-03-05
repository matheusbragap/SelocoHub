package com.matheusdev.selocoplugin.selecoHub.commands;

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

    public PluginReload(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        // Se o arquivo config.yml não existir, copia o padrão
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
            sender.sendMessage("§aArquivo config.yml restaurado com sucesso!");
        } else {
            // Corrige valores ausentes no config.yml
            if (fixMissingConfigValues(configFile)) {
                sender.sendMessage("§aConfig.yml corrigido e reorganizado!");
            }
        }

        // Recarrega as configurações
        plugin.reloadConfig();
        sender.sendMessage("§aConfigurações do plugin recarregadas com sucesso!");
    }

    private boolean fixMissingConfigValues(File configFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        boolean changed = false;

        // Carrega o config.yml padrão do projeto (do diretório resources)
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(plugin.getResource("config.yml"), StandardCharsets.UTF_8)
        );

        // Percorre todas as chaves do config.yml padrão
        for (String key : defaultConfig.getKeys(true)) {
            // Verifica se a chave está ausente ou vazia no config.yml atual
            if (!config.contains(key) || (config.get(key) instanceof String && ((String) config.get(key)).isEmpty())) {
                // Define o valor padrão
                config.set(key, defaultConfig.get(key));
                changed = true;
            }
        }

        // Se algo foi alterado, salva o arquivo
        if (changed) {
            try {
                config.save(configFile);
                Bukkit.getLogger().info("Config.yml atualizado! Valores ausentes foram preenchidos.");
            } catch (IOException e) {
                Bukkit.getLogger().warning("Erro ao salvar config.yml!");
            }
        }

        return changed;
    }
}