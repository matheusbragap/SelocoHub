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
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("seloco")) {
            // Se o jogador não tiver permissão para usar /seloco, retorna lista vazia
            if (!permissions.canUseSeloco(sender)) {
                return suggestions;
            }

            if (args.length == 1) {
                // Builder pode ver apenas "ajuda" e "build"
                if (sender.hasPermission(Permissions.PERMISSAO_BUILDER)) {
                    suggestions.add("ajuda");
                    suggestions.add("build");
                }
                // Permissão "tudo" pode ver todos os comandos
                if (sender.hasPermission(Permissions.PERMISSAO_TUDO)) {
                    suggestions.add("ajuda");
                    suggestions.add("setlobby");
                    suggestions.add("build");
                    suggestions.add("reload");
                }
            }
        }
        return suggestions;
    }
}