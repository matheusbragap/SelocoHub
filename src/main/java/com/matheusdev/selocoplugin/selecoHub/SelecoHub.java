package com.matheusdev.selocoplugin.selecoHub;

import com.matheusdev.selocoplugin.selecoHub.commands.PluginReload;
import com.matheusdev.selocoplugin.selecoHub.commands.SpawnTeleport;
import com.matheusdev.selocoplugin.selecoHub.listeners.ProtectWorld;
import com.matheusdev.selocoplugin.selecoHub.listeners.TpJoin;
import com.matheusdev.selocoplugin.selecoHub.listeners.GameSettings;
import com.matheusdev.selocoplugin.selecoHub.listeners.DistanceVoid;
import com.matheusdev.selocoplugin.selecoHub.listeners.PlayerJoin;
import com.matheusdev.selocoplugin.selecoHub.listeners.JumpPads;
import com.matheusdev.selocoplugin.selecoHub.management.AutoCompleter;
import com.matheusdev.selocoplugin.selecoHub.management.Commands;
import com.matheusdev.selocoplugin.selecoHub.management.Permissions;
import com.matheusdev.selocoplugin.selecoHub.inventory.OnJoin;
import com.matheusdev.selocoplugin.selecoHub.inventory.ClickItems;
import com.matheusdev.selocoplugin.selecoHub.inventory.ExecuteItemAction;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SelecoHub extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Instancia as classes necessárias
        ProtectWorld protectWorld = new ProtectWorld(this);
        Permissions permissions = new Permissions();
        JumpPads jumpPads = new JumpPads(this);
        Commands commands = new Commands(this, protectWorld, permissions, jumpPads); // Passa a instância de JumpPads
        AutoCompleter autoCompleter = new AutoCompleter(permissions);

        // Registra o comando /seloco
        PluginCommand selocoCommand = getCommand("seloco");
        if (selocoCommand != null) {
            selocoCommand.setExecutor(commands);
            selocoCommand.setTabCompleter(autoCompleter);
        } else {
            getLogger().warning("O comando /seloco não está registrado no plugin.yml!");
        }

        // Registra o comando /lobby
        PluginCommand lobbyCommand = getCommand("lobby");
        if (lobbyCommand != null) {
            lobbyCommand.setExecutor(new SpawnTeleport(this));
            lobbyCommand.setAliases(getConfig().getStringList("lobby_command.aliases"));
        } else {
            getLogger().warning("O comando /lobby não está registrado no plugin.yml!");
        }

        // Registra os listeners
        getServer().getPluginManager().registerEvents(protectWorld, this);
        getServer().getPluginManager().registerEvents(new TpJoin(this), this);
        getServer().getPluginManager().registerEvents(new GameSettings(this), this);
        getServer().getPluginManager().registerEvents(new DistanceVoid(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(jumpPads, this);
        getServer().getPluginManager().registerEvents(new OnJoin(this), this);

        // Instancia a classe ExecuteItemAction
        ExecuteItemAction executeItemAction = new ExecuteItemAction(this);

        // Registra os listeners de inventário
        getServer().getPluginManager().registerEvents(new ClickItems(this, executeItemAction), this);

        // Executa a lógica de recarregar a configuração ao ligar o plugin
        PluginReload pluginReload = new PluginReload(this, jumpPads);
        pluginReload.execute(getServer().getConsoleSender(), true); // Passa true para sobrescrever alterações

        getLogger().info("Plugin SelecoHub carregado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Chama a lógica de recarregar a configuração ao desligar o plugin
        PluginReload pluginReload = new PluginReload(this, new JumpPads(this));
        pluginReload.execute(getServer().getConsoleSender(), true); // Passa true para sobrescrever alterações
        getLogger().info("Plugin SelecoHub desativado.");
    }
}