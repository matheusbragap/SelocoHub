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

## 🔑 Permissões
- `seloco.builder`: Permite usar os comandos `/seloco ajuda` e `/seloco build`.
- `seloco.*`: Permite usar todos os comandos do plugin.
- `seloco.ajuda`: Permite usar o comando `/seloco ajuda`.
- `seloco.setlobby`: Permite usar o comando `/seloco setlobby`.
- `seloco.build`: Permite usar o comando `/seloco build`.
- `seloco.reload`: Permite usar o comando `/seloco reload`.

## ⚙️ Configuração
O arquivo `config.yml` permite personalizar as funcionalidades do plugin. Aqui está um exemplo:

```yaml
# Configurações do plugin SelecoHub

# Proteção do spawn (true = ativada, false = desativada)
spawn_protection: true

# Localização do lobby
lobby:
  world: world
  x: 0.0
  y: 0.0
  z: 0.0
  yaw: 0.0
  pitch: 0.0
  void-layer: -64
  horizontal-distance: 100.0

game-settings:
  disable-hunger: true
  disable-damage: true
  disable-rain: true
  disable-block-burn: true
  disable-drop-item: true
  disable-item-move: true
  disable-item-damage: true
  disable-pick-up-items: true
  disable-explode: true
  disable-daylight-cycle: true

# Configurações do comando /lobby
lobby_command:
  aliases:
    - spawn
    - hub
    - l
```

## 📜 Licença

Este projeto está licenciado sob a MIT License. Veja abaixo os detalhes:

```
MIT License

Copyright (c) 2023 Matheus

Permissão é concedida, gratuitamente, a qualquer pessoa que obtenha uma cópia deste software e arquivos de documentação associados (o "Software"), para lidar no Software sem restrições, incluindo, sem limitação, os direitos de usar, copiar, modificar, mesclar, publicar, distribuir, sublicenciar e/ou vender cópias do Software, e para permitir
