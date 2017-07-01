# MultiplayerTicTacToe - Android Client

Dies ist der Web Client zum Multiplayer Tic Tac Toe Spiel.

----------

# Setup

### Projekt konfigurieren

Als einziges muss man den Service-Enpoint des Game-Servers konfigurieren.
Entweder laeuft der Server oeffentlich auf einem zentralen Webserver oder lokal auf dem Entwicklungsrechner.

#### a) zentraler Webserver
   Anpassen Configfile:<br>
   /src/main/res/xml/tictactoe_config.xml<br>
   ```html<ServiceEndpoint>https://warm-shelf-33316.herokuapp.com/</ServiceEndpoint>```

#### b) lokaler Entwicklungs-PC
   Anpassen Configfile:<br>
   /src/main/res/xml/tictactoe_config.xml<br>
   ```html<ServiceEndpoint>"lokale-ip-adresse-des-servers"</ServiceEndpoint>```
   Die Android App laeuft hierbei sicher auf dem Emulator. Aus Sicht App ist also localhost oder 127.0.0.1 der Emulator.<br>
   Der Server laeuft aber auf dem PC, somit darf man nicht localhost oder 127.0.0.1 als Server Endpoint verwenden.<br>
   Auf dem PC muss man sich die IP Adresse ermitteln (Windows-Terminal: ipconfig, Linux-Terminal: ifconfig)<br>
   zum Beispiel:<br>
   ```html<ServiceEndpoint>http://192.168.1.39:3100</ServiceEndpoint>```

Wenn man es sich nicht lokal installieren moechte, ist die App auch im Google App Store kostenlos verfuegbar.
Diese ist bereits so konfiguriert, dass der zentrale Webserver als Endpoint verwendet wird (oeffentlich).

----------

# Test

Die Tests werden mit folgendem Befehl ausgeführt:

``gradle test ``     TODO

----------

