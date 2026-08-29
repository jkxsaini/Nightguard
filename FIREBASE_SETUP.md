# Nightguard – Firebase / Firestore Setup

Die App ist für Cloud Firestore vorbereitet und verwendet Google Maps für die Gefahrenkarte.

## 1. Firebase-Verbindung

Die Android-App verwendet den Package-Namen:

`com.example.nightguard`

Die projektbezogene Datei muss hier liegen:

`app/google-services.json`

Danach in Android Studio **Sync Project with Gradle Files** ausführen.

## 2. Cloud Firestore aktivieren

1. Firebase Console -> **Firestore Database**.
2. Datenbank erstellen.
3. Für Deutschland/Europa möglichst eine europäische Region wählen.
4. Unter **Rules** den Inhalt der Datei `firestore.rules` aus diesem Projekt einfügen und veröffentlichen.

## 3. Unsicheren Bereich anlegen

- Auf der Google Map eine Position **lange drücken**.
- Im Dialog den Radius zwischen 50 und 500 Metern auswählen.
- Optional eine Nachricht mit bis zu 300 Zeichen eingeben.
- Auf **Speichern** drücken.
- Der Bereich wird in der Collection `unsafeAreas` gespeichert und über den Firestore-Echtzeitlistener auf allen laufenden Clients aktualisiert.

Ein Dokument sieht beispielsweise so aus:

```text
unsafeAreas/{documentId}
  latitude: 51.2277
  longitude: 6.7735
  radiusMeters: 120
  label: "Unsicherer Bereich"
  message: "Unterführung ist nachts schlecht beleuchtet"
  active: true
  createdAt: <server timestamp>
```

## 4. Anzeige auf Google Maps

- Jeder Bereich wird als halbtransparenter roter Kreis mit dem tatsächlich gespeicherten Radius dargestellt.
- Im Mittelpunkt befindet sich ein Marker.
- Beim Antippen des Markers werden Radius und die optionale Nachricht angezeigt.
- Bereits vorhandene ältere Firestore-Dokumente ohne `message` werden weiterhin geladen; die Nachricht bleibt dann einfach leer.

## 5. Firestore-Regeln aktualisieren

Nach diesem Update muss die Datei `firestore.rules` erneut in der Firebase Console unter **Firestore Database -> Regeln** veröffentlicht werden, weil neue Dokumente jetzt zusätzlich das Feld `message` enthalten.

## Sicherheit

Die beiliegenden Regeln sind weiterhin Prototyp-Regeln: Lesen und das Anlegen validierter Gefahrenbereiche ist öffentlich möglich. Update und Delete sind gesperrt. Vor einem öffentlichen Release sollten Firebase Authentication und/oder ein Moderationsmechanismus ergänzt werden.
