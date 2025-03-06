package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent; // Importação adicionada
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameSettings implements Listener {

    private final JavaPlugin plugin;

    // Construtor que recebe uma instância do plugin
    public GameSettings(JavaPlugin plugin) {
        this.plugin = plugin;
        setupLobbyWorld(); // Configura o mundo do lobby ao iniciar o plugin
    }

    // Método para configurar o mundo do lobby
    private void setupLobbyWorld() {
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName != null) {
            World lobbyWorld = plugin.getServer().getWorld(lobbyWorldName);
            if (lobbyWorld != null) {
                // Desativa o ciclo de tempo e o clima no mundo do lobby
                lobbyWorld.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
                lobbyWorld.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
                lobbyWorld.setStorm(false); // Para a chuva imediatamente
                lobbyWorld.setThundering(false); // Para tempestades imediatamente
                plugin.getLogger().info("Configurações do mundo do lobby aplicadas: clima e ciclo de tempo desativados.");
            } else {
                plugin.getLogger().warning("Mundo do lobby não encontrado: " + lobbyWorldName);
            }
        } else {
            plugin.getLogger().warning("Nome do mundo do lobby não está definido no config.yml.");
        }
    }

    // Método para verificar se o evento ocorreu no mundo do lobby
    private boolean isLobbyWorld(org.bukkit.event.Event event) {
        String lobbyWorld = plugin.getConfig().getString("lobby.world");
        if (lobbyWorld != null) {
            if (event instanceof org.bukkit.event.world.WorldEvent) {
                return ((org.bukkit.event.world.WorldEvent) event).getWorld().getName().equals(lobbyWorld);
            } else if (event instanceof org.bukkit.event.entity.EntityEvent) {
                return ((org.bukkit.event.entity.EntityEvent) event).getEntity().getWorld().getName().equals(lobbyWorld);
            } else if (event instanceof org.bukkit.event.player.PlayerEvent) {
                return ((org.bukkit.event.player.PlayerEvent) event).getPlayer().getWorld().getName().equals(lobbyWorld);
            } else if (event instanceof org.bukkit.event.block.BlockEvent) {
                return ((org.bukkit.event.block.BlockEvent) event).getBlock().getWorld().getName().equals(lobbyWorld);
            } else if (event instanceof org.bukkit.event.inventory.InventoryEvent) {
                return ((org.bukkit.event.inventory.InventoryEvent) event).getView().getPlayer().getWorld().getName().equals(lobbyWorld);
            }
        }
        return false;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (isLobbyWorld(event)) {
            disableHunger(event); // Desativa a fome
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (isLobbyWorld(event)) {
            disableDamage(event); // Desativa o dano
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (isLobbyWorld(event)) {
            disableRain(event); // Desativa a chuva
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (isLobbyWorld(event)) {
            disableBlockBurn(event); // Desativa a queima de blocos
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isLobbyWorld(event)) {
            disableDropItem(event); // Desativa o drop de itens
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (isLobbyWorld(event)) {
            if (event.getWhoClicked() instanceof org.bukkit.entity.Player) {
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();
                if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
                    disableItemMove(event); // Desativa o movimento de itens
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (isLobbyWorld(event)) {
            disableItemDamage(event); // Desativa o dano a itens
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (isLobbyWorld(event)) {
            disablePickUpItems(event); // Desativa a coleta de itens
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (isLobbyWorld(event)) {
            disableExplode(event); // Desativa explosões
        }
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        if (isLobbyWorld(event)) {
            disableDaylightCycle(event); // Desativa o ciclo diurno
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (isLobbyWorld(event)) {
            disableCreatureSpawn(event); // Desativa o spawn de criaturas
        }
    }

    // Métodos auxiliares para desativar funcionalidades
    private void disableHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    private void disableDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    private void disableRain(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    private void disableBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    private void disableDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    private void disableItemMove(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    private void disableItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    private void disablePickUpItems(EntityPickupItemEvent event) {
        event.setCancelled(true);
    }

    private void disableExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    private void disableDaylightCycle(TimeSkipEvent event) {
        event.setCancelled(true);
    }

    private void disableCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }
}