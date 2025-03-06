package com.matheusdev.selocoplugin.selecoHub.management;

import com.matheusdev.selocoplugin.selecoHub.commands.*;
import com.matheusdev.selocoplugin.selecoHub.listeners.ProtectWorld;
import com.matheusdev.selocoplugin.selecoHub.listeners.JumpPads;
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
    private final JumpPads jumpPads; // Adicionado o parâmetro JumpPads
    private final HashMap<UUID, Long> cooldowns; // Mapa para armazenar os cooldowns dos jogadores
    private final long cooldownTime = 1; // Cooldown de 1 segundo

    public Commands(JavaPlugin plugin, ProtectWorld protectWorld, Permissions permissions, JumpPads jumpPads) {
        this.plugin = plugin;
        this.protectWorld = protectWorld;
        this.permissions = permissions;
        this.jumpPads = jumpPads; // Inicializa o parâmetro JumpPads
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
                        new PluginReload(plugin, jumpPads).execute(sender,true); // Passa a instância de JumpPads
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