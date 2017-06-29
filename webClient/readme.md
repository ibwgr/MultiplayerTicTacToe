# MultiplayerTicTacToe - Web Client

Dies ist der Web Client zum Multiplayer Tic Tac Toe Spiel.

----------

# Setup

- Projekt installieren mit:

  ``yarn install ``

- Serer URL kann unter config/config.json angepasst werden:

  ``{"serverUrl": "http://localhost:8008" } ``

- Server starten mit:

  ``gulp ``

----------

# Deployment

Deployment des Web Clients auf einen Web-Server:

- Anpassen der Sever URL im config/config.json File
- Folgende Files/Verzeichnisse auf den Webserver kopieren:
  - bundle/bundle.js
  - index.html
  - style.css

----------
