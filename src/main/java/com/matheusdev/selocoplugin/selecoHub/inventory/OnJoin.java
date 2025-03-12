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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

public class OnJoin implements Listener {
    private final JavaPlugin plugin;
    private final HidePlayers hidePlayers;

    public OnJoin(JavaPlugin plugin, HidePlayers hidePlayers) {
        this.plugin = plugin;
        this.hidePlayers = hidePlayers;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        scheduleItemCheck(player); // Agenda a verificação após 2 ticks
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        scheduleItemCheck(player); // Agenda a verificação após 2 ticks
    }

    // Agenda a verificação do mundo do lobby após 2 ticks
    private void scheduleItemCheck(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkAndSetItems(player); // Verifica e define os itens após 2 ticks
            }
        }.runTaskLater(plugin, 2); // Executa após 2 ticks
    }

    private void checkAndSetItems(Player player) {
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null || !player.getWorld().getName().equals(lobbyWorldName)) {
            return; // Só configura itens no mundo do lobby
        }

        setItemsInHotbar(player); // Configura os itens da hotbar
        giveHidePlayersItem(player); // Dá o item de "hide players"
    }

    private void setItemsInHotbar(Player player) {
        if (!plugin.getConfig().getBoolean("hotbar-lobby.activate")) {
            return; // Se a hotbar estiver desativada, não faz nada
        }

        player.getInventory().clear(); // Limpa o inventário

        List<Map<?, ?>> itemsConfig = plugin.getConfig().getMapList("hotbar-lobby.items");
        if (itemsConfig == null || itemsConfig.isEmpty()) {
            plugin.getLogger().warning("hotbar-lobby.items não está configurado corretamente.");
            return;
        }

        for (Map<?, ?> itemConfig : itemsConfig) {
            String materialName = (String) itemConfig.get("material");
            Integer slot = (Integer) itemConfig.get("slot");

            if (materialName == null || slot == null) {
                plugin.getLogger().warning("Material ou slot não especificado em hotbar-lobby.items.");
                continue;
            }

            Material material = Material.getMaterial(materialName);
            if (material == null) {
                plugin.getLogger().warning("Material " + materialName + " não é válido!");
                continue;
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                String itemName = (String) itemConfig.get("name");
                if (itemName != null) {
                    meta.setDisplayName(itemName);
                }

                List<String> lore = (List<String>) itemConfig.get("lore");
                if (lore != null && !lore.isEmpty()) {
                    meta.setLore(lore);
                }

                item.setItemMeta(meta);
            }

            List<String> enchantments = (List<String>) itemConfig.get("enchantments");
            if (enchantments != null) {
                for (String enchantmentString : enchantments) {
                    String[] parts = enchantmentString.split(":");
                    if (parts.length == 2) {
                        Enchantment enchantment = Enchantment.getByName(parts[0]);
                        if (enchantment != null) {
                            int level = Integer.parseInt(parts[1]);
                            item.addUnsafeEnchantment(enchantment, level);
                        }
                    }
                }
            }

            player.getInventory().setItem(slot, item);
        }
    }

    private void giveHidePlayersItem(Player player) {
        if (!plugin.getConfig().getBoolean("hide-players.activate")) {
            return; // Se a funcionalidade de "hide players" estiver desativada, não dá o item
        }

        hidePlayers.updateHidePlayersItem(player, "hide-enabled-item"); // Dá o item de "hide players"
    }
}