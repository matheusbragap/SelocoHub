package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TpJoin implements Listener {
    private final JavaPlugin plugin;

    // Construtor que recebe uma instância do plugin
    public TpJoin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Verifica se o teleporte ao lobby ao entrar está ativado
        boolean tpJoinLobbyActive = plugin.getConfig().getBoolean("tp-join-lobby.activate", true);
        if (!tpJoinLobbyActive) {
            return; // Se não estiver ativado, não faz nada
        }

        // Verifica se o jogador deve ser teleportado apenas se deslogou no mundo do lobby
        boolean onlyWorldLobby = plugin.getConfig().getBoolean("tp-join-lobby.only-world-lobby", false);
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");

        if (lobbyWorldName == null) {
            plugin.getLogger().warning("O mundo do lobby não está definido no config.yml!");
            return;
        }

        // Se onlyWorldLobby estiver ativado, verifica se o jogador deslogou no mundo do lobby
        if (onlyWorldLobby && !player.getWorld().getName().equals(lobbyWorldName)) {
            return; // Se o jogador não estiver no mundo do lobby, não faz nada
        }

        // Carrega as coordenadas do lobby do config.yml
        double x = plugin.getConfig().getDouble("lobby.x");
        double y = plugin.getConfig().getDouble("lobby.y");
        double z = plugin.getConfig().getDouble("lobby.z");
        float yaw = (float) plugin.getConfig().getDouble("lobby.yaw");
        float pitch = (float) plugin.getConfig().getDouble("lobby.pitch");

        // Obtém o mundo do lobby
        World lobbyWorld = plugin.getServer().getWorld(lobbyWorldName);
        if (lobbyWorld == null) {
            plugin.getLogger().warning("Mundo do lobby não encontrado: " + lobbyWorldName);
            return;
        }

        // Cria uma Location com as coordenadas do lobby
        Location lobbyLocation = new Location(lobbyWorld, x, y, z, yaw, pitch);

        // Teleporta o jogador para o lobby
        player.teleport(lobbyLocation);
        plugin.getLogger().info("Jogador " + player.getName() + " foi teleportado para o lobby.");
    }
}