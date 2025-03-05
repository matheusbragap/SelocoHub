package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoin implements Listener {
    private JavaPlugin plugin;

    // Construtor que recebe uma instância do plugin
    public PlayerJoin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Método que escuta o evento de entrada no mundo
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Verifica se o mundo do jogador é o mesmo do lobby
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName != null && player.getWorld().getName().equals(lobbyWorldName)) {
            // Carrega as coordenadas do lobby do config.yml
            double x = plugin.getConfig().getDouble("lobby.x");
            double y = plugin.getConfig().getDouble("lobby.y");
            double z = plugin.getConfig().getDouble("lobby.z");
            float yaw = (float) plugin.getConfig().getDouble("lobby.yaw");
            float pitch = (float) plugin.getConfig().getDouble("lobby.pitch");

            // Obtém o mundo do lobby
            World lobbyWorld = player.getWorld();

            // Cria uma Location com as coordenadas do lobby
            Location lobbyLocation = new Location(lobbyWorld, x, y, z, yaw, pitch);

            // Teleporta o jogador para o lobby
            player.teleport(lobbyLocation);
        }
    }
}