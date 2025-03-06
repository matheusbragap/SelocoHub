package com.matheusdev.selocoplugin.selecoHub.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class OnJoin implements Listener {
    private final JavaPlugin plugin;

    public OnJoin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        checkAndSetItems(player);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        checkAndSetItems(player);
    }

    private void checkAndSetItems(Player player) {
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null) {
            plugin.getLogger().warning("Mundo do lobby não está definido no config.yml!");
            return;
        }

        if (player.getWorld().getName().equals(lobbyWorldName)) {
            setItemsInHotbar(player);
        }
    }

    private void setItemsInHotbar(Player player) {
        // Verifica se os itens da hotbar estão ativados
        boolean hotbarLobbyActivate = plugin.getConfig().getBoolean("hotbar-lobby.activate", false); // Alterado para "hotbar-lobby"
        if (!hotbarLobbyActivate) {
            player.sendMessage("§cItens da hotbar estão desativados.");
            return;
        }

        player.getInventory().clear();

        List<Map<?, ?>> itemsConfig = plugin.getConfig().getMapList("hotbar-lobby.items"); // Alterado para "hotbar-lobby"

        if (itemsConfig != null) {
            for (Map<?, ?> itemConfig : itemsConfig) {
                String materialName = (String) itemConfig.get("material");
                Integer slot = (Integer) itemConfig.get("slot");

                Material material = Material.getMaterial(materialName);
                if (material != null && slot != null) {
                    ItemStack item = new ItemStack(material);

                    String itemName = (String) itemConfig.get("name");
                    if (itemName != null) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(itemName);
                        item.setItemMeta(meta);
                    }

                    List<String> lore = (List<String>) itemConfig.get("lore");
                    if (lore != null && !lore.isEmpty()) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                    }

                    List<String> enchantments = (List<String>) itemConfig.get("enchantments");
                    if (enchantments != null) {
                        for (String enchantmentString : enchantments) {
                            String[] parts = enchantmentString.split(":");
                            if (parts.length == 2) {
                                Enchantment enchantment = Enchantment.getByName(parts[0]);
                                int level = Integer.parseInt(parts[1]);
                                if (enchantment != null) {
                                    item.addUnsafeEnchantment(enchantment, level);
                                }
                            }
                        }
                    }

                    player.getInventory().setItem(slot, item);
                } else {
                    player.sendMessage("§cMaterial " + materialName + " não é válido ou slot não especificado!");
                }
            }
        }
    }
}