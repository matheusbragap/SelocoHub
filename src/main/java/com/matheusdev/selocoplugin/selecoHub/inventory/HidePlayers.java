package com.matheusdev.selocoplugin.selecoHub.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HidePlayers implements Listener {
    private final JavaPlugin plugin;
    private final Map<UUID, Boolean> playerVisibilityState = new HashMap<>(); // Armazena o estado de visibilidade no lobby
    private final Map<UUID, Long> cooldowns = new HashMap<>(); // Armazena os cooldowns

    public HidePlayers(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        applyVisibilityState(player); // Aplica o estado de visibilidade ao entrar no servidor
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        applyVisibilityState(player); // Aplica o estado de visibilidade ao mudar de mundo
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Não há necessidade de salvar o estado ao sair, pois não há persistência
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Verifica se o jogador está no mundo do lobby
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null || !player.getWorld().getName().equals(lobbyWorldName)) {
            return; // Só funciona no mundo do lobby
        }

        // Verifica se a ação é um clique direito ou esquerdo no ar ou em um bloco
        if (!event.getAction().toString().contains("RIGHT_CLICK") && !event.getAction().toString().contains("LEFT_CLICK")) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            return;
        }

        // Verifica se o item é o de ativar ou desativar o vanish
        if (isHideEnabledItem(itemInHand)) {
            event.setCancelled(true);

            // Verifica o cooldown
            double cooldown = plugin.getConfig().getDouble("hide-players.hide-enabled-item.cooldown");
            if (cooldown > 0 && isOnCooldown(player.getUniqueId(), cooldown)) {
                double remainingCooldown = getRemainingCooldown(player.getUniqueId(), cooldown);
                player.sendMessage("§cAguarde " + String.format("%.1f", remainingCooldown) + " segundos para usar novamente.");
                return;
            }

            // Aplica o cooldown
            if (cooldown > 0) {
                applyCooldown(player.getUniqueId());
            }

            hidePlayers(player); // Esconde os players
            updateHidePlayersItem(player, "hide-disabled-item"); // Troca para o item de desativar
        } else if (isHideDisabledItem(itemInHand)) {
            event.setCancelled(true);

            // Verifica o cooldown
            double cooldown = plugin.getConfig().getDouble("hide-players.hide-disabled-item.cooldown");
            if (cooldown > 0 && isOnCooldown(player.getUniqueId(), cooldown)) {
                double remainingCooldown = getRemainingCooldown(player.getUniqueId(), cooldown);
                player.sendMessage("§cAguarde " + String.format("%.1f", remainingCooldown) + " segundos para usar novamente.");
                return;
            }

            // Aplica o cooldown
            if (cooldown > 0) {
                applyCooldown(player.getUniqueId());
            }

            showPlayers(player); // Mostra os players
            updateHidePlayersItem(player, "hide-enabled-item"); // Troca para o item de ativar
        }
    }

    // Aplica o estado de visibilidade ao jogador
    public void applyVisibilityState(Player player) {
        UUID playerId = player.getUniqueId();
        boolean isHidden = playerVisibilityState.getOrDefault(playerId, false);

        if (isHidden) {
            hidePlayers(player); // Esconde os players
            updateHidePlayersItem(player, "hide-disabled-item"); // Atualiza o item
        } else {
            showPlayers(player); // Mostra os players
            updateHidePlayersItem(player, "hide-enabled-item"); // Atualiza o item
        }
    }

    // Verifica se o item é o de ativar o vanish
    private boolean isHideEnabledItem(ItemStack item) {
        Material configMaterial = Material.getMaterial(plugin.getConfig().getString("hide-players.hide-enabled-item.material"));
        String configName = plugin.getConfig().getString("hide-players.hide-enabled-item.name");
        List<String> configLore = plugin.getConfig().getStringList("hide-players.hide-enabled-item.lore");

        if (item.getType() == configMaterial) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && configName != null && configName.equals(meta.getDisplayName())) {
                List<String> lore = meta.getLore();
                return lore != null && lore.equals(configLore);
            }
        }
        return false;
    }

    // Verifica se o item é o de desativar o vanish
    private boolean isHideDisabledItem(ItemStack item) {
        Material configMaterial = Material.getMaterial(plugin.getConfig().getString("hide-players.hide-disabled-item.material"));
        String configName = plugin.getConfig().getString("hide-players.hide-disabled-item.name");
        List<String> configLore = plugin.getConfig().getStringList("hide-players.hide-disabled-item.lore");

        if (item.getType() == configMaterial) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && configName != null && configName.equals(meta.getDisplayName())) {
                List<String> lore = meta.getLore();
                return lore != null && lore.equals(configLore);
            }
        }
        return false;
    }

    // Esconde os players para o jogador
    private void hidePlayers(Player player) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                player.hidePlayer(plugin, onlinePlayer);
            }
        }
        playerVisibilityState.put(player.getUniqueId(), true); // Atualiza o estado como "escondido"
    }

    // Mostra os players para o jogador
    private void showPlayers(Player player) {
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            player.showPlayer(plugin, onlinePlayer);
        }
        playerVisibilityState.put(player.getUniqueId(), false); // Atualiza o estado como "visível"
    }

    // Atualiza o item de "hide players" na hotbar do jogador
    public void updateHidePlayersItem(Player player, String configPath) {
        String lobbyWorldName = plugin.getConfig().getString("lobby.world");
        if (lobbyWorldName == null || !player.getWorld().getName().equals(lobbyWorldName)) {
            return; // Só atualiza o item no mundo do lobby
        }

        Material material = Material.getMaterial(plugin.getConfig().getString("hide-players." + configPath + ".material"));
        String name = plugin.getConfig().getString("hide-players." + configPath + ".name");
        List<String> lore = plugin.getConfig().getStringList("hide-players." + configPath + ".lore");
        int slot = plugin.getConfig().getInt("hide-players." + configPath + ".slot");

        if (material == null || name == null) {
            return;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        List<String> enchantments = plugin.getConfig().getStringList("hide-players." + configPath + ".enchantments");
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

        player.getInventory().clear(slot);
        player.getInventory().setItem(slot, item);
    }

    // Verifica se o jogador está em cooldown
    private boolean isOnCooldown(UUID playerId, double cooldown) {
        if (cooldowns.containsKey(playerId)) {
            long currentTime = System.currentTimeMillis();
            long lastUsedTime = cooldowns.get(playerId);
            return (currentTime - lastUsedTime) < (cooldown * 1000);
        }
        return false;
    }

    // Obtém o tempo restante do cooldown
    private double getRemainingCooldown(UUID playerId, double cooldown) {
        if (cooldowns.containsKey(playerId)) {
            long currentTime = System.currentTimeMillis();
            long lastUsedTime = cooldowns.get(playerId);
            return Math.max(0, cooldown - ((currentTime - lastUsedTime) / 1000.0));
        }
        return 0;
    }

    // Aplica o cooldown ao jogador
    private void applyCooldown(UUID playerId) {
        cooldowns.put(playerId, System.currentTimeMillis());
    }
}