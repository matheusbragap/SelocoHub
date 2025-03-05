package com.matheusdev.selocoplugin.selecoHub.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetLobby {
    private JavaPlugin plugin;

    // Construtor que recebe uma instância do plugin
    public SetLobby(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Método que executa o comando /seloco setlobby
    public void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Salva a localização do jogador no config.yml
            plugin.getConfig().set("lobby.world", player.getLocation().getWorld().getName());
            plugin.getConfig().set("lobby.x", player.getLocation().getX());
            plugin.getConfig().set("lobby.y", player.getLocation().getY());
            plugin.getConfig().set("lobby.z", player.getLocation().getZ());
            plugin.getConfig().set("lobby.yaw", player.getLocation().getYaw());
            plugin.getConfig().set("lobby.pitch", player.getLocation().getPitch());
            plugin.saveConfig(); // Salva as alterações no arquivo config.yml

            // Mensagem formatada com as informações do lobby
            String mensagem = "§aLobby definido com sucesso!\n" +
                    "§fMundo: §e" + player.getLocation().getWorld().getName() + "\n" +
                    "§fCoordenadas: §eX: " + player.getLocation().getX() +
                    " §7| §eY: " + player.getLocation().getY() +
                    " §7| §eZ: " + player.getLocation().getZ() + "\n" +
                    "§fYaw: §e" + player.getLocation().getYaw() + "\n" +
                    "§fPitch: §e" + player.getLocation().getPitch();

            player.sendMessage(mensagem);
        } else {
            sender.sendMessage("§cEste comando só pode ser executado por um jogador.");
        }
    }
}