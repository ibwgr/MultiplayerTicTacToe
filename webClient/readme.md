# MultiplayerTicTacToe - Web Client

Dies ist der Web Client zum Multiplayer Tic Tac Toe Spiel.

----------

# Setup

### Projekt installieren mit:

  ``yarn install ``

### Server URL kann unter config/config.json angepasst werden:

- Lokaler Entwicklungs-PC:

    ``{"serverUrl": "http://localhost:3100" } ``

- zentraler Webserver:

  ``{"serverUrl": "https://warm-shelf-33316.herokuapp.com/" } ``

### Server starten mit:

  ``gulp ``

----------

# Deployment

Deployment des Web Clients auf einen Web-Server:

- Anpassen der Sever URL im config/config.json File
- gulp ausführen
- Folgende Files/Verzeichnisse auf den Webserver kopieren:
  - bundle/bundle.js
  - index.html
  - style.css

----------
