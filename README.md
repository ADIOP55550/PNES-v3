# PNES - The Petri Net System

## Cel

🇺🇸 EN

The Petri Net System (PNeS for short) is an integrated graphical computer tool for building, modifying, analyzing many
types of Petri nets (including fuzzy Petri nets), as well as controlling a mobile robot. The aim of the lecture is to
present the main functionalities of PNeS, with particular emphasis on the functionality related to the possibility of
controlling mobile robots. The current version of PNeS allows us to control the Lego Mindstorms mobile robot performing
various tasks, including avoiding obstacles, reaching the target, following an obstacle, finding an exit from the
labyrinth and influencing the environment by selecting the appropriate manipulators. In fact, the list of tasks for the
robot can be more extensive, it depends mainly on the hardware configuration of the available robot in the control mode.

🇵🇱 PL

_TODO_

## Materiały

- [Wikipedia o Sieciach Petriego PL](https://pl.wikipedia.org/wiki/Sie%C4%87_Petriego) [[EN](https://en.wikipedia.org/wiki/Petri_net)]
- [pnml.org - gramatyka .PNML](https://www.pnml.org/index.php)
- [Model 3D CAD Mindstorms EV3](https://grabcad.com/library/lego-mindstorms-ev3-1)
- [Model 3D CAD Mindstorms EV3 2](https://grabcad.com/library/lego-mindstorms-ev3-robot-tomy-1)
- [Porównanie i rozważania dot. systemów dokowania okien w JavaFX](https://github.com/DaveJarvis/PitchDockFX#requirements)
- [Różnica między 'package' i 'module' w javie](https://docs.oracle.com/javase/specs/jls/se9/html/jls-7.html)
- [O grafach i algorytmach ich układu](https://www.baeldung.com/cs/graph-auto-layout-algorithm)
- [Tworzenie horyzontalnego układu grafu](https://crinkles.io/writing/auto-graph-layout-algorithm)
- [Graphdrawing.org - a collection of resources mostly related to the annual International Symposium on Graph Drawing and Network Visualization](http://graphdrawing.org/index.html)
- [PDF o rysowaniu grafów (107 stron)](https://cs.brown.edu/people/rtamassi/papers/gd-tutorial/gd-constraints.pdf)


## Instalacja i uruchomienie

_TODO_

## Konfiguracja środowiska programistycznego

### Dodawanie JMetro

Aby dodać bibliotekę **JMetro** należy pobrać .jar, gdyż repozytorium Maven nie instaluje
poprawnie ([zobacz ten post](https://stackoverflow.com/a/68235655)). Żeby to zrobić, pobierz plik `jmetro-11.6.15.jar`
z [GitHuba](https://github.com/JFXtras/jfxtras-styles/releases) i dodaj jako bibliotekę do projektu, lub dodaj do
własnego repozytorium maven - w sposób opisany we [wspomnianym wcześniej poście](https://stackoverflow.com/a/68235655).

### Uruchamianie

**Klasą główną jest klasa [Main App.java](./src/main/java/pl/edu/ur/pnes/MainApp.java)**

_TODO_

## Wymagania

### Java

- Java 17 (w projekcie załączona [Liberica](https://bell-sw.com/pages/downloads/))
- Maven 3
- JavaFX 17

### Biblioteki

- TiwulFX Dock
    - [GitHub](https://github.com/panemu/tiwulfx-dock)
    - [maven](https://mvnrepository.com/artifact/com.panemu/tiwulfx-dock)
    - [MIT License](https://github.com/panemu/tiwulfx-dock/blob/main/LICENSE)
- jfxtras-styles - JMetro
    - [GitHub](https://github.com/JFXtras/jfxtras-styles/)
    - [przykład użycia](https://www.pixelduke.com/java-javafx-theme-jmetro/)
    - [maven](https://mvnrepository.com/artifact/org.jfxtras/jmetro/11.6.15) (nie działa instalacja,
      patrz [Dodawanie JMetro](#dodawanie-jmetro))
    - [New BSD License](https://opensource.org/licenses/BSD-3-Clause)