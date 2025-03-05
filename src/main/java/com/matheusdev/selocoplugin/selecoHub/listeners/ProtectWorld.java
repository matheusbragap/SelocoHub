package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ProtectWorld implements Listener {
    private final JavaPlugin plugin;
    private final Map<Player, Boolean> playerBuildMode = new HashMap<>();

    public ProtectWorld(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Verifica se o modo de construção está ativado para o jogador
    public boolean isBuilderModeEnabled(Player player) {
        // Se o jogador não tiver permissão, o modo de construção é automaticamente desativado
        if (!player.hasPermission("seloco.build")) {
            playerBuildMode.put(player, false); // Define o modo de construção como false
            return false;
        }
        return playerBuildMode.getOrDefault(player, false);
    }

    // Define o modo de construção para o jogador
    public void setBuilderMode(Player player, boolean enabled) {
        // Só permite ativar o modo de construção se o jogador tiver permissão
        if (player.hasPermission("seloco.build")) {
            playerBuildMode.put(player, enabled);
        } else {
            playerBuildMode.put(player, false); // Força o modo de construção como false
        }
    }

    // Método para verificar se o evento ocorreu no mundo do lobby
    private boolean isLobbyWorld(Player player) {
        // Recupera o nome do mundo do lobby do config.yml
        String lobbyWorld = plugin.getConfig().getString("lobby.world");

        // Verifica se o mundo do jogador é o mesmo que o mundo do lobby
        return lobbyWorld != null && player.getWorld().getName().equals(lobbyWorld);
    }

    // Impede a quebra de blocos se o modo de construção estiver desativado
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        // Verifica se o evento ocorreu no mundo do lobby
        if (!isLobbyWorld(player)) {
            return; // Não cancela o evento se não for no mundo do lobby
        }

        // Verifica se o jogador está no modo de construção e tem permissão
        if (isBuilderModeEnabled(player)) {
            // Permite a quebra de blocos se o modo de construção estiver ativado
            return;
        }

        // Impede a quebra de blocos se o modo de construção estiver desativado
        event.setCancelled(true);
    }

    // Verifica interações e permite interações com placas de pressão e botões
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Verifica se o evento ocorreu no mundo do lobby
        if (!isLobbyWorld(player)) {
            return; // Não cancela o evento se não for no mundo do lobby
        }

        // Verifica se o jogador pode interagir com blocos, dependendo do modo de construção e permissão
        if (isBuilderModeEnabled(player)) {
            return; // Permite interações se o modo de construção estiver ativado
        }

        // Verifica se o bloco interagido é uma placa de pressão ou botão
        if (event.getClickedBlock() != null) {
            Material material = event.getClickedBlock().getType();
            if (isPressurePlate(material) || isButton(material)) {
                return; // Permite interações com placas de pressão e botões
            }
        }

        // Cancela a interação se o modo de construção estiver desativado e o bloco não for uma placa de pressão ou botão
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        // Verifica se o evento ocorreu no mundo do lobby
        if (!isLobbyWorld(player)) {
            return; // Não cancela o evento se não for no mundo do lobby
        }

        // Verifica se o jogador pode interagir com entidades, dependendo do modo de construção e permissão
        if (isBuilderModeEnabled(player)) {
            return; // Permite interação com entidades se o modo de construção estiver ativado
        }

        // Cancela a interação com entidades se o modo de construção estiver desativado
        event.setCancelled(true);
    }

    // Verifica se o material é uma placa de pressão
    private boolean isPressurePlate(Material material) {
        return material.name().endsWith("_PRESSURE_PLATE");
    }

    // Verifica se o material é um botão
    private boolean isButton(Material material) {
        return material.name().endsWith("_BUTTON");
    }
}