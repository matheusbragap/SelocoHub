package com.matheusdev.selocoplugin.selecoHub.management;

import com.matheusdev.selocoplugin.selecoHub.commands.*;
import com.matheusdev.selocoplugin.selecoHub.listeners.ProtectWorld;
import com.matheusdev.selocoplugin.selecoHub.listeners.JumpPads;
import com.matheusdev.selocoplugin.selecoHub.listeners.GameSettings; // Importe a classe GameSettings
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Commands implements CommandExecutor {
    private final JavaPlugin plugin;
    private final ProtectWorld protectWorld;
    private final Permissions permissions;
    private final JumpPads jumpPads;
    private final GameSettings gameSettings; // Adicionado o parâmetro GameSettings
    private final HashMap<UUID, Long> cooldowns;
    private final long cooldownTime = 1;

    public Commands(JavaPlugin plugin, ProtectWorld protectWorld, Permissions permissions, JumpPads jumpPads, GameSettings gameSettings) {
        this.plugin = plugin;
        this.protectWorld = protectWorld;
        this.permissions = permissions;
        this.jumpPads = jumpPads;
        this.gameSettings = gameSettings; // Inicializa o parâmetro GameSettings
        this.cooldowns = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("seloco")) {
            // Verifica se o jogador tem permissão para usar o comando /seloco
            if (!permissions.canUseSeloco(sender)) {
                sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                return true;
            }

            // Verifica se o jogador está em cooldown
            if (sender instanceof Player) {
                Player player = (Player) sender;
                UUID playerId = player.getUniqueId();

                if (isOnCooldown(playerId)) {
                    long remainingTime = getRemainingCooldown(playerId);
                    player.sendMessage("§cAguarde " + remainingTime + " segundos antes de usar qualquer comando novamente.");
                    return true;
                }

                // Aplica o cooldown ao jogador
                applyCooldown(playerId);
            }

            if (args.length == 0) {
                sender.sendMessage("§cUse: /seloco ajuda");
                return true;
            }

            String subcomando = args[0].toLowerCase();

            switch (subcomando) {
                case "ajuda":
                    if (permissions.canUseAjuda(sender)) {
                        new HelpCommandsList().execute(sender);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                case "setlobby":
                    if (permissions.canUseSetLobby(sender)) {
                        new SetLobby(plugin).execute(sender);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                case "build":
                    if (permissions.canUseBuild(sender)) {
                        BuilderMode builderMode = new BuilderMode(plugin, protectWorld);
                        builderMode.execute(sender);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                case "reload":
                    if (permissions.canUseReload(sender)) {
                        // Passa a instância de JumpPads e GameSettings para o PluginReload
                        new PluginReload(plugin, jumpPads, gameSettings).execute(sender, true);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                default:
                    sender.sendMessage("§cComando desconhecido!");
                    break;
            }
            return true;
        }
        return false;
    }

    // Método para verificar se o jogador está em cooldown
    private boolean isOnCooldown(UUID playerId) {
        if (cooldowns.containsKey(playerId)) {
            long currentTime = System.currentTimeMillis() / 1000; // Tempo atual em segundos
            long lastUsedTime = cooldowns.get(playerId);
            return (currentTime - lastUsedTime) < cooldownTime;
        }
        return false;
    }

    // Método para obter o tempo restante do cooldown
    private long getRemainingCooldown(UUID playerId) {
        if (cooldowns.containsKey(playerId)) {
            long currentTime = System.currentTimeMillis() / 1000; // Tempo atual em segundos
            long lastUsedTime = cooldowns.get(playerId);
            return cooldownTime - (currentTime - lastUsedTime);
        }
        return 0;
    }

    // Método para aplicar o cooldown ao jogador
    private void applyCooldown(UUID playerId) {
        long currentTime = System.currentTimeMillis() / 1000; // Tempo atual em segundos
        cooldowns.put(playerId, currentTime);
    }
}