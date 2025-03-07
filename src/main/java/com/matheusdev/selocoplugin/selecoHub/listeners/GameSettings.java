package com.matheusdev.selocoplugin.selecoHub.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
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

    public GameSettings(JavaPlugin plugin) {
        this.plugin = plugin;
        setupLobbyWorld();
    }

    public void reloadConfig() {
        setupLobbyWorld();
    }

    private void setupLobbyWorld() {
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName != null) {
            World lobbyWorld = plugin.getServer().getWorld(lobbyWorldName);
            if (lobbyWorld != null) {
                boolean disableDaylightCycle = plugin.getConfig().getBoolean("game-settings.disable-daylight-cycle");

                // Aplica as configurações de tempo
                lobbyWorld.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, !disableDaylightCycle);

                // Desativa o clima no mundo do lobby
                lobbyWorld.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
                lobbyWorld.setStorm(false);
                lobbyWorld.setThundering(false);

                plugin.getLogger().info("Configurações do mundo do lobby aplicadas:");
                plugin.getLogger().info("- Clima desativado.");
                plugin.getLogger().info("- Ciclo diurno: " + (disableDaylightCycle ? "Desativado" : "Ativado"));
            } else {
                plugin.getLogger().warning("Mundo do lobby não encontrado: " + lobbyWorldName);
            }
        } else {
            plugin.getLogger().warning("Nome do mundo do lobby não está definido no config.yml.");
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (isLobbyWorld(event)) {
            if (event.getWhoClicked() instanceof org.bukkit.entity.Player) {
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();
                if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (isLobbyWorld(event)) {
            event.setCancelled(true);
        }
    }

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
}