package com.matheusdev.selocoplugin.selecoHub.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class ClickItems implements Listener {
    private final JavaPlugin plugin;
    private final ExecuteItemAction executeItemAction;

    public ClickItems(JavaPlugin plugin, ExecuteItemAction executeItemAction) {
        this.plugin = plugin;
        this.executeItemAction = executeItemAction;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Verifica se os itens da hotbar estão ativados
        boolean hotbarLobbyActivate = plugin.getConfig().getBoolean("hotbar-lobby.activate", false); // Alterado para "hotbar-lobby"
        if (!hotbarLobbyActivate) {
            return;
        }

        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null) {
            plugin.getLogger().warning("Mundo do lobby não está definido no config.yml!");
            return;
        }

        if (!player.getWorld().getName().equals(lobbyWorldName)) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand != null && itemInHand.getType() != Material.AIR) {
            Material itemType = itemInHand.getType();

            if (isItemDefinedInConfig(itemInHand)) {
                event.setCancelled(true);

                if (event.getAction().toString().contains("RIGHT_CLICK") || event.getAction().toString().contains("LEFT_CLICK")) {
                    List<String> actions = getItemActions(itemType);
                    if (actions != null && !actions.isEmpty()) {
                        for (String action : actions) {
                            executeItemAction.execute(player, action);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        // Verifica se os itens da hotbar estão ativados
        boolean hotbarLobbyActivate = plugin.getConfig().getBoolean("hotbar-lobby.activate", false); // Alterado para "hotbar-lobby"
        if (!hotbarLobbyActivate) {
            return;
        }

        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null) {
            plugin.getLogger().warning("Mundo do lobby não está definido no config.yml!");
            return;
        }

        if (!player.getWorld().getName().equals(lobbyWorldName)) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            Material itemType = clickedItem.getType();

            if (isItemDefinedInConfig(clickedItem)) {
                event.setCancelled(true);

                List<String> actions = getItemActions(itemType);
                if (actions != null && !actions.isEmpty()) {
                    for (String action : actions) {
                        executeItemAction.execute(player, action);
                    }
                }
            }
        }
    }

    private boolean isItemDefinedInConfig(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            String itemName = meta.getDisplayName();
            List<String> itemLore = meta.getLore();

            List<Map<?, ?>> itemsConfig = plugin.getConfig().getMapList("hotbar-lobby.items"); // Alterado para "hotbar-lobby"

            for (Map<?, ?> itemConfig : itemsConfig) {
                String configName = (String) itemConfig.get("name");
                List<String> configLore = (List<String>) itemConfig.get("lore");

                if (configName != null && configName.equals(itemName)) {
                    if (configLore != null && configLore.equals(itemLore)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private List<String> getItemActions(Material itemType) {
        List<Map<?, ?>> itemsConfig = plugin.getConfig().getMapList("hotbar-lobby.items"); // Alterado para "hotbar-lobby"
        for (Map<?, ?> itemConfig : itemsConfig) {
            String materialName = (String) itemConfig.get("material");
            if (materialName != null && Material.getMaterial(materialName) == itemType) {
                Object actionObj = itemConfig.get("actions");
                if (actionObj instanceof List) {
                    return (List<String>) actionObj;
                } else if (actionObj instanceof String) {
                    List<String> singleActionList = new java.util.ArrayList<>();
                    singleActionList.add((String) actionObj);
                    return singleActionList;
                }
            }
        }
        return null;
    }
}