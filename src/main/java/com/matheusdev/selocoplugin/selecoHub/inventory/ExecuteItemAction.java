package com.matheusdev.selocoplugin.selecoHub.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ExecuteItemAction {
    private final JavaPlugin plugin;

    public ExecuteItemAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player, String action) {
        if (action == null || action.isEmpty()) {
            player.sendMessage("§cNenhuma ação definida para este item.");
            return;
        }

        // Substitui placeholders como %player% pelo nome do jogador
        action = action.replace("%player%", player.getName());

        // Verifica o tipo de ação com base no prefixo
        if (action.startsWith("[console]")) {
            executeConsoleCommand(action);
        } else if (action.startsWith("[player]")) {
            executePlayerCommand(player, action);
        } else if (action.startsWith("[message]")) {
            sendMessageToPlayer(player, action);
        } else if (action.startsWith("[broadcastsound]")) {
            broadcastSound(action);
        } else if (action.startsWith("[sound]")) {
            playSoundToPlayer(player, action);
        } else {
            player.sendMessage("§cAção desconhecida: " + action);
        }
    }

    private void executeConsoleCommand(String action) {
        // Remove o prefixo [console] e executa o comando no console
        String command = action.substring("[console]".length()).trim();
        if (!command.isEmpty()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    private void executePlayerCommand(Player player, String action) {
        // Remove o prefixo [player] e executa o comando como o jogador
        String command = action.substring("[player]".length()).trim();
        if (!command.isEmpty()) {
            player.performCommand(command);
        }
    }

    private void sendMessageToPlayer(Player player, String action) {
        // Remove o prefixo [message] e envia a mensagem ao jogador
        String message = action.substring("[message]".length()).trim();
        if (!message.isEmpty()) {
            player.sendMessage(message);
        }
    }

    private void broadcastSound(String action) {
        // Remove o prefixo [broadcastsound] e toca o som para todos os jogadores
        String soundName = action.substring("[broadcastsound]".length()).trim();
        if (!soundName.isEmpty()) {
            try {
                Sound sound = Sound.valueOf(soundName.toUpperCase());
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), sound, 1.0f, 1.0f);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Som inválido: " + soundName);
            }
        }
    }

    private void playSoundToPlayer(Player player, String action) {
        // Remove o prefixo [sound] e toca o som para o jogador específico
        String soundName = action.substring("[sound]".length()).trim();
        if (!soundName.isEmpty()) {
            try {
                Sound sound = Sound.valueOf(soundName.toUpperCase());
                player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Som inválido: " + soundName);
            }
        }
    }
}