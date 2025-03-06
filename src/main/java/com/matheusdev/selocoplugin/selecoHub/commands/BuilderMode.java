package com.matheusdev.selocoplugin.selecoHub.commands;

import com.matheusdev.selocoplugin.selecoHub.listeners.ProtectWorld;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class BuilderMode {
    private final JavaPlugin plugin;
    private final ProtectWorld protectWorld;

    // Chave para armazenar o modo de jogo original nos metadados do jogador
    private static final String ORIGINAL_GAMEMODE_KEY = "original_gamemode";

    // Construtor que aceita JavaPlugin e ProtectWorld
    public BuilderMode(JavaPlugin plugin, ProtectWorld protectWorld) {
        this.plugin = plugin;
        this.protectWorld = protectWorld;
    }

    // Método execute que ativa/desativa o modo de construção
    public void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Verifica se o jogador está no mundo do lobby
            String lobbyWorld = plugin.getConfig().getString("lobby.world");
            if (lobbyWorld == null || !player.getWorld().getName().equals(lobbyWorld)) {
                player.sendMessage("§cVocê só pode usar o modo Builder no mundo do lobby.");
                return;
            }

            if (player.hasPermission("seloco.build")) {
                // Alterna o modo de construção
                boolean currentMode = protectWorld.isBuilderModeEnabled(player);
                protectWorld.setBuilderMode(player, !currentMode);

                // Limpa o inventário do jogador ao ativar ou desativar o modo Builder
                player.getInventory().clear(); // Limpa todos os itens do inventário

                if (!currentMode) {
                    // Ativa o modo Builder
                    // Armazena o modo de jogo original nos metadados do jogador
                    player.setMetadata(ORIGINAL_GAMEMODE_KEY, new FixedMetadataValue(plugin, player.getGameMode()));
                    player.setGameMode(GameMode.CREATIVE); // Muda para o modo Criativo
                    player.sendMessage("§aModo Builder ativado!");
                } else {
                    // Desativa o modo Builder
                    // Obtém o modo de jogo original dos metadados do jogador
                    if (player.hasMetadata(ORIGINAL_GAMEMODE_KEY)) {
                        GameMode originalMode = (GameMode) player.getMetadata(ORIGINAL_GAMEMODE_KEY).get(0).value();
                        player.setGameMode(originalMode); // Restaura o modo original
                        player.removeMetadata(ORIGINAL_GAMEMODE_KEY, plugin); // Remove os metadados
                        player.sendMessage("§cModo Builder desativado!");
                    } else {
                        // Caso o jogador não tenha metadados (situação inesperada)
                        player.setGameMode(GameMode.SURVIVAL); // Define como Survival por padrão
                        player.sendMessage("§cModo Builder desativado! Modo padrão (Survival) restaurado!");
                    }
                }
            } else {
                player.sendMessage("§cVocê não tem permissão para usar o modo Builder.");
            }
        } else {
            sender.sendMessage("§cEste comando só pode ser executado por um jogador.");
        }
    }
}