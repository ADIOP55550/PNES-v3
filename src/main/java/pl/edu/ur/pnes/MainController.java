package pl.edu.ur.pnes;

import com.panemu.tiwulfx.control.dock.DetachableTabPane;
import com.panemu.tiwulfx.control.dock.TabStageFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.editor.history.UndoHistory;
import pl.edu.ur.pnes.editor.history.Undoable;

import java.util.Arrays;
import java.util.function.Function;

import static pl.edu.ur.pnes.MainApp.*;

public class MainController {
    @FXML
    public DetachableTabPane leftTabPane;
    @FXML
    public DetachableTabPane centerTabPane;
    @FXML
    public DetachableTabPane rightTabPane;

    @FXML
    public HBox mainToolbarRight;
    @FXML
    public HBox mainToolbarLeft;
    @FXML
    public HBox mainToolbar;

    @FXML public MenuItem menuUndo;
    @FXML public MenuItem menuRedo;

    public void initialize() {
//        add dark bg to the toolbar
        mainToolbar.getStyleClass().add(JMetroStyleClass.BACKGROUND);


        final Function<Boolean, TabStageFactory> detachableStageFactoryFactory = (preventClosing) -> (detachableTabPane, tab) -> {

            var stage = new DetachableTabPane.TabStage(detachableTabPane, tab);
            if (preventClosing)
                stage.setOnCloseRequest(Event::consume);

            var s = stage.getScene();

            // this allocation prevents garbage collecting jmetro instance
            final JMetro[] jMetro1 = {new JMetro(mainJMetro.getStyle())};
            jMetro1[0].setScene(s);
            stage.setOnHidden(windowEvent -> {
                jMetro1[0] = null;
            });
            s.getRoot().getStyleClass().add(JMetroStyleClass.BACKGROUND);
            // keep new stage's scene in sync
            jMetro1[0].styleProperty().bind(mainJMetro.styleProperty());

            return stage;
        };

        final Callback<DetachableTabPane, Scene> detachableSceneFactory = detachableTabPane -> {
            var s = new Scene(detachableTabPane, 400, 400);
            // recurring factory
            detachableTabPane.setStageFactory(detachableStageFactoryFactory.apply(false));
            return s;
        };


        // set up all detachable tab panes
        for (DetachableTabPane detachableTabPane : Arrays.asList(leftTabPane, centerTabPane, rightTabPane)) {
            detachableTabPane.setMinWidth(30);
            detachableTabPane.setMinHeight(30);
            detachableTabPane.setSceneFactory(detachableSceneFactory);
            detachableTabPane.setStageFactory(detachableStageFactoryFactory.apply(false));
        }


        // button for changing theme
        Button button = (Button) mainToolbarLeft.getChildren().get(mainToolbarLeft.getChildren().size() - 1);
        button.setText("Surprise! 🎁");
        button.setOnAction(actionEvent -> {
            // invert style
            mainJMetro.setStyle(mainJMetro.getStyle() == Style.DARK ? Style.LIGHT : Style.DARK);
        });
    }


    @FXML
    public void editMenuShowingAction(ActionEvent event) {
        final var undoable = getSession().undoHistory.peekUndo();
        if (undoable == null) {
            menuUndo.setDisable(true);
            menuUndo.setText("Undo (Nothing to undo)");
        }
        else {
            menuUndo.setDisable(false);
            menuUndo.setText("Undo (%s)".formatted(undoable.description()));
        }

        final var redoable = getSession().undoHistory.peekRedo();
        if (redoable == null) {
            menuRedo.setDisable(true);
            menuRedo.setText("Redo (Nothing to redo)");
        }
        else {
            menuRedo.setDisable(false);
            menuRedo.setText("Redo (%s)".formatted(redoable.description()));
        }
    }

    @FXML
    public void undoAction(ActionEvent event) {
        getSession().undoHistory.undo();
    }
    @FXML
    public void redoAction(ActionEvent event) {
        getSession().undoHistory.redo();
    }
}