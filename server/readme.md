# MultiplayerTicTacToe - Server

Dies ist der nodeJS Server für das Multiplayer Tic Tac Toe Spiel.

----------

# Setup

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
- ``heroku git:remote -a`` <i>heroku-app-name</i>
- ``git push heroku master``

----------

