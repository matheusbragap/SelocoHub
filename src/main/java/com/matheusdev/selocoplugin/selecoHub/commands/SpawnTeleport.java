package com.matheusdev.selocoplugin.selecoHub.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class SpawnTeleport implements CommandExecutor {
    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> cooldowns; // Mapa para armazenar os cooldowns dos jogadores
    private final long cooldownTime = 5; // Cooldown de 5 segundos

    // Construtor que recebe uma instância do plugin
    public SpawnTeleport(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }

    // Método que executa o comando /lobby
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();

            // Verifica se o jogador está em cooldown
            if (isOnCooldown(playerId)) {
                long remainingTime = getRemainingCooldown(playerId);
                player.sendMessage("§cAguarde " + remainingTime + " segundos antes de usar este comando novamente.");
                return true;
            }

            // Carrega as coordenadas do lobby do config.yml
            String lobbyWorldName = plugin.getConfig().getString("lobby.world");
            if (lobbyWorldName == null) {
                player.sendMessage("§cO lobby ainda não foi definido! Use /seloco setlobby.");
                return true;
            }

            World lobbyWorld = plugin.getServer().getWorld(lobbyWorldName);
            if (lobbyWorld == null) {
                player.sendMessage("§cMundo do lobby não encontrado!");
                return true;
            }

            double x = plugin.getConfig().getDouble("lobby.x");
            double y = plugin.getConfig().getDouble("lobby.y");
            double z = plugin.getConfig().getDouble("lobby.z");
            float yaw = (float) plugin.getConfig().getDouble("lobby.yaw");
            float pitch = (float) plugin.getConfig().getDouble("lobby.pitch");

            // Cria uma Location com as coordenadas do lobby
            Location lobbyLocation = new Location(lobbyWorld, x, y, z, yaw, pitch);

            // Teleporta o jogador para o lobby
            player.teleport(lobbyLocation);
            player.sendMessage("§aTeleportado para o lobby!");

            // Aplica o cooldown ao jogador
            applyCooldown(playerId);
        } else {
            sender.sendMessage("§cEste comando só pode ser executado por um jogador.");
        }
        return true;
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