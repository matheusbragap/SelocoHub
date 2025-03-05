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
            // Recupera as configurações de teleporte por distância
            boolean teleportDistanceEnabled = plugin.getConfig().getBoolean("teleport-distance.activate", true);
            int horizontalRadius = plugin.getConfig().getInt("teleport-distance.horizontal", 150); // Raio horizontal (padrão: 150)
            int verticalRange = plugin.getConfig().getInt("teleport-distance.vertical", 100); // Raio vertical (padrão: 100)

            // Verifica se o teleporte por distância está ativado
            if (teleportDistanceEnabled) {
                // Verifica a distância horizontal (X e Z)
                if (isOutsideHorizontalRadius(player, horizontalRadius)) {
                    teleportToLobby(player); // Teleporta o jogador para o lobby se ultrapassar o raio horizontal
                }

                // Verifica a distância vertical (Y)
                if (isOutsideVerticalRange(player, verticalRange)) {
                    teleportToLobby(player); // Teleporta o jogador para o lobby se ultrapassar o raio vertical
                }
            }
        }
    }

    // Método para verificar se o jogador está no mundo do lobby
    private boolean isLobbyWorld(Player player) {
        String lobbyWorld = plugin.getConfig().getString("lobby.world");
        return lobbyWorld != null && player.getWorld().getName().equals(lobbyWorld);
    }

    // Método para verificar se o jogador está fora do raio horizontal
    private boolean isOutsideHorizontalRadius(Player player, int horizontalRadius) {
        Location lobbyLocation = getLobbyLocation(player);
        double deltaX = Math.abs(player.getLocation().getX() - lobbyLocation.getX()); // Distância absoluta em X
        double deltaZ = Math.abs(player.getLocation().getZ() - lobbyLocation.getZ()); // Distância absoluta em Z

        // Verifica se o jogador ultrapassou o limite em X ou Z
        return deltaX > horizontalRadius || deltaZ > horizontalRadius;
    }

    // Método para verificar se o jogador está fora do raio vertical
    private boolean isOutsideVerticalRange(Player player, int verticalRange) {
        Location lobbyLocation = getLobbyLocation(player);
        double lobbyY = lobbyLocation.getY();
        double playerY = player.getLocation().getY();

        // Verifica se o jogador ultrapassou o limite para cima ou para baixo
        return playerY > (lobbyY + verticalRange) || playerY < (lobbyY - verticalRange);
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