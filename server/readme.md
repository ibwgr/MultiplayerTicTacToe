# MultiplayerTicTacToe - Server

Dies ist der node.js Server für das Multiplayer Tic Tac Toe Spiel.

----------

# Setup

### ganzes Projekt klonen

``git clone https://github.com/ibwgr/MultiplayerTicTacToe.git``

### in das Server Verzeichnis wechseln

``cd MultiplayerTicTacToe/server``

### Projekt installieren mit:

``yarn install ``

### Server starten für Development mit:

``npm run start-nodemon ``

----------

# Test

Die Tests werden mit folgendem Befehl ausgeführt:

``npm run test ``

----------

# Deployment auf einen Server

Am Beispiel von Heroku. Der start Befehl im package.json ist bereits für Heroku konfiguriert.

- ``git init``
- ``git add .``
- ``git commit -m "first commit"``
- ``heroku login``
- ``heroku git:remote -a`` *heroku-app-name*
- ``git push heroku master``

----------

