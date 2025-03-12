package com.matheusdev.selocoplugin.selecoHub.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

        action = action.replace("%player%", player.getName());

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
        String command = action.substring("[console]".length()).trim();
        if (!command.isEmpty()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    private void executePlayerCommand(Player player, String action) {
        String command = action.substring("[player]".length()).trim();
        if (!command.isEmpty()) {
            player.performCommand(command);
        }
    }

    private void sendMessageToPlayer(Player player, String action) {
        String message = action.substring("[message]".length()).trim();
        if (!message.isEmpty()) {
            player.sendMessage(message);
        }
    }

    private void broadcastSound(String action) {
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