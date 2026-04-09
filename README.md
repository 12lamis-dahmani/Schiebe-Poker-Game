# Schiebe-Poker

**Schiebe-Poker** ist ein Kartenspiel für 2 Spieler, entwickelt in **Kotlin** mit dem **BoardGameWork**-Framework. Dieses Projekt wurde im Rahmen des **Software-Praktikums** an der **TU Dortmund** realisiert.

---

## Spielkonzept

Das Ziel von Schiebe-Poker ist es, durch geschickles Verschieben und Tauschen von Karten die beste Poker-Hand zu erhalten. Das Spiel wird mit einem Standard-Kartendeck gespielt.

### Spielkomponenten
- **Standard-Blatt:** 52 Karten ({Kreuz, Pik, Herz, Karo} × {2–10, Bube, Dame, König, Ass})
- **Handkarten:** 2 verdeckte + 3 offene Karten pro Spieler
- **Mittenkarten:** 3 offene Karten in der Spielfeldmitte
- **Nachziehstapel:** Verbleibende Karten
- **Ablagestapel:** Für jede Spielrunde

---

## Spielablauf

1. Die **Anzahl der Spielrunden** wird vor Spielstart vereinbart (2–7 Runden).
2. Ein **zufälliger Startspieler** wird bestimmt.
3. Spieler führen abwechselnd **genau zwei Aktionen** durch:
   - **Schieben:** Karten in der Mitte nach links oder rechts verschieben
   - **Tauschen:** Eine oder alle drei offenen Karten mit der Mitte tauschen

### Aktion 1 – Schieben

Der Spieler wählt die Richtung:

- **Nach links:** Die linke Karte wird auf den Ablagestapel gelegt, die übrigen Karten rücken nach links, eine neue Karte vom Nachziehstapel füllt die rechte Position.
- **Nach rechts:** Analoge Vorgehensweise in umgekehrter Richtung.

### Aktion 2 – Tauschen

Der Spieler kann:

- **Einfach-Tausch:** Eine eigene offene Karte mit einer Mittenkarte tauschen
- **Dreifach-Tausch:** Alle drei offenen Karten mit den Mittenkarten tauschen (Position bleibt gleich)
- **Verzicht:** Keinen Tausch durchführen

---

## Allgemeine Spielregeln

- **Handkarten dürfen nicht sortiert werden** – die Reihenfolge ergibt sich ausschließlich aus dem Spiel.
- **Nachziehstapel leer?** Der Ablagestapel wird gemischt und als neuer Nachziehstapel verwendet.
- **Handwert:** Jeder Spieler hat eine 5-Karten-Hand (2 verdeckt + 3 offen).

---

## Spielende und Punktewertung

Das Spiel endet nach der vereinbarten Anzahl an Runden.

Die Handwerte folgen den **Standard-Poker-Regeln**:
- Royal Flush
- Straight Flush
- Four of a Kind
- Full House
- Flush
- Straight
- Three of a Kind
- Two Pair
- One Pair
- High Card

**Unentschieden:** Haben zwei Spieler die gleiche Handstärke, wird dies als unentschieden bewertet – unabhängig von spezifischen Kartenwerten.

Nach jeder Runde wird die Rangfolge der Spieler angezeigt. Am Ende aller Runden wird der Gesamtsieger bestimmt.

---

## Implementationsfeatures

- **Hotseat-Modus:** Spieler wechseln sich am gleichen Bildschirm ab
- **Verdeckte Karten:** Nur für den jeweiligen Spieler sichtbar; „nächster Spieler"-Screen deckt alle Karten ab
- **Spielprotokoll:** Detailliertes Aktionslog während des Spiels mit allen öffentlichen Aktionen
- **Rangfolge:** Anzeige der Spielerplatzierung nach jeder Runde und am Ende
- **Konfigurierbare Spieler:** Namen werden vor Spielstart eingegeben
- **Variable Rundenzahl:** 2–7 Runden wählbar

---

## Anforderungen an das Programm

Das Programm steuert den kompletten Spielablauf und gewährleistet die Einhaltung aller Spielregeln:

✓ Spielername und Rundenzahl konfigurierbar beim Start  
✓ Hotseat-Modus mit „nächster Spieler"-Screen  
✓ Verdeckte Handkarten nur für jeweiligen Spieler sichtbar  
✓ Nachvollziehbares Spielprotokoll aller Aktionen  
✓ Korrekte Poker-Hand-Bewertung nach Standard-Regeln  
✓ Automatische Nachziehstapel-Verwaltung  
✓ Rangfolge nach jeder Runde und am Ende  

---

## Technische Spezifikation

- **Sprache:** Kotlin
- **Framework:** BoardGameWork
- **Spieleranzahl:** 2–4 Spieler
- **Rundenzahl:** 2–7 Runden (konfigurierbar)
- **Kartendeck:** Standard 52-Karten Deck

---

## Autoren

Projekt im Rahmen des Software-Praktikums an der TU Dortmund (WiSe 2025/2026)
