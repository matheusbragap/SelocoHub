package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DistanceVoid implements Listener {

    private final JavaPlugin plugin;

    // Construtor que recebe uma instância do plugin
    public DistanceVoid(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Verifica se o jogador está no mundo do lobby
        if (isLobbyWorld(player)) {
            // Recupera a camada de void definida no config.yml
            int voidLayer = plugin.getConfig().getInt("lobby.void-layer", -64);
            // Recupera a distância horizontal permitida do config.yml
            double horizontalDistance = plugin.getConfig().getDouble("lobby.horizontal-distance", 100.0);

            // Verifica se o jogador atingiu ou ultrapassou a camada de void
            if (player.getLocation().getY() <= voidLayer) {
                teleportToLobby(player); // Teleporta o jogador para o lobby
            }

            // Verifica a distância horizontal
            if (player.getLocation().distanceSquared(getLobbyLocation(player)) > horizontalDistance * horizontalDistance) {
                teleportToLobby(player); // Teleporta o jogador para o lobby se ultrapassar a distância
            }
        }
    }

    // Método para verificar se o jogador está no mundo do lobby
    private boolean isLobbyWorld(Player player) {
        String lobbyWorld = plugin.getConfig().getString("lobby.world");
        return lobbyWorld != null && player.getWorld().getName().equals(lobbyWorld);
    }

    // Método para obter a localização do lobby
    private Location getLobbyLocation(Player player) {
        String world = plugin.getConfig().getString("lobby.world");
        double x = plugin.getConfig().getDouble("lobby.x");
        double y = plugin.getConfig().getDouble("lobby.y");
        double z = plugin.getConfig().getDouble("lobby.z");
        float yaw = (float) plugin.getConfig().getDouble("lobby.yaw");
        float pitch = (float) plugin.getConfig().getDouble("lobby.pitch");

        return new Location(player.getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    // Método para teleportar o jogador para o lobby
    private void teleportToLobby(Player player) {
        // Cria a localização do lobby
        Location lobbyLocation = getLobbyLocation(player);

        // Teleporta o jogador para o lobby
        player.teleport(lobbyLocation);
    }
}