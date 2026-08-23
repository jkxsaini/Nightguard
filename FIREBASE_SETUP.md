# Nightguard – Firebase / Firestore Setup

Die App ist bereits für Cloud Firestore vorbereitet. Es fehlt nur noch die projektbezogene Firebase-Konfigurationsdatei.

## 1. Firebase-Projekt anlegen

1. Öffne die Firebase Console.
2. Erstelle ein Projekt (z. B. `Nightguard`).
3. Füge eine **Android-App** hinzu.
4. Verwende exakt diesen Package-Namen:
   `com.example.nightguard`

## 2. google-services.json hinzufügen

1. Lade in Firebase die Datei `google-services.json` herunter.
2. Lege sie hier ab:
   `Nightguard/app/google-services.json`
3. Führe in Android Studio **Sync Project with Gradle Files** aus.

Die Datei ist projektspezifisch und kann deshalb nicht sinnvoll vorab in dieses ZIP eingebaut werden.

## 3. Cloud Firestore aktivieren

1. Firebase Console -> **Firestore Database**.
2. Datenbank erstellen.
3. Für Deutschland/Europa möglichst eine europäische Region wählen.
4. Unter **Rules** den Inhalt der Datei `firestore.rules` aus diesem Projekt einfügen und veröffentlichen.

## 4. So funktioniert es in der App

- Die Collection heißt `unsafeAreas`.
- Beim Öffnen der Startseite werden alle aktiven Bereiche in Echtzeit geladen.
- Jeder Bereich wird als halbtransparenter roter Kreis auf der OpenStreetMap angezeigt.
- **Lange auf eine Position in der Karte drücken**, um dort einen neuen unsicheren Bereich anzulegen.
- Vor dem Speichern kannst du den Radius einstellen.
- Nach dem Speichern erscheint der Bereich über den Firestore-Echtzeitlistener automatisch auf allen laufenden App-Instanzen.

Ein Dokument sieht beispielsweise so aus:

```text
unsafeAreas/{documentId}
  latitude: 51.2277
  longitude: 6.7735
  radiusMeters: 120
  label: "Unsicherer Bereich"
  active: true
  createdAt: <server timestamp>
```

## 5. Schneller Test

1. App starten und Standortberechtigung erlauben.
2. Auf der Karte lange auf eine Stelle drücken.
3. Radius auswählen und speichern.
4. Firebase Console -> Firestore -> `unsafeAreas` prüfen.
5. App neu öffnen oder auf einem zweiten Emulator starten: der rote Bereich sollte automatisch erscheinen.

## Sicherheit

Die beiliegenden Firestore-Regeln sind bewusst für einen Prototyp gedacht: Lesen und das Anlegen validierter Gefahrenbereiche ist öffentlich möglich. Update und Delete sind gesperrt. Vor einer Veröffentlichung sollte Firebase Authentication und/oder ein Moderationsmechanismus ergänzt werden.
