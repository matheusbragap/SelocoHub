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

        if (command.getName().equalsIgnoreCase("seloco")) {
            if (args.length == 1) {
                completions.add("ajuda");
                completions.add("setlobby");
                completions.add("build");
                completions.add("reload");
            }
            // Removido o caso para sugerir IDs de blocos
        }

        return completions;
    }
}