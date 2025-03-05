package com.matheusdev.selocoplugin.selecoHub.commands;

import com.matheusdev.selocoplugin.selecoHub.listeners.JumpPads;
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

    public PluginReload(JavaPlugin plugin, JumpPads jumpPads) {
        this.plugin = plugin;
        this.jumpPads = jumpPads;
    }

    public void execute(CommandSender sender, boolean overwriteChanges) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        // Se o arquivo config.yml não existir, copia o padrão
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
            sender.sendMessage("§aArquivo config.yml restaurado com sucesso!");
        } else {
            // Corrige valores ausentes ou inválidos no config.yml
            if (fixMissingConfigValues(configFile, overwriteChanges)) {
                sender.sendMessage("§aConfig.yml corrigido e reorganizado!");
            }
        }

        // Recarrega as configurações
        plugin.reloadConfig();
        jumpPads.reloadConfig(); // Recarrega as configurações dos JumpPads
        sender.sendMessage("§aConfigurações do plugin recarregadas com sucesso!");
    }

    private boolean fixMissingConfigValues(File configFile, boolean overwriteChanges) {
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
            } else if (overwriteChanges) {
                // Verifica se o valor atual é inválido (incompleto ou incorreto)
                Object currentValue = config.get(key);
                Object defaultValue = defaultConfig.get(key);

                // Se o tipo do valor atual for diferente do tipo do valor padrão, corrige
                if (currentValue != null && defaultValue != null && !currentValue.getClass().equals(defaultValue.getClass())) {
                    config.set(key, defaultValue);
                    changed = true;
                }

                // Verifica valores específicos (por exemplo, booleanos incompletos)
                if (defaultValue instanceof Boolean) {
                    // Se o valor atual for uma string que representa um booleano incompleto
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

        // Se algo foi alterado, salva o arquivo
        if (changed) {
            try {
                // Salva o arquivo com a formatação correta
                config.save(configFile);
                Bukkit.getLogger().info("Config.yml atualizado! Valores ausentes ou inválidos foram corrigidos.");
            } catch (IOException e) {
                Bukkit.getLogger().warning("Erro ao salvar config.yml!");
            }
        }

        return changed;
    }
}