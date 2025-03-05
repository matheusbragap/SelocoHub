package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.event.block.Action;

import java.util.HashMap;
import java.util.Map;

public class JumpPads implements Listener {

    private final JavaPlugin plugin;
    private String lobbyWorld;
    private boolean jumpPadsEnabled;
    private double verticalPower;
    private double horizontalPower;
    private Map<Material, Boolean> pressurePlates;

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
        lobbyWorld = plugin.getConfig().getString("lobby.world", "world");

        // Carrega as configurações dos JumpPads
        jumpPadsEnabled = plugin.getConfig().getBoolean("jumppads.enabled", true);
        verticalPower = plugin.getConfig().getDouble("jumppads.vertical", 1.5);
        horizontalPower = plugin.getConfig().getDouble("jumppads.horizontal", 1.5);

        // Carrega a lista de placas de pressão ativas
        pressurePlates = new HashMap<>();
        for (Material material : Material.values()) {
            if (material.name().endsWith("_PRESSURE_PLATE")) {
                boolean enabled = plugin.getConfig().getBoolean("jumppads.items." + material.name(), true);
                pressurePlates.put(material, enabled);
            }
        }

        // Mensagem de depuração para verificar as configurações carregadas
        plugin.getLogger().info("Configurações carregadas:");
        plugin.getLogger().info("JumpPads Enabled: " + jumpPadsEnabled);
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

        // Verifica se os JumpPads estão habilitados
        if (!jumpPadsEnabled) {
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
        return pressurePlates.getOrDefault(material, false);
    }
}