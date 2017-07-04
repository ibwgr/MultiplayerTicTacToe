# Multiplayer Tic Tac Toe

Semesterarbeit NDK HF Web und Mobile Frontend Entwicklung<br>
Reto Kaufmann / Dieter Biedermann

## Spielbeschrieb

Bei TicTacToe spielen jeweils zwei Spieler gegeneinander. Die Spieler können sich mit einem selbstgewählten Spielername anmelden. Sobald sich zwei Spieler angemeldet haben, wird ein neues Spiel automatisch gestartet. Jeder Spieler hat jeweils 30 Sekunden Zeit für seinen Spielzug. Benötigt ein Spieler länger oder schliesst er den Browser bzw. die App, so hat dieser Spieler automatisch verloren. 

Gleichzeitig wird eine Statistik über alle laufenden und die zwanzig letzten abgeschlossenen Spiele angezeigt. Die Statistik wird nach jedem Zug aktualisiert. 

## Was ist Tic Tac Toe?

- [Tic-Tac-Toe auf Wikipedia](https://de.wikipedia.org/wiki/Tic-Tac-Toe)


## Details und Konfiguration Server
- [Server](https://github.com/ibwgr/MultiplayerTicTacToe/tree/master/server)
## Details und Konfiguration Clients
- [Web Client](https://github.com/ibwgr/MultiplayerTicTacToe/tree/master/webClient)
- [Android Client](https://github.com/ibwgr/MultiplayerTicTacToe/tree/master/androidClient)


## Produktive Version öffentlich Verfügbar
Folgende Clients stehen öffentlich zur Verfügung (bereits Deployed):<br>
- Web-Client direkt im [Browser](http://www.lastminute.li/aaa/) aufrufbar 
- Android App kann installiert werden via [Google Play Store](https://play.google.com/store/apps/details?id=ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe)

## Aufbau

Für die Verbindung zwischen Client und Server werden WebSockets verwendet (sockets.io). Der Client verbindet sich mit dem Server sobald der Web oder Android Client geöffnet wird. Der Client und der Server können dann Events senden und empfangen.

Da aber ältere Browser teilweise Probleme mit WebSockets haben, verwenden wir anstelle einer direkten WebSockets Programmierung, die JavaScript-Bibliothek Socket.io<br>
Diese kapselt die dahinterliegende Technik und wählt eine für die Übertragung bestmögliche Technologie.<br>
Socket.io verwendet alternativ zu WebSocket bei Bedarf Adobe Flash Socket, AJAX Long Polling, JAX Multipart Streaming, Forever Iframe oder JSONP Polling. Mit Heartbeats, Time-outs und Disconnection-Unterstützung komplettiert Socket.io den Umfang wichtiger Funktionen für die Konnektivität.<br>
Auch für Java/Android gibt es Libraries die Socket.io implementiert haben, sodass für Java wie auch für JavaScript fast dieselbe API zur Anwendung gelangt.<br>

![Komponenten](androidClient/documentation/TicTacToe-Komponentendiagramm-500x374.jpg?raw=true "Komonenten")
