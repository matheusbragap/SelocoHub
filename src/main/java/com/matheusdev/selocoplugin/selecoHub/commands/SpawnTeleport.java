package com.matheusdev.selocoplugin.selecoHub.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnTeleport implements CommandExecutor {
    private JavaPlugin plugin;

    // Construtor que recebe uma instância do plugin
    public SpawnTeleport(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Método que executa o comando /lobby
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

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
        } else {
            sender.sendMessage("§cEste comando só pode ser executado por um jogador.");
        }
        return true;
    }
}