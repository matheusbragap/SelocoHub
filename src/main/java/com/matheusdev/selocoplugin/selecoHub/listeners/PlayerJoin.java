package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoin implements Listener {
    private final JavaPlugin plugin;

    // Construtor que recebe uma instância do plugin
    public PlayerJoin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getLogger().info("Jogador " + player.getName() + " entrou no servidor.");

        // Verifica se o mundo do lobby está definido no config.yml
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null) {
            plugin.getLogger().warning("Mundo do lobby não está definido no config.yml!");
            return;
        }

        // Verifica se o jogador está no mundo do lobby
        if (!player.getWorld().getName().equals(lobbyWorldName)) {
            plugin.getLogger().info("Jogador " + player.getName() + " não está no mundo do lobby. Mundo atual: " + player.getWorld().getName());
            return; // Se não estiver no mundo do lobby, não faz nada
        }

        plugin.getLogger().info("Jogador " + player.getName() + " está no mundo do lobby. Aplicando configurações...");

        // Limpa o inventário do jogador, se configurado
        if (plugin.getConfig().getBoolean("on-join-settings.clear-inventory", true)) {
            player.getInventory().clear(); // Limpa o inventário
            plugin.getLogger().info("Inventário do jogador " + player.getName() + " foi limpo.");
        }

        // Restaura a vida e a fome do jogador, se configurado
        if (plugin.getConfig().getBoolean("on-join-settings.heal-player", true)) {
            player.setHealth(player.getMaxHealth()); // Enche a vida
            player.setFoodLevel(20); // Enche a barra de fome
            plugin.getLogger().info("Vida e fome do jogador " + player.getName() + " foram restauradas.");
        }

        // Remove todos os efeitos de poção atuais do jogador
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType()); // Remove cada efeito
        }
        plugin.getLogger().info("Efeitos de poção do jogador " + player.getName() + " foram removidos.");

        // Aplica os efeitos de poção configurados
        if (plugin.getConfig().getBoolean("on-join-settings.effects.speed", true)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1)); // Velocidade 2 com duração infinita
            plugin.getLogger().info("Efeito de velocidade 2 aplicado ao jogador " + player.getName() + " com duração infinita.");
        }
        if (plugin.getConfig().getBoolean("on-join-settings.effects.jump-boost", true)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 0)); // Super pulo 1 com duração infinita
            plugin.getLogger().info("Efeito de super pulo 1 aplicado ao jogador " + player.getName() + " com duração infinita.");
        }

        // Define o GameMode ao jogador entrar
        String gamemodeString = plugin.getConfig().getString("on-join-settings.set-gamemode", "ADVENTURE").toUpperCase();
        GameMode gamemode;

        try {
            gamemode = GameMode.valueOf(gamemodeString); // Converte a string para GameMode
            plugin.getLogger().info("GameMode definido no config.yml: " + gamemodeString);
        } catch (IllegalArgumentException e) {
            // Se o GameMode for inválido, define como ADVENTURE por padrão
            gamemode = GameMode.ADVENTURE;
            plugin.getLogger().warning("GameMode inválido no config.yml. Usando ADVENTURE como padrão.");
        }

        // Aplica o GameMode ao jogador
        player.setGameMode(gamemode);
        plugin.getLogger().info("GameMode " + gamemode + " aplicado ao jogador " + player.getName() + ".");

        // Verifica se o GameMode foi aplicado corretamente
        if (player.getGameMode() != gamemode) {
            plugin.getLogger().warning("Falha ao aplicar o GameMode " + gamemode + " ao jogador " + player.getName() + ".");
        } else {
            plugin.getLogger().info("GameMode " + gamemode + " aplicado com sucesso ao jogador " + player.getName() + ".");
        }
    }
}