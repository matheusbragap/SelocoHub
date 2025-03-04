package com.matheusdev.selocoplugin.selecoHub.management;

import org.bukkit.command.CommandSender;

public class Permissions {
    // Permissões
    public static final String PERMISSAO_BUILDER = "seloco.builder"; // Permissão para builder
    public static final String PERMISSAO_TUDO = "seloco.*"; // Permissão para tudo
    public static final String PERMISSAO_AJUDA = "seloco.ajuda";
    public static final String PERMISSAO_SETLOBBY = "seloco.setlobby";
    public static final String PERMISSAO_BUILD = "seloco.build";
    public static final String PERMISSAO_RELOAD = "seloco.reload";

    // Verifica se o jogador tem permissão para usar o comando /seloco
    public boolean canUseSeloco(CommandSender sender) {
        return sender.hasPermission(PERMISSAO_BUILDER) || sender.hasPermission(PERMISSAO_TUDO);
    }

    // Verifica permissões específicas para subcomandos
    public boolean canUseAjuda(CommandSender sender) {
        return sender.hasPermission(PERMISSAO_AJUDA) || sender.hasPermission(PERMISSAO_BUILDER) || sender.hasPermission(PERMISSAO_TUDO);
    }

    public boolean canUseSetLobby(CommandSender sender) {
        return sender.hasPermission(PERMISSAO_SETLOBBY) || sender.hasPermission(PERMISSAO_TUDO);
    }

    public boolean canUseBuild(CommandSender sender) {
        return sender.hasPermission(PERMISSAO_BUILD) || sender.hasPermission(PERMISSAO_BUILDER) || sender.hasPermission(PERMISSAO_TUDO);
    }

    public boolean canUseReload(CommandSender sender) {
        return sender.hasPermission(PERMISSAO_RELOAD) || sender.hasPermission(PERMISSAO_TUDO);
    }
}