package com.matheusdev.selocoplugin.selecoHub.management;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleter implements TabCompleter {
    private final Permissions permissions;

    public AutoCompleter(Permissions permissions) {
        this.permissions = permissions;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        // Verifica se o comando é /seloco
        if (command.getName().equalsIgnoreCase("seloco")) {
            // Verifica se o jogador tem pelo menos uma permissão para usar o comando /seloco
            if (!hasAnyPermission(sender)) {
                return completions; // Retorna uma lista vazia se o jogador não tiver nenhuma permissão
            }

            // Adiciona sugestões apenas se o jogador tiver permissão para cada subcomando
            if (args.length == 1) {
                if (permissions.canUseAjuda(sender)) {
                    completions.add("ajuda");
                }
                if (permissions.canUseSetLobby(sender)) {
                    completions.add("setlobby");
                }
                if (permissions.canUseBuild(sender)) {
                    completions.add("build");
                }
                if (permissions.canUseReload(sender)) {
                    completions.add("reload");
                }
            }
        }

        return completions;
    }

    // Verifica se o jogador tem pelo menos uma permissão para usar o comando /seloco
    private boolean hasAnyPermission(CommandSender sender) {
        return permissions.canUseAjuda(sender) ||
                permissions.canUseSetLobby(sender) ||
                permissions.canUseBuild(sender) ||
                permissions.canUseReload(sender);
    }
}