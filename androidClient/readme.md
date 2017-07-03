# Multiplayer TicTacToe - Android Client

Dies ist die Android App (Client) zum Multiplayer Tic Tac Toe Spiel.

----------

# Setup

### Projekt Setup
Klonen dieses Repositories (https://github.com/ibwgr/MultiplayerTicTacToe/tree/master/androidClient) auf den eigenen Entwicklungs-PC<br>
Am besten verwendet man Android Studio

### Projekt konfigurieren

Es muss nur der Service-Enpoint des Game-Servers konfiguriert werden.
Entweder laeuft der Server oeffentlich auf einem zentralen Webserver (was er sowieso tut) oder er läuft lokal auf dem eigenen Entwicklungsrechner.

#### a) zentraler Webserver
   Anpassen Configfile:<br>
   **/src/main/res/xml/tictactoe_config.xml**<br>
   ```<ServiceEndpoint>https://warm-shelf-33316.herokuapp.com/</ServiceEndpoint>```

#### b) lokale Entwicklungsmaschine
   Anpassen Configfile:<br>
   **/src/main/res/xml/tictactoe_config.xml**<br>
   ```<ServiceEndpoint>"lokale-ip-adresse-des-servers:3100"</ServiceEndpoint>```
   Die Android App laeuft hierbei sicher auf dem Emulator. Aus Sicht App ist also localhost oder 127.0.0.1 der Emulator.<br>
   Der Server laeuft aber auf dem PC, somit darf man nicht localhost oder 127.0.0.1 als Server Endpoint verwenden.<br>
   Auf dem PC muss man sich die IP Adresse ermitteln (Windows-Terminal: ipconfig, Linux-Terminal: ifconfig)<br>
   und diese dann eintragen. Der Port ist fix mit :3100 anzugeben. Zum Beispiel:<br>
   ```<ServiceEndpoint>http://192.168.1.39:3100</ServiceEndpoint>```

Wenn man es sich nicht lokal installieren moechte, ist die Android App auch im [Google Play Store](https://play.google.com/store/apps/details?id=ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe) kostenlos verfuegbar.
Diese ist bereits so konfiguriert, dass der zentrale Webserver als Endpoint verwendet wird (oeffentlich).


