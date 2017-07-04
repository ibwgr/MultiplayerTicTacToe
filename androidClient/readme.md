# Multiplayer TicTacToe - Android Client

Dies ist die Android App (Client) zum Multiplayer Tic Tac Toe Spiel.

----------

# Setup

### Projekt Setup
Klonen dieses Repositories auf den eigenen Entwicklungs-PC<br>
Am besten verwendet man Android Studio

- ganzes Projekt klonen

   ``git clone https://github.com/ibwgr/MultiplayerTicTacToe.git``

- in das androidClient Verzeichnis wechseln

   ``cd androidClient``

### Projekt konfigurieren

Es muss nur der Service-Enpoint des Game-Servers konfiguriert werden.
Entweder laeuft der Server oeffentlich auf einem zentralen Webserver (was er sowieso tut) oder er läuft lokal auf dem eigenen Entwicklungsrechner.

#### a) zentraler Webserver
   Anpassen Configfile: **/src/main/res/xml/tictactoe_config.xml**<br>
   
   ```<ServiceEndpoint>https://warm-shelf-33316.herokuapp.com/</ServiceEndpoint>```

#### b) lokale Entwicklungsmaschine
   Anpassen Configfile: **/src/main/res/xml/tictactoe_config.xml**<br>
   
   ```<ServiceEndpoint>"lokale-ip-adresse-des-servers:3100"</ServiceEndpoint>```<br>
   
   Die Android App laeuft hierbei sicher auf dem Emulator. Aus Sicht App ist also localhost oder 127.0.0.1 der Emulator.<br>
   Der Server laeuft aber auf dem PC, somit darf man nicht localhost oder 127.0.0.1 als Server Endpoint verwenden.<br>
   Auf dem PC muss man sich die IP Adresse ermitteln (Windows-Terminal: ipconfig, Linux-Terminal: ifconfig)<br>
   und diese dann eintragen. Der Port ist fix mit :3100 anzugeben.<br>Zum Beispiel:<br>
   ```<ServiceEndpoint>http://192.168.1.39:3100</ServiceEndpoint>```

Wenn man es sich nicht lokal installieren moechte, ist die Android App auch im **[Google Play Store](https://play.google.com/store/apps/details?id=ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe)** kostenlos verfuegbar.
Diese ist bereits so konfiguriert, dass der zentrale Webserver als Endpoint verwendet wird (oeffentlich).

### Technische Dokumentation
Da für den JS Server und den JS-Web-Client socket.io verwendet wurde, macht es Sinn, auch hier für den Android auf socket.io zurückzugreifen.<br>
Die für unsere Zwecke beste Java Implementierung davon ist in der Library com.github.nkzawa:socket.io-client:0.3.0 aufzufinden.
Diese ist unter der MIT Licence frei verfügbar (siehe ganz unten).

#### Artefakte
Grob zusammengefasst sind dies die wichtigsten Artefakte in unserem Android Projekt Sourcecode Verzeichnis:<br>
![Diagramm Android Klassen/LayoutViews](documentation/TicTacToe-Android-Diagramm-800x646.png?raw=true "Diagramm Android Klassen/LayoutViews")


#### Test
Die Unit-Tests können via Commandline wie folgt gestartet werden:<br>
``./gradlew test``


### License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
