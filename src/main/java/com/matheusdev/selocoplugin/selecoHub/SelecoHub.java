package com.matheusdev.selocoplugin.selecoHub;

import com.matheusdev.selocoplugin.selecoHub.commands.PluginReload;
import com.matheusdev.selocoplugin.selecoHub.commands.SpawnTeleport;
import com.matheusdev.selocoplugin.selecoHub.listeners.*;
import com.matheusdev.selocoplugin.selecoHub.management.AutoCompleter;
import com.matheusdev.selocoplugin.selecoHub.management.Commands;
import com.matheusdev.selocoplugin.selecoHub.management.Permissions;
import com.matheusdev.selocoplugin.selecoHub.inventory.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SelecoHub extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ProtectWorld protectWorld = new ProtectWorld(this);
        Permissions permissions = new Permissions();
        JumpPads jumpPads = new JumpPads(this);
        GameSettings gameSettings = new GameSettings(this);

        Commands commands = new Commands(this, protectWorld, permissions, jumpPads, gameSettings);
        AutoCompleter autoCompleter = new AutoCompleter(permissions);

        PluginCommand selocoCommand = getCommand("seloco");
        if (selocoCommand != null) {
            selocoCommand.setExecutor(commands);
            selocoCommand.setTabCompleter(autoCompleter);
        } else {
            getLogger().warning("O comando /seloco não está registrado no plugin.yml!");
        }

        PluginCommand lobbyCommand = getCommand("lobby");
        if (lobbyCommand != null) {
            lobbyCommand.setExecutor(new SpawnTeleport(this));
            lobbyCommand.setAliases(getConfig().getStringList("lobby_command.aliases"));
        } else {
            getLogger().warning("O comando /lobby não está registrado no plugin.yml!");
        }

        getServer().getPluginManager().registerEvents(protectWorld, this);
        getServer().getPluginManager().registerEvents(new TpJoin(this), this);
        getServer().getPluginManager().registerEvents(gameSettings, this);
        getServer().getPluginManager().registerEvents(new DistanceVoid(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(jumpPads, this);

        // Registrando a classe HidePlayers
        HidePlayers hidePlayers = new HidePlayers(this);
        getServer().getPluginManager().registerEvents(hidePlayers, this);

        // Registrando a classe OnJoin com a instância de HidePlayers
        getServer().getPluginManager().registerEvents(new OnJoin(this, hidePlayers), this);

        ExecuteItemAction executeItemAction = new ExecuteItemAction(this);
        getServer().getPluginManager().registerEvents(new ClickItems(this, executeItemAction), this);

        PluginReload pluginReload = new PluginReload(this, jumpPads, gameSettings);
        pluginReload.execute(getServer().getConsoleSender(), true);

        getLogger().info("Plugin SelecoHub carregado com sucesso!");
    }

    @Override
    public void onDisable() {
        PluginReload pluginReload = new PluginReload(this, new JumpPads(this), new GameSettings(this));
        pluginReload.execute(getServer().getConsoleSender(), true);
        getLogger().info("Plugin SelecoHub desativado.");
    }
}