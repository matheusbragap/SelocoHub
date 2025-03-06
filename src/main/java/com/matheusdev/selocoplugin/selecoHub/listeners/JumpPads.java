package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.event.block.Action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JumpPads implements Listener {

    private final JavaPlugin plugin;
    private String lobbyWorld;
    private boolean jumpPadsActivate; // Alterado de "jumpPadsEnabled" para "jumpPadsActivate"
    private double verticalPower;
    private double horizontalPower;
    private Set<Material> pressurePlates;

    public JumpPads(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void reloadConfig() {
        loadConfig();
        plugin.getLogger().info("Configurações dos JumpPads recarregadas.");
    }

    private void loadConfig() {
        // Carrega o mundo do lobby do config.yml
        lobbyWorld = plugin.getConfig().getString("lobby.world");

        // Carrega as configurações dos JumpPads
        jumpPadsActivate = plugin.getConfig().getBoolean("jumppads.activate"); // Alterado de "enabled" para "activate"
        verticalPower = plugin.getConfig().getDouble("jumppads.vertical");
        horizontalPower = plugin.getConfig().getDouble("jumppads.horizontal");

        // Carrega a lista de placas de pressão ativas
        pressurePlates = new HashSet<>();
        List<String> pressurePlateNames = plugin.getConfig().getStringList("jumppads.items");
        for (String plateName : pressurePlateNames) {
            try {
                Material material = Material.valueOf(plateName.toUpperCase());
                pressurePlates.add(material);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Material inválido no config.yml: " + plateName);
            }
        }

        // Mensagem de depuração para verificar as configurações carregadas
        plugin.getLogger().info("Configurações carregadas:");
        plugin.getLogger().info("JumpPads Activate: " + jumpPadsActivate); // Alterado de "Enabled" para "Activate"
        plugin.getLogger().info("Vertical Power: " + verticalPower);
        plugin.getLogger().info("Horizontal Power: " + horizontalPower);
        plugin.getLogger().info("Pressure Plates: " + pressurePlates);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Verifica se a ação é PHYSICAL (pisar em uma placa de pressão)
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Player player = event.getPlayer();

        // Verifica se os JumpPads estão ativados
        if (!jumpPadsActivate) { // Alterado de "jumpPadsEnabled" para "jumpPadsActivate"
            return;
        }

        // Verifica se o jogador está no mundo do lobby
        if (!player.getWorld().getName().equals(lobbyWorld)) {
            return;
        }

        // Verifica se o bloco é uma placa de pressão e se está ativa
        if (event.getClickedBlock() != null && isPressurePlateEnabled(event.getClickedBlock().getType())) {
            // Aplica uma velocidade para frente e para cima com base nas configurações
            Vector velocity = player.getLocation().getDirection().multiply(horizontalPower).setY(verticalPower);
            player.setVelocity(velocity);
            plugin.getLogger().info("JumpPad ativado para o jogador " + player.getName());
        }
    }

    // Verifica se o material é uma placa de pressão e se está ativa
    private boolean isPressurePlateEnabled(Material material) {
        return pressurePlates.contains(material);
    }
}