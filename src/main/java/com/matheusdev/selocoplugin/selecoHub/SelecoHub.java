package com.matheusdev.selocoplugin.selecoHub;

import com.matheusdev.selocoplugin.selecoHub.commands.SpawnTeleport;
import com.matheusdev.selocoplugin.selecoHub.listeners.ProtectWorld;
import com.matheusdev.selocoplugin.selecoHub.listeners.PlayerJoin;
import com.matheusdev.selocoplugin.selecoHub.listeners.GameSettings;
import com.matheusdev.selocoplugin.selecoHub.listeners.DistanceVoid;
import com.matheusdev.selocoplugin.selecoHub.management.AutoCompleter;
import com.matheusdev.selocoplugin.selecoHub.management.Commands;
import com.matheusdev.selocoplugin.selecoHub.management.Permissions;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SelecoHub extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ProtectWorld protectWorld = new ProtectWorld(this);
        Permissions permissions = new Permissions();
        Commands commands = new Commands(this, protectWorld, permissions);
        AutoCompleter autoCompleter = new AutoCompleter(permissions);

        // Registra o comando "seloco" com o CommandManager e AutoCompleter
        PluginCommand selocoCommand = getCommand("seloco");
        if (selocoCommand != null) {
            // Define uma permissão padrão para o comando /seloco
            selocoCommand.setPermission(Permissions.PERMISSAO_BUILDER + ";" + Permissions.PERMISSAO_TUDO);
            selocoCommand.setExecutor(commands);
            selocoCommand.setTabCompleter(autoCompleter);
        } else {
            getLogger().warning("O comando /seloco não está registrado no plugin.yml!");
        }

        // Registra o comando /lobby e seus aliases
        PluginCommand lobbyCommand = getCommand("lobby");
        if (lobbyCommand != null) {
            lobbyCommand.setExecutor(new SpawnTeleport(this));
            lobbyCommand.setAliases(getConfig().getStringList("lobby_command.aliases"));
        } else {
            getLogger().warning("O comando /lobby não está registrado no plugin.yml!");
        }

        // Registra os listeners
        getServer().getPluginManager().registerEvents(protectWorld, this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new GameSettings(this), this);
        getServer().getPluginManager().registerEvents(new DistanceVoid(this), this);
        getLogger().info("Plugin SelecoHub carregado com sucesso!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        getLogger().info("Plugin SelecoHub desativado.");
    }
}