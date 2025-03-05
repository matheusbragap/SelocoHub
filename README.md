# 🎮 SelecoHub

O **SelecoHub** é um plugin completo para proteção e gerenciamento de spawn em servidores de Minecraft. Com uma variedade de funcionalidades, ele garante que o spawn seja seguro, organizado e personalizável. 🛡️🌍

## 🚀 Recursos Principais

### 🛡️ Proteção Completa do Spawn
- **🚫 PvP:** Desative o PvP no spawn.
- **🍖 Fome:** Jogadores não perdem fome no spawn.
- **🪂 Queda:** Jogadores não sofrem dano de queda.
- **🌧️ Chuva:** Desative a chuva no spawn.
- **⚔️ Dano:** Jogadores não sofrem dano de qualquer tipo.
- **🔥 Queima de Blocos:** Blocos não pegam fogo.
- **📦 Drop de Itens:** Itens não podem ser dropados.
- **🎒 Movimentação de Inventário:** Bloqueie a movimentação de itens no inventário.
- **💥 Explosões:** Desative explosões no spawn.
- **🛠️ Dano a Itens:** Itens não sofrem dano.
- **🌞 Ciclo Diurno:** Desative o ciclo dia/noite.

### 🌍 Gerenciamento de Mundo e Spawn
- **🗺️ Setar Mundo de Spawn:** Defina o mundo de spawn com um comando.
- **📍 Setar Coordenadas do Spawn:** Defina as coordenadas exatas do spawn.
- **🕳️ Anti-Void:** Teleporta jogadores para o spawn caso caiam no void.
- **🚶 Teleporte Automático por Distância:** Teleporta jogadores para o spawn caso se afastem demais. É possível setar valores horizontais e verticais.
- **🧹 Clear Inventory:** Limpa o inventário do jogador ao entrar.
- **❤️ Heal Player:** Recupera a vida do jogador ao entrar.
- **🎮 Set Gamemode:** Define o GameMode ao entrar (SURVIVAL, CREATIVE, ADVENTURE ou SPECTATOR).
- **⚡ Efeitos:**
- **🏃‍♂️ Speed:** Aplica efeito de velocidade 1 ao jogador.
- **🦘 Jump Boost:** Aplica efeito de super pulo 1 ao jogador.

### 🛠️ Modo Build por Comando
- **🏗️ Ativar/Desativar Modo Build:** Controle a construção no spawn com um comando simples.

### 🔒 Sistema de Permissões
- **🛂 Permissões Personalizadas:** Controle quem pode usar cada funcionalidade.
- Permissão para todos os comandos: `seloco.*`

## 📥 Instalação
1. ⬇️ Baixe o arquivo `.jar` do plugin.
2. 📁 Coloque o arquivo na pasta `plugins` do seu servidor.
3. 🔄 Reinicie o servidor.
4. ⚙️ Configure o plugin conforme necessário no arquivo `config.yml`.

## 🎮 Comandos

| Comando              | Descrição                                   | Permissão          |
|---------------------|---------------------------------------------|--------------------|
| `/seloco ajuda`     | Exibe a lista de comandos disponíveis.      | `seloco.ajuda`     |
| `/seloco reload`    | Recarrega as configurações do plugin.       | `seloco.reload`    |
| `/seloco setlobby`  | Define o mundo e as coordenadas do lobby.   | `seloco.setlobby`  |
| `/seloco build`     | Habilita a função de construir no mapa do lobby. | `seloco.builder`   |

## ⚙️ Configuração
O arquivo `config.yml` permite personalizar as funcionalidades do plugin. Aqui está um exemplo:

```yaml
# Plugin: SelocoHub
# Autor: matheusdev
# Versão: 1.0.1 alpha
# Descrição: Um plugin para gerenciar o lobby e funcionalidades relacionadas.

###############################################
# CONFIGURAÇÕES GERAIS #
###############################################

# Proteção do spawn (true = ativada, false = desativada)
spawn_protection: true

###############################################
# LOBBY #
###############################################

# Localização do lobby
lobby:
  world: world  # Nome do mundo do lobby
  x: 0.0        # Coordenada X do lobby
  y: 0.0        # Coordenada Y do lobby
  z: 0.0        # Coordenada Z do lobby
  yaw: 0.0      # Rotação horizontal (yaw) do lobby
  pitch: 0.0    # Rotação vertical (pitch) do lobby

###############################################
# TELEPORTE AO ENTRAR NO SERVIDOR #
###############################################

tp-join-lobby:
  active: true          # Ativa ou desativa o teleporte ao lobby ao entrar no servidor
  only-world-lobby: false # Se true, só teleporta se o jogador deslogou no mundo do lobby

###############################################
# CONFIGURAÇÕES AO ENTRAR NO SERVIDOR #
###############################################

on-join-settings:
  clear-inventory: true # Limpa o inventário do jogador ao entrar
  heal-player: true # Enche a vida do jogador ao entrar
  set-gamemode: ADVENTURE # Define o GameMode ao entrar (Pode ser SURVIVAL, CREATIVE, ADVENTURE ou SPECTATOR)
  effects:
    speed: true # Aplica efeito de velocidade 1 ao jogador
    jump-boost: true # Aplica efeito de super pulo 1 ao jogador

###############################################
# CONFIGURAÇÕES DE JOGO #
###############################################

game-settings:
  disable-hunger: true         # Desativa a fome
  disable-damage: true         # Desativa o dano
  disable-rain: true           # Desativa a chuva
  disable-block-burn: true     # Desativa a queima de blocos
  disable-drop-item: true      # Desativa o drop de itens
  disable-item-move: true      # Desativa o movimento de itens no inventário
  disable-item-damage: true    # Desativa o dano a itens
  disable-pick-up-items: true  # Desativa a coleta de itens
  disable-explode: true        # Desativa explosões
  disable-daylight-cycle: true # Desativa o ciclo diurno

###############################################
# TELEPORTE POR DISTÂNCIA #
###############################################

teleport-distance:
  activate: true   # Ativa ou desativa o teleporte por distância
  horizontal: 150  # Distância horizontal máxima permitida
  vertical: 100    # Distância vertical máxima permitida

###############################################
# JUMPPADS #
###############################################

jumppads:
  enabled: false # Ativa ou desativa os JumpPads
  vertical: 1.5 # Potência vertical do JumpPad
  horizontal: 1.5 # Potência horizontal do JumpPad
  items:
    OAK_PRESSURE_PLATE: true
    SPRUCE_PRESSURE_PLATE: true
    BIRCH_PRESSURE_PLATE: true
    JUNGLE_PRESSURE_PLATE: true
    ACACIA_PRESSURE_PLATE: true
    DARK_OAK_PRESSURE_PLATE: true
    STONE_PRESSURE_PLATE: true
    HEAVY_WEIGHTED_PRESSURE_PLATE: true
    LIGHT_WEIGHTED_PRESSURE_PLATE: true
```

## 📜 Licença

Este projeto está licenciado sob a MIT License. Veja abaixo os detalhes:

```
MIT License

Copyright (c) 2023 Matheus

Permissão é concedida, gratuitamente, a qualquer pessoa que obtenha uma cópia deste software e arquivos de documentação associados (o "Software"), para lidar no Software sem restrições, incluindo, sem limitação, os direitos de usar, copiar, modificar, mesclar, publicar, distribuir, sublicenciar e/ou vender cópias do Software, e para permitir
