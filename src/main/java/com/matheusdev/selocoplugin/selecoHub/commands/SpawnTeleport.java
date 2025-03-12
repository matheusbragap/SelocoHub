package com.matheusdev.selocoplugin.selecoHub.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class SpawnTeleport implements CommandExecutor {
    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> cooldowns; // Mapa para armazenar os cooldowns dos jogadores
    private final HashMap<UUID, Integer> warmupTasks; // Mapa para armazenar as tarefas de warmup dos jogadores
    private final HashMap<UUID, Location> warmupPlayers; // Mapa para armazenar jogadores em warmup

    // Construtor que recebe uma instância do plugin
    public SpawnTeleport(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
        this.warmupTasks = new HashMap<>();
        this.warmupPlayers = new HashMap<>();
    }

    // Método que executa o comando /lobby
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por um jogador.");
            return true;
        }

        Player player = (Player) sender;

        // Verifica se o comando está ativado no config.yml
        if (!plugin.getConfig().getBoolean("hub-teleport")) {
            player.sendMessage("§cEste comando está desativado.");
            return true;
        }

        UUID playerId = player.getUniqueId();

        // Verifica se o jogador está em warmup
        if (warmupTasks.containsKey(playerId)) {
            player.sendMessage("§cVocê já está em processo de teleporte. Aguarde...");
            return true;
        }

        // Verifica se o jogador está em cooldown
        if (isOnCooldown(playerId)) {
            long remainingTime = getRemainingCooldown(playerId);
            player.sendMessage("§cAguarde " + remainingTime + " segundos antes de usar este comando novamente.");
            return true;
        }

        // Verifica se o jogador está no mundo do lobby e se only-other-world está ativado
        boolean onlyOtherWorld = plugin.getConfig().getBoolean("warmup.only-other-world");
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");

        if (onlyOtherWorld && lobbyWorldName != null && player.getWorld().getName().equals(lobbyWorldName)) {
            // Teleporta instantaneamente se estiver no lobby e only-other-world estiver ativado
            teleportToLobby(player);
            return true;
        }

        // Inicia o warmup
        startWarmup(player);
        return true;
    }

    // Método para iniciar o warmup
    private void startWarmup(Player player) {
        UUID playerId = player.getUniqueId();
        long warmupTime = (long) (plugin.getConfig().getDouble("warmup.time") * 20); // Converte segundos para ticks
        boolean moveAllowed = plugin.getConfig().getBoolean("warmup.move");

        // Armazena a localização inicial do jogador para verificar se ele se moveu
        if (!moveAllowed) {
            warmupPlayers.put(playerId, player.getLocation());
        }

        // Agenda a contagem regressiva do warmup
        int taskId = new BukkitRunnable() {
            int timeLeft = (int) plugin.getConfig().getDouble("warmup.time");

            @Override
            public void run() {
                // Verifica se o jogador se moveu (se move estiver desativado)
                if (!moveAllowed && warmupPlayers.containsKey(playerId)) {
                    Location initialLocation = warmupPlayers.get(playerId);
                    if (!initialLocation.equals(player.getLocation())) {
                        player.sendMessage("§cVocê se moveu! Teleporte cancelado.");
                        warmupPlayers.remove(playerId);
                        warmupTasks.remove(playerId);
                        this.cancel();
                        return;
                    }
                }

                // Envia a mensagem de contagem regressiva
                if (timeLeft > 0) {
                    player.sendMessage("§aTeleportando em " + timeLeft + " segundos...");
                    timeLeft--;
                } else {
                    // Teleporta o jogador após o warmup
                    teleportToLobby(player);
                    warmupPlayers.remove(playerId);
                    warmupTasks.remove(playerId);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L).getTaskId(); // Executa a cada 20 ticks (1 segundo)

        // Armazena a tarefa de warmup do jogador
        warmupTasks.put(playerId, taskId);
    }

    // Método para teleportar o jogador para o lobby
    private void teleportToLobby(Player player) {
        // Carrega as coordenadas do lobby do config.yml
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null) {
            player.sendMessage("§cO lobby ainda não foi definido! Use /seloco setlobby.");
            return;
        }

        World lobbyWorld = plugin.getServer().getWorld(lobbyWorldName);
        if (lobbyWorld == null) {
            player.sendMessage("§cMundo do lobby não encontrado!");
            return;
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
        applyCooldown(player.getUniqueId());
    }

    // Método para verificar se o jogador está em cooldown
    private boolean isOnCooldown(UUID playerId) {
        if (cooldowns.containsKey(playerId)) {
            long currentTime = System.currentTimeMillis() / 1000; // Tempo atual em segundos
            long lastUsedTime = cooldowns.get(playerId);
            long cooldownTime = (long) plugin.getConfig().getDouble("cooldown"); // Cooldown em segundos
            return (currentTime - lastUsedTime) < cooldownTime;
        }
        return false;
    }

    // Método para obter o tempo restante do cooldown
    private long getRemainingCooldown(UUID playerId) {
        if (cooldowns.containsKey(playerId)) {
            long currentTime = System.currentTimeMillis() / 1000; // Tempo atual em segundos
            long lastUsedTime = cooldowns.get(playerId);
            long cooldownTime = (long) plugin.getConfig().getDouble("cooldown"); // Cooldown em segundos
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