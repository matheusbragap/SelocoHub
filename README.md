# 🎮 SelecoHub

O **SelecoHub** é um plugin completo para proteção e gerenciamento de spawn em servidores de Minecraft. Com uma variedade de funcionalidades, ele garante que o spawn seja seguro, organizado e personalizável. 🛡️🌍

---

## 🚀 Recursos Principais

### 🛡️ Proteção Completa do Spawn
- **🚫 PvP:** Desative o PvP no spawn.
- **🍖 Fome:** Jogadores não perdem fome no spawn.
- **🪂 Queda:** Jogadores não sofrem dano de queda.
- **🌧️ Chuva:** Desative a chuva no spawn.
- **⚔️ Dano:** Jogadores não sofrem dano de qualquer tipo.
- **🦇 Disable Creature Spawn:** Desative o spawn de criaturas no lobby.
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

### 🎁 Itens Personalizados no Lobby
- **Itens na Hotbar:** Ao entrar no lobby, os jogadores recebem itens personalizados na hotbar.
- **Personalização Completa:** Configure os itens com:
    - **Nome (`name`)**
    - **Descrição (`lore`)**
    - **Ações (`actions`)**
    - **Encantamentos (`enchantments`)**
- **Funções Predefinidas:** Defina ações como abrir menus, executar comandos ou reproduzir sons.
- **Exemplo de Configuração:**
  ```yaml
  hotbar-lobby:
    activate: true
    items:
      - material: COMPASS
        name: §6Seletor
        lore:
          - §7Clique para abrir o menu!
          - §aUse com sabedoria.
        slot: 0
        enchantments:
          - SHARPNESS:1
        actions:
          - '[console] dm open lobby_menu %player%'
          - '[sound] ENTITY_ARROW_SHOOT'
      - material: PAPER
        name: §6Créditos
        lore:
          - §7Veja os créditos do servidor!
          - §aDesenvolvido por @matheusbragap.
        slot: 1
        enchantments:
          - SHARPNESS:1
        actions:
          - '[message] discord: @matheusbragap'
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
# Versão: 1.0.2 alpha
# Descrição: Um plugin para gerenciar o lobby e funcionalidades relacionadas.

###############################################
# CONFIGURAÇÕES GERAIS #
###############################################

# Proteção do spawn (true = ativada, false = desativada)
# Impede que jogadores quebrem ou coloquem blocos no spawn.
spawn_protection: true

###############################################
# LOBBY #
###############################################

# Localização do lobby
# Define as coordenadas e rotação do ponto de spawn do lobby.
lobby:
  world: world  # Nome do mundo do lobby
  x: 0.0        # Coordenada X do lobby
  y: 0.0        # Coordenada Y do lobby
  z: 0.0        # Coordenada Z do lobby
  yaw: 0.0      # Rotação horizontal (yaw) do lobby (0 = norte, 90 = leste, 180 = sul, 270 = oeste)
  pitch: 0.0    # Rotação vertical (pitch) do lobby (-90 = olhando para cima, 90 = olhando para baixo)

###############################################
# TELEPORTE AO ENTRAR NO SERVIDOR #
###############################################

# Configurações de teleporte ao entrar no servidor
tp-join-lobby:
  active: true          # Ativa ou desativa o teleporte ao lobby ao entrar no servidor
  only-world-lobby: false # Se true, só teleporta se o jogador deslogou no mundo do lobby. Caso contrário, teleporta independentemente do mundo.

###############################################
# CONFIGURAÇÕES AO ENTRAR NO SERVIDOR #
###############################################

# Configurações aplicadas ao jogador ao entrar no servidor
on-join-settings:
  clear-inventory: true # Limpa o inventário do jogador ao entrar
  heal-player: true     # Enche a vida do jogador ao entrar
  set-gamemode: ADVENTURE # Define o GameMode ao entrar. Valores possíveis: SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR
  effects:
    speed: true         # Aplica efeito de velocidade 1 ao jogador
    jump-boost: true    # Aplica efeito de super pulo 1 ao jogador

###############################################
# CONFIGURAÇÕES DE JOGO #
###############################################

# Configurações que afetam o comportamento do jogo no lobby
game-settings:
  disable-hunger: true         # Desativa a fome (jogadores não perdem fome)
  disable-damage: true         # Desativa o dano (jogadores não sofrem dano)
  disable-rain: true           # Desativa a chuva no mundo do lobby
  disable-block-burn: true     # Desativa a queima de blocos (fogo não se espalha)
  disable-drop-item: true      # Desativa o drop de itens (jogadores não podem soltar itens)
  disable-item-move: true      # Desativa o movimento de itens no inventário (jogadores não podem reorganizar itens)
  disable-item-damage: true    # Desativa o dano a itens (itens não perdem durabilidade)
  disable-pick-up-items: true  # Desativa a coleta de itens (jogadores não podem pegar itens do chão)
  disable-explode: true        # Desativa explosões (TNT, creepers, etc.)
  disable-daylight-cycle: true # Desativa o ciclo diurno (o tempo não muda)

###############################################
# TELEPORTE POR DISTÂNCIA #
###############################################

# Configurações de teleporte automático ao ultrapassar uma distância máxima
teleport-distance:
  activate: true   # Ativa ou desativa o teleporte por distância
  horizontal: 150  # Distância horizontal máxima permitida (em blocos)
  vertical: 100    # Distância vertical máxima permitida (em blocos)

###############################################
# JUMPPADS #
###############################################

# Configurações dos JumpPads (placas de pressão que impulsionam o jogador)
jumppads:
  activate: true # Ativa ou desativa os JumpPads
  vertical: 1.0  # Potência vertical do JumpPad (quanto maior, mais alto o jogador é impulsionado)
  horizontal: 1.5 # Potência horizontal do JumpPad (quanto maior, mais longe o jogador é impulsionado)
  items:
    - OAK_PRESSURE_PLATE
    - SPRUCE_PRESSURE_PLATE
    - BIRCH_PRESSURE_PLATE
    - JUNGLE_PRESSURE_PLATE
    - ACACIA_PRESSURE_PLATE
    - DARK_OAK_PRESSURE_PLATE
    - STONE_PRESSURE_PLATE
    - HEAVY_WEIGHTED_PRESSURE_PLATE
    - LIGHT_WEIGHTED_PRESSURE_PLATE

###############################################
# ITEMS #
###############################################

# Itens na hotbar ao entrar no mundo do lobby
hotbar-lobby:
  activate: true # Ativa ou desativa os itens da hotbar no lobby
  items:
    - material: COMPASS
      name: §6Seletor
      lore:
        - §7Clique para abrir o menu!
        - §aUse com sabedoria.
      slot: 0     # Slot da hotbar (0 = primeiro slot, 1 = segundo slot, etc.)
      enchantments:
        - SHARPNESS:1 # Adiciona encantamento ao item (neste caso, Sharpness 1)
      actions:
        - '[console] dm open lobby_menu %player%' # Executa um comando no console
        - '[sound] ENTITY_ARROW_SHOOT'            # Toca um som para o jogador
    - material: PAPER
      name: §6Créditos
      lore:
        - §7Veja os créditos do servidor!
        - §aDesenvolvido por @matheusbragap.
      slot: 1
      enchantments:
        - SHARPNESS:1
      actions:
        - '[message] discord: @matheusbragap'     # Envia uma mensagem ao jogador

# Exemplo de todas as ações possíveis:
# - [console] <comando>: Executa um comando no console.
#   Exemplo: [console] give %player% diamond 1
# - [player] <comando>: Executa um comando como o jogador.
#   Exemplo: [player] spawn
# - [message] <mensagem>: Envia uma mensagem ao jogador.
#   Exemplo: [message] Bem-vindo ao servidor!
# - [broadcastsound] <som>: Toca um som para todos os jogadores.
#   Exemplo: [broadcastsound] ENTITY_ENDER_DRAGON_GROWL
# - [sound] <som>: Toca um som apenas para o jogador.
#   Exemplo: [sound] ENTITY_PLAYER_LEVELUP
```

## 📜 Licença

Este projeto está licenciado sob a MIT License. Veja abaixo os detalhes:

```
MIT License

Copyright (c) 2023 Matheus

Permissão é concedida, gratuitamente, a qualquer pessoa que obtenha uma cópia deste software e arquivos de documentação associados (o "Software"), para lidar no Software sem restrições, incluindo, sem limitação, os direitos de usar, copiar, modificar, mesclar, publicar, distribuir, sublicenciar e/ou vender cópias do Software, e para permitir
