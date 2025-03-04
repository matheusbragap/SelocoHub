package com.matheusdev.selocoplugin.selecoHub.management;

import com.matheusdev.selocoplugin.selecoHub.commands.*;
import com.matheusdev.selocoplugin.selecoHub.listeners.ProtectWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Commands implements CommandExecutor {
    private final JavaPlugin plugin;
    private final ProtectWorld protectWorld;
    private final Permissions permissions;

    public Commands(JavaPlugin plugin, ProtectWorld protectWorld, Permissions permissions) {
        this.plugin = plugin;
        this.protectWorld = protectWorld;
        this.permissions = permissions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("seloco")) {
            // Verifica se o jogador tem permissão para usar o comando /seloco
            if (!permissions.canUseSeloco(sender)) {
                sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("§cUso correto: /seloco <comando>");
                return true;
            }

            String subcomando = args[0].toLowerCase();

            switch (subcomando) {
                case "ajuda":
                    if (permissions.canUseAjuda(sender)) {
                        new HelpCommandsList().execute(sender);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                case "setlobby":
                    if (permissions.canUseSetLobby(sender)) {
                        new SetLobby(plugin).execute(sender);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                case "build":
                    if (permissions.canUseBuild(sender)) {
                        BuilderMode builderMode = new BuilderMode(plugin, protectWorld);
                        builderMode.execute(sender);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                case "reload":
                    if (permissions.canUseReload(sender)) {
                        new PluginReload(plugin).execute(sender);
                    } else {
                        sender.sendMessage("§cVocê não tem permissão para usar este comando.");
                    }
                    break;
                default:
                    sender.sendMessage("§cComando desconhecido!");
                    break;
            }
            return true;
        }
        return false;
    }
}