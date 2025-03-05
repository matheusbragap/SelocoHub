package com.matheusdev.selocoplugin.selecoHub.commands;

import org.bukkit.command.CommandSender;

public class HelpCommandsList {
    public static void execute(CommandSender sender) {
        // Cabeçalho
        sender.sendMessage("§6§l» §e§lCOMANDOS DO SELOCOHUB §6§l«");
        sender.sendMessage("§7§m----------------------------------------");

        // Comandos
        sender.sendMessage("§a/seloco ajuda");
        sender.sendMessage("§fExibe essa lista de comandos.");
        sender.sendMessage("");

        sender.sendMessage("§a/seloco setlobby");
        sender.sendMessage("§fDefine o lobby para o servidor.");
        sender.sendMessage("");

        sender.sendMessage("§a/seloco build");
        sender.sendMessage("§fAtiva ou desativa o modo de construção.");
        sender.sendMessage("");

        sender.sendMessage("§a/seloco reload");
        sender.sendMessage("§fRecarrega as configurações do plugin.");
        sender.sendMessage("");

        // Rodapé
        sender.sendMessage("§7§m----------------------------------------");
        sender.sendMessage("§6§l» §e§lDICA: §fUse §a/seloco ajuda §fpara ver esta lista novamente.");
    }
}