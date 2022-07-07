# PNeS 3.0 - The Petri Net System

## Wprowadzenie

**Petri Net System** (w skrócie PNeS) to zintegrowane graficzne narzędzie komputerowe do budowania, modyfikowania,
analizowania wielu rodzajów sieci Petriego (w tym rozmytych sieci Petriego).

**Sieć Petriego** – język modelowania dyskretnych systemów rozproszonych. Sieci Petriego zostały zdefiniowane w latach
60 XX w. przez Carla Adama Petriego. Przez swoją zdolność do wyrażania współbieżnych zdarzeń jest blisko związana z
teorią automatów.

Sieć Petriego w najprostszej wersji składa się z "miejsc", "tranzycji" oraz krawędzi skierowanych. Taką siecią można
jedynie opisać układ jako statyczne połączenie możliwych do osiągnięcia stanów. Aby opisać konkretny stan układu,
potrzebne są "żetony", które można przemieszczać pomiędzy miejscami poprzez przejścia, po krawędziach grafu. Tradycyjnie
miejsce oznacza się okręgiem, w którym można umieścić żeton prezentowany przez koło. W jednym miejscu może znajdować się
dowolna nieujemna liczba żetonów. Tranzycje oznacza się prostokątami lub kreskami a krawędzie to strzałki. Krawędzie
mogą mieć wagi większe lub równe 1. Wagi równej 1 nie oznacza się. Waga określa ile dokładnie żetonów przechodzi po
krawędzi.

W najprostszej postaci, żetony w sieci Petriego są nierozróżnialne między sobą. Bardziej złożone postacie sieci Petriego
korzystają z pojęć kolorowania żetonów, czasu aktywacji przejść oraz hierarchii. Poza nimi istnieje wiele innych różnych
rozszerzeń Sieci Petriego, takich jak sieci obiektowe (z żetonami, które mogą być Sieciami Petriego), z ograniczonymi
pojemnościami miejsc, łukami wzbraniającymi i inne.

## Użyte technologie

- **[Java 17](http:/docs.oracle.com/en/java/javase/17/)** - współbieżny, oparty na klasach, obiektowy język
  programowania ogólnego zastosowania. Java jest językiem tworzenia programów źródłowych kompilowanych do kodu
  bajtowego, czyli postaci wykonywanej przez maszynę wirtualną. Język cechuje się silnym typowaniem. Wersja 17 Javy jest
  obecnie najnowszą wersją ze wsparciem długoterminowym (LTS).
- **[Maven 3](https:/maven.apache.org/guides/index.html)** - narzędzie automatyzujące budowę oprogramowania na platformę
  Java. W oparciu o koncepcję modelu obiektowego projektu (POM), Maven może zarządzać budowaniem projektu, raportowaniem
  i dokumentacją z centralnego miejsca. Maven 3 ma na celu zapewnienie poprawę użyteczności, zwiększenie wydajności,
  umożliwienie bezpiecznego osadzania i utorowanie drogi do implementacji wielu bardzo pożądanych funkcji.
- **[JavaFX 17](https:/openjfx.io/javadoc/17/)** - technologia oprogramowania, która w połączeniu ze środowiskiem Java
  pozwala tworzyć i wdrażać aplikacje o nowoczesnym wyglądzie oraz bogactwie treści i elementów multimedialnych.
- **[TiwulFX Dock](https:/github.com/panemu/tiwulfx-dock)** - biblioteka JavaFX zapewniająca ulepszone TabPane'y.
  Dodatkowo dostarcza funkcjonalności takie jak zmianę kolejności, odłączanie i dokowanie kart.
- **[JMetro](https:/github.com/JFXtras/jfxtras-styles)** - biblioteka JavaFX dostarczająca nowoczesny wygląd aplikacji.
  Dodatkowo oferuje łatwe przejście między jasnym a ciemnym motywem aplikacji.
- **[GraphStream](https:/graphstream-project.org/doc/)** - biblioteka Javy do modelowania i analizy dynamicznych grafów.
  Umożliwia generowanie, importowanie, eksportowanie, modyfikowanie, układanie i wizualizowanie sieci Petriego.
- **[Git](https://git-scm.com/doc)** - darmowy i rozproszony system kontroli wersji o otwartym kodzie źródłowym,
  zaprojektowany do obsługi wszystkiego, od małych do bardzo dużych projektów, z szybkością i wydajnością.
- **[GitHub](https://docs.github.com/en)** - hostingowy serwis internetowy przeznaczony do projektów programistycznych
  wykorzystujących system kontroli wersji Git.
- **[IntelliJ IDEA](https://www.jetbrains.com/help/idea/getting-started.html)** - komercyjne zintegrowane środowisko
  programistyczne napisane w języku Java do tworzenia oprogramowania. Zostało stworzone przez firmę JetBrains.

## Uruchomienie

W celu uruchomienia programu należy wykonać wszystkie poniższe kroki. W procesie uruchomienie najlepiej użyć tych samych
programów jakie są przedstawione w instrukcji.

- pobrać pliki znajdujące się w **[repozytorium](https://github.com/ADIOP55550/PNES-v3)**
- pobrać i uruchomić program **[IntelliJ IDEA](https://www.jetbrains.com/idea/download)**
- w oknie aplikacji wybrać **Open**
  ![alt](https://i.imgur.com/XP07reJ.png)
- wybrać folder z kodem źródłowym projektu
- gdy otworzy się okno aplikacji należy chwile odczekać, aby cały projekt poprawnie się wczytał
- w następnym kroku należy wybrać zakładkę **Maven**
  ![alt](https://i.imgur.com/L9IzUog.png)
- następnie odświeżyć Mavena klikając niżej wskazaną ikonę
  ![alt](https://i.imgur.com/3y512S0.png)
- w niektórych sytuacjach IntelliJ zasugeruje zainstalowanie odpowiedniej wersji Javy, należy wtedy to zrobić
- w celu uruchomienia aplikacji należy klinać zielony trójkąt
  ![alt](https://i.imgur.com/BI6SBAY.png)

## Struktura projektu

Kod źródłowy programu został podzielony na pakiety. Łączą one w grupy klasy o podobnym przeznaczeniu lub chcarakterze
działani.

```
- pl.edu.ur.pnes
    - editor
        - actions
        - history
    - events
    - panels
    - petriNet
        - events
        - ghosts
        - netTypes
            - annotations
            - nonClasical.FPN
        - simulator
        - utils
        - visualizer
            - events
                - attribute
                - mouse
                - net
    - ui
        - controls
        - panels
        - utils
    - utils
   ```

- `pl.edu.ur.pnes`- główny pakiet aplikacji. Zawiera w sobie wszystkie pozostałe pakiety aplikacji oraz klasy
  umożliwiające uruchomienie aplikacji
- `editor`- zawiera w sobie pakiety i klasy umożliwijące użytkownikowi edycje zawartości wyświetlanej przez aplikację.
  Umożliwia również zmianę trybu z **edycji** na **symulację** i na odwrót
- `actions`- zawiera w sobie klasy umożliwijące użytkownikowi dodanie **miejsca**, **tranzycji** i **łuku**. Dodatkowo
  implementują możliwość przemieszczania elementów
- `history`- zawiera w sobie klasy i interfejsy umożliwijące użytkownikowi cofnięcie ostatnio wykonanych akcji i
  wprowadzonych zmian.
- `events`- zawiera w sobie klasy i interfejsy umożliwiające obsługę akcji.
- `panels`
- `petriNet`- zawiera w sobie pakiety klasy i interfejsy implementującę logikę, symulacje i akcje sieci Petrigo.
- `events`- zawiera w sobie klasy obsługujące dodawanie, usuwanie i przemieszczanie elementów sieci. Dadatkowo obsługują
  zmianę symulowanego typu sieci Petriego
- `ghosts`
- `netTypes`- zawiera w sobie pakiety, klasy i interfejsy określające zasady działania sieci Petriego.
- `annotations`
- `nonClasical.FPN`- zawiera w sobie klasy określające logikę rozmytych sieci Petriego. Dodatkowo implementuje zasady
  działania T-norm i S-norm w tych sieciach.
- `simulator`- zawiera w sobie klasy i typ wyliczeniowy implementujące obsługę trybu **symulacji**. Kod w tym pakiecie
  sparwdza czy wszystkie reguły pozwalające uruchomić daną tranzycje są spełnione, decyduje który krok wykonać i go
  wykonuje. Dodatkowo w przypadku, gdy nie ma możliwości wykonanie żadnego kroku wyświetla stosowny komunikat.
- `utils`
- `visualizer`- zawiera w sobie pakiety, klasy i typy wyliczeniowe umożliwiające wizualizacje pasywnych i aktywnych
  elementów sieci.
- `events`- zawiera w sobie pakiety, klasy i typy wyliczeniowe umożliwiające obsługę akcji użytkownika związane z
  wizualizacją.
- `attribute`- zawiera w sobie klasy i typy wyliczeniowe umożliwiające sprawdzenie czym jest element z którym użytkownik
  wchodzi w akcje.
- `mouse`- zawiera w sobie klasy umożliwiające wizualizacje akcji użytkownika odbywających się przy pomocy myszki.
- `net`- zawiera w sobie klasy umożliwiające wizualizacje akcji użytkownika, które są związane z graficznymi elementami
  sieci.
- `ui` - zawiera klasy, interfejsy i pliki o rozszerzeniu `fxml` będące graficznym interfejsem użytkownika.
- `controls`- zawiera klasy i pliki o rozszerzeniu `fxml` obsługujące i wyświetlające ikony w graficznym interfejsie
  użytkownika.
- `panels`- zawiera klasy i pliki o rozszerzeniu `fxml` wyświetlające układ głównych paneli aplikacji. Obejmuje to
  wstawienie do głównego okna aplikacji głównego panelu z edycją i symulacją sieci Petriego oraz dwóch pobocznych
  paneli- `project tree` oraz `properties panel`
- `utils`
- `utils`

## Historia

Cały kod źródłowy aplikacji słada się z klas, interfejsów i innych. Poniżej znajduje się omówienie najważnijeszych
fragmentów kodu.

### `pl.edu.ur.pnes/editor/actions/AddArcAction`

```
package pl.edu.ur.pnes.editor.actions;
```

Linijka kodu określająca pakiet do którego należy klasa. W dalszej części dokumentu podobny kod nie będzie ponownie
przedstawiany i omawiany.

```
import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.Net;
```

Linijki dodające konieczne do poprawnego działania aplikacji importy. W dalszej części dokumentu podobny kod nie będzie
ponownie przedstawiany i omawiany.

```

public class AddArcAction extends UndoableAction {
public final Net net;
public final Arc arc;

    public AddArcAction(Net net, Arc arc) {
        this.net = net;
        this.arc = arc;
    }
```

Klasa `AddArcAction` implementuje dodawanie łuków. Dodatkowo dzięki rozszerzeniu klasy `UndoableAction` umożliwia
cofanie i
ponawianie tej czynności. Klasa posiada publiczne pola `net` typu `Net` i `arc` typu `Arc` oraz konstruktor ustawaiający
te pola.

```
    @Override
    public String description() {
        return "Add arc";
    }
```

Nadpisana metoda `description()` zwraca opis wykonaniej akcji- dodania łuku. Metoda ta jest dziedziczona po klasie
nadrzędnej.

```
    @Override
    public String details() {
        return "Add arc from node %s to node %s".formatted(arc.output.getName(), arc.input.getName());
    }
```

Nadpisana metoda `details()` zwraca szczegółowy opis wykonaniej akcji- dodania łuku. Metoda ta jest dziedziczona po
klasie nadrzędnej.

```
    @Override
    public void undo() {
        net.removeElement(arc);
        applied = false;
    }
```

Nadpisana metoda `undo()` usuwa łuk z sieci. Metoda ta jest dziedziczona po klasie nadrzędnej.

```
    @Override
    public void redo() {
        apply();
    }
```

Nadpisana metoda `redo()` ponownie dodaje usunięty przez metodę `undo()` łuk do sieci. Metoda ta jest dziedziczona po
klasie nadrzędnej.

```
    public void apply() {
        net.addElement(arc);
        applied = true;
    }
}
```

Metoda `apply()` dodaje łuk do sieci. Metoda ta jest dziedziczona po klasie nadrzędnej.

### `pl.edu.ur.pnes/editor/actions/AddNodeAction`

```
public class AddNodeAction extends UndoableAction {
    public final Net net;
    public final Point3D position;
    public final Node node;

    public AddNodeAction(Net net, Node node, Point3D position) {
        this.net = net;
        this.node = node;
        this.position = position;
    }
```

Klasa `AddNodeAction` implementuje dodawanie `miejsca` lub `tranzycji`. Dodatkowo dzięki rozszerzeniu
klasy `UndoableAction` umożliwia cofanie i ponawianie tej czynności. Klasa posiada publiczne pola `net` typu `Net`
, `node` typu `Node` i `position`
typu `Point3D` oraz konstruktor ustawaiający
te pola.

```

    @Override
    public String description() {
        return "Add %s".formatted(getNodeTypeString(node));
    }
```

Nadpisana metoda `description()` zwraca opis wykonaniej akcji- dodania miejsca lub tranzycji. Metoda ta jest
dziedziczona po klasie
nadrzędnej.

```
    @Override
    public String details() {
        return "Add %s at (%f, %f)".formatted(getNodeTypeString(node), position.getX(), position.getY());
    }
```

Nadpisana metoda `details()` zwraca szczegółowy opis wykonaniej akcji- dodania miejsca lub tranzycji. Metoda ta jest
dziedziczona po
klasie nadrzędnej.

```
    private String getNodeTypeString(Node node) {
        if (node instanceof Place)      return "place";
        if (node instanceof Transition) return "transition";
        return "node";
    }
```

Metoda `getNodeTypeString()` przyjmuje element sieci i zwraca informacje czym jest ten element -miejscem bądź tranzycją.

```
    @Override
    public void undo() {
        net.removeElement(node);
        applied = false;
    }
```

Nadpisana metoda `undo()` usuwa miejsce lub tranzycji z sieci. Metoda ta jest dziedziczona po klasie nadrzędnej.

```
    @Override
    public void redo() {
        apply();
    }
```

Nadpisana metoda `redo()` ponownie dodaje usunięte przez metodę `undo()` miejsce lub tranzycje do sieci. Metoda ta jest
dziedziczona po
klasie nadrzędnej.

```

    public void apply() {
        net.addElement(node, position);
        applied = true;
    }
}
```

Metoda `apply()` dodaje miejsce lub tranzycje do sieci w określonym przez pozycje `position` miejscu. Metoda
ta jest dziedziczona po klasie nadrzędnej.

### `pl.edu.ur.pnes/editor/actions/MoveNodesAction`

```
public class MoveNodesAction extends UndoableAction {
public final VisualizerFacade visualizer;
public final List<Node> nodes;
public final Point3D offset;

    public MoveNodesAction(VisualizerFacade visualizer, List<Node> nodes, Point3D offset) {
        this.visualizer = visualizer;
        this.nodes = nodes;
        this.offset = offset;
    }
```

Klasa `MoveNodesAction` implementuje przesuwanie elementów sieci. Dodatkowo dzięki rozszerzeniu
klasy `UndoableAction` umożliwia cofanie i ponawianie tej czynności. Klasa posiada publiczne pola `visualizer`
typu `VisualizerFacade`, `nodes` będący listą elementów typu `Node` i `offset`
typu `Point3D` oraz konstruktor ustawaiający te pola.

```
    @Override
    public String description() {
        if (nodes.size() > 1)
            return "Move %d nodes".formatted(nodes.size());
        else
            return "Move node";
    }
```

Nadpisana metoda `description()` zwraca opis wykonaniej akcji- przemieszczenia dla listy elementów `nodes`. Metoda ta
jest
dziedziczona po klasie
nadrzędnej.

```
    @Override
    public String details() {
        if (nodes.size() > 1)
            return "Move %d nodes (".formatted(nodes.size())
                    + nodes.stream().map(Node::getName).collect(Collectors.joining(", "))
                    + ") by (%f, %f)".formatted(offset.getX(), offset.getY());
        else {
            final var node = nodes.get(0);
            return "Move %s %s by (%f, %f)".formatted(getNodeTypeString(node), node.getName(), offset.getX(), offset.getY());
        }
    }
```

Nadpisana metoda `details()` zwraca szczegółowy opis wykonaniej akcji- przemieszczenia dla listy elementów `nodes`.
Metoda ta jest dziedziczona po klasie nadrzędnej.

```

    private String getNodeTypeString(Node node) {
        if (node instanceof Place)      return "place";
        if (node instanceof Transition) return "transition";
        return "node";
    }
```

Metoda `getNodeTypeString()` przyjmuje element sieci i zwraca informacje czym jest ten element -miejscem, tranzycją bądź
nieokreślonym elementem.

```
    @Override
    public void undo() {
        for (var node : nodes) {
            visualizer.setNodePosition(node, visualizer.getNodePosition(node).subtract(offset));
        }
        applied = false;
    }
```

Nadpisana metoda `undo()` cofa przemiesznie elementów sieci z listy `nodes`. Metoda ta jest dziedziczona po klasie
nadrzędnej.

```
    @Override
    public void redo() {
        for (var node : nodes) {
            visualizer.setNodePosition(node, visualizer.getNodePosition(node).add(offset));
        }
        applied = true;
    }
}
```

Nadpisana metoda `redo()` ponownie przemieszcza elementy sieci z listy `nodes`. Metoda ta jest dziedziczona po klasie
nadrzędnej.

### `pl.edu.ur.pnes/editor/history/Undoable`

```
public interface Undoable {

    String description();

    String details();

    void undo();

    void redo();
}
```

Interfejs `Undoable`  dostarcza takich metod jak `description()`, `details()`, `undo()` i `redo()`. Metody te są
następnie nadpisywane w klasach, które używają tego interfejsu.

### `pl.edu.ur.pnes/editor/history/UndoableAction`

```
public abstract class UndoableAction implements Undoable {

    protected boolean applied = false;
```

Abstrakcyjna klasa `UndoableAction` używa interfejsu `Undoable`. Posiada tylko jedno pole `applied` typu `boolean`
domyślnie ustawiane na `false`.

```

    public String details() {
        return description();
    }
```

Metoda `details()` zwraca `description()`.

```
    public Undoable asApplied() {
        applied = true;
        return this;
    }
```

Metoda `asApplied()` ustawia pole akcji `applied` na `true` i zwraca tę akcję.

```
    public Undoable andApply() {
        applied = false;
        redo();
        return this;
    }
}
```

Metoda `andApply()` ustawia pole akcji `applied` na `false`, cofa wykonaną akcję i ją zwraca.

### `pl.edu.ur.pnes/editor/history/UndoableAction`

```
public class UndoHistory {

    private final static Logger logger = LogManager.getLogger();
    ArrayList<Undoable> steps = new ArrayList<>();
    int lastAppliedIndex = -1;
    final Session session;
 ```

Klasa `UndoHistory` jest glówną klasą, która zarządza **cofaniem** i **ponawianiem** **akcji**. Historia wykonanych
akcji jest zapisywana w formie **stosu** w polu `steps`. Jest to `ArrayList` przechowująca elementy `Undoable`.

```

    public Session getSession() {
        return session;
    }

    public UndoHistory(Session session) {
        this.session = session;
    }
```

Metoda `getSession()` oraz `UndoHistory()` kolejno zwraca i ustawia pole `session`.

```
    public int countUndo() {
        return steps.size();
    }
```

Metoda `countUndo()` zwraca ilość możliwych cofnięć akcji.

```
    public int countRedo() {
        return lastAppliedIndex - steps.size() + 1;
    }
```

Metoda `countRedo()` zwraca ilość możliwych ponowień cofniętych akcji.

```
    public Undoable peekUndo() {
        return peekUndo(1);
    }

    public Undoable peekUndo(int n) {
        try {
            return steps.get(lastAppliedIndex - n + 1);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

```

Metody `countRedo()` zwraca ilość możliwych ponowień cofniętych akcji.

```
    public Undoable peekRedo() {
        return peekRedo(1);
    }

    public Undoable peekRedo(int n) {
        try {
            return steps.get(lastAppliedIndex + n);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean undo() {
        final var step = peekUndo();
        if (step == null) return false;
        if (session.mode().get() == Mode.RUN && !step.getClass().isAnnotationPresent(UndoableWhileRunning.class)) {
            logger.debug("couldn't undo #%d %s because is not undoable while running".formatted(lastAppliedIndex + 1, step.description()));
            return false;
        }
        step.undo();
        logger.debug("undid #%d %s".formatted(lastAppliedIndex + 1, step.details()));
        lastAppliedIndex -= 1;
        return true;
    }
```

Metody `peekRedo()` i `undo()` implementują cofanie. Sprawdzają czy cofnięcie wykonanie akcji jest możliwe. Następnie
jeśli to możliwe akcja jest cofana i zwracane `true`, w przecinym wypadku zracane jest `false` i wyświetlany stosowny
komunikat.

```
    public void push(Undoable step) {
        for (int i = steps.size() - 1; lastAppliedIndex < i; i--) {
            steps.remove(i);
        }
        steps.add(step);
        logger.debug("pushed #%d %s".formatted(steps.size(), step.details()));
        lastAppliedIndex += 1;
    }
```

Metoda `push()` umieszcza przekazaną akcje na szczycie stosu historii `steps`.

```
    public boolean redo() {
        final var step = peekRedo();
        if (step == null) return false;
        if (session.mode().get() == Mode.RUN && !step.getClass().isAnnotationPresent(UndoableWhileRunning.class)) {
            logger.debug("couldn't redo #%d %s because is not undoable while running".formatted(lastAppliedIndex + 2, step.description()));
            return false;
        }
        step.redo();
        lastAppliedIndex += 1;
        logger.debug("redid #%d %s".formatted(lastAppliedIndex + 1, step.details()));
        return true;
    }
```

Metoda `redo()` implementuje ponowienie cofaniętej akcji. Sprawdzają czy jest to możliwe. Następnie jeśli to możliwe
akcja jest ponawiana i zwracane `true`, w przecinym wypadku zracane jest `false` i wyświetlany stosowny komunikat.

## Dodawanie funkcjonalności

### Dodanie przycisku do panelu wizualizacji sieci

`src/main/java/pl/edu/ur/pnes/ui/panels/CenterPanelController.java` to klasa do zarządzania głównym panelem. W celu
dodania do niego przycisku należy najpierw zdefiniować nowy przycisk:

```
private final Button testButton = new Button("test");
```

Następnie zdefiniowany przcisk należy dodać do wybranej kontenera. Do wyboru są 4:

```
primaryToolbarLeft
primaryToolbarRight
secondaryToolbarLeft
secondaryToolbarRight
```

![alt](https://i.imgur.com/cxM1sNk.png)

W taki sposób można dodać przycisk do wybranego kontenera:

```
nazwaKontenera.getChildren().add(testButton);
```

lub zmodyfikować kod, który dodaje przyciski do kontenera:

```
primaryToolbarLeft.getChildren().addAll(layoutButton, toggleModeButton, netGroupChoiceBox, netTypesChoiceBox, testButton);
```

W celu dodania funkcjonalności do stworzonego przycisku należy dodać metodę:

```
testButton.addEventHandler(actionEvent -> {
//TODO: Twoja implementacja
});
```

**Wynik:**

![alt](https://i.imgur.com/l4XIbIc.png)

### Dodanie elementu sieci

W celu dodania do sieci **miesjca** należy w `src/main/java/pl/edu/ur/pnes/petriNet/visualizer/Visualizer.java` w
metodzie `visualizeNet()` najpierw zadeklarować nowe miejsce:

```
final var place = new Place(net);
```

następnie dzięki dostarczonym metodom można zmienić właściwośći **miejsca**. Dostępne metody:

`setCapacity()`- ustawia jaką maksymanlą liczbę **tokenów** może przetrzymywać miejsce,

`setPosition()`- ustawienie pozycji miejsca,

`setTokens()`- ustawienie liczby tokenów przechwowyanych w miejscu. Domyślnie jest to 1,

`setName()`- ustawia nazwę,

Następnie należy dodać element do sieci:

```
net.addElement(place);
```

W celu dodania **tranzycji** należy najpierw ją zadeklarowć:

```
final var transition = new Transition(net);
```

Nstępnie można zmodyfikować domyślną tranzycję używając dostarczonych metod, a następnie dodać element do sieci:

```
net.addElement(transition);
```

W celu dodania **łuku** należy najpierw go zadeklarowć:

```
final var arc = new Arc(net, place, transition);
```

Ważne, żeby podać najpierw element sieci, z którego ma wychodzić łuk a następnie wyjście. Nastepnie należy dodac łuk do
sieci,

```
net.addElement(arc);
```

**Wynik:**

![alt](https://i.imgur.com/QFtqnKG.png)

Uwaga! Ten sposób dodawania elemnentów sieci to dodawanie go aby wyświetlił się na starcie programu. Gdy chcemy aby to
użytkownik mógł dodać element nalezy oprogramać przycisk który to umożliwi. A apliakcji odbywa się to w
klasie `src/main/java/pl/edu/ur/pnes/ui/panels/CenterPanelController.java`.
Przykład metody dodającej **miejsce**:

```
addPlaceButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                this.mouseEventEventHandler = mouseEvent -> {
                    if (!mouseEvent.getButton().equals(MouseButton.PRIMARY))
                        return;
                    final var place = new Place(net);
                    getSession().undoHistory.push(
                            new AddNodeAction(net, place, visualizerFacade.mousePositionToGraphPosition(mouseEvent)).andApply()
                    );
                };
                graphPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            } else {
                graphPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            }
        });
```

W metodziej oprócz sprawdzenia czy wszytskie warunki koniecznie do wstawienia elementu są spełnione, dodana jest opcja
cofania wykonanej akcji.

```
getSession().undoHistory.push(
new AddNodeAction(net, place, visualizerFacade.mousePositionToGraphPosition(mouseEvent)).andApply()
);
```

Do historii dodanna zostaje wykonana akcja - dodanie miejsca. Umożliwia to cofnięcie i ponowienie tej akcji.

### Dodanie karty

Obecnie w aplikacji są domyślnie 3 karty, po jedej w każdym panelu. W celu dodania kolejnego karty konieczne jest
wykonanie kolejnych kroków:

W pakiecie `panels` nalezy dodać plik o rozszerzeniu `fxml` np.:

```
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.*?>

<AnchorPane fx:id="root" xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            prefHeight="200.0" prefWidth="600.0">

</AnchorPane>
```

Następnie w tym samym pakiecie dodać kontroler np.:

```
package pl.edu.ur.pnes.ui.panels;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import pl.edu.ur.pnes.ui.utils.FXMLUtils;
import pl.edu.ur.pnes.ui.utils.Rooted;

import java.io.IOException;

public class TestPanelController implements Rooted {
    @FXML
    AnchorPane root;
    @Override
    public javafx.scene.Node getRoot() {
        return root;
    }

    static public TestPanelController prepare() {
        final var loader = FXMLUtils.getLoader(ProjectTreePanelController.class);
        final var controller = new TestPanelController();
        try {
            loader.setController(controller);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }
}

```

Ostatnim krokiem jest wywołanie kontrolera w `src/main/java/pl/edu/ur/pnes/MainController.java`. Najpierw należy dodać
pole:

```
public TestPanelController testPanelController;
```

Następnie w metodzie `initialize()` ustawić kontroler i przypsiać zakładke do wybranego panelu:

```
testPanelController=  TestPanelController.prepare();
leftTabPane.addTab("test", testPanelController.getRoot());
```

**Wynik:**

![alt](https://i.imgur.com/HhFg8YI.png)

### Dodanie panelu bocznego

Obecnie w aplikacji są 3 panele. Środkowy odpowieda za wizualizacje sieci, lewy za wyświetlanie układu plików a prawy za
właściwości wybranego elementu.

![alt](https://i.imgur.com/YzsZREr.png)

W celu dodania kolejnego panelu konieczne jest wykonanie kolejnych kroków. W
klasie `src/main/java/pl/edu/ur/pnes/MainController.java` dodać pole:

```
@FXML
public DetachableTabPane testPane;
```

Następnie w metodzie `initialize()` zmienić linię:

```
for (DetachableTabPane detachableTabPane : Arrays.asList(leftTabPane, centerTabPane, rightTabPane)) {
```
na taką uwzględniającą nowy panel:
```
for (DetachableTabPane detachableTabPane : Arrays.asList(leftTabPane, testPane, centerTabPane, rightTabPane)) {
```
Następnie, w pliku `src/main/resources/pl/edu/ur/pnes/main-view.fxml` należy dodać nowy panel.

```
    <center>
        <SplitPane dividerPositions="0.18699186991869918, 0.7626016260162601" prefHeight="372.0" prefWidth="790.0" BorderPane.alignment="CENTER">
            <items>
                <SplitPane>
                    <DetachableTabPane fx:id="leftTabPane" />
                    <DetachableTabPane fx:id="testPane" /> //DODANA LINIJKA Z NOWYM PANELEM
                </SplitPane>
                <VBox>
                    <children>
                        <SplitPane VBox.vgrow="ALWAYS">
                            <DetachableTabPane fx:id="centerTabPane" tabClosingPolicy="ALL_TABS" />
                        </SplitPane>
                    </children>
                </VBox>
                <SplitPane>
                    <DetachableTabPane fx:id="rightTabPane" />
                </SplitPane>
            </items>
        </SplitPane>
    </center>
```
Wynik:
![alt](https://i.imgur.com/M1vSzmL.png)