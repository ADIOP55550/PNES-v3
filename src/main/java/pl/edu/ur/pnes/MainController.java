package pl.edu.ur.pnes;

import com.panemu.tiwulfx.control.dock.DetachableTabPane;
import com.panemu.tiwulfx.control.dock.TabStageFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.ui.controls.Icon;
import pl.edu.ur.pnes.ui.panels.CenterPanelController;
import pl.edu.ur.pnes.ui.panels.ProjectTreePanelController;
import pl.edu.ur.pnes.ui.panels.PropertiesPanelController;
import pl.edu.ur.pnes.utils.SoundAlertUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Function;

import static pl.edu.ur.pnes.MainApp.mainJMetro;

public class MainController implements Initializable {
    private final static Logger logger = LogManager.getLogger();

    Collection<Session> sessions = new ArrayList<>();

    /**
     * Opens new session, opening initial session-scoped panels.
     *
     * @param session Session to open.
     * @return True if session was already open, false otherwise.
     */
    public boolean open(final Session session) throws IOException {
        if (sessions.contains(session)) {
            // TODO: focus most important panel related to the session
            return false;
        }
        sessions.add(session);

        final var panel = CenterPanelController.prepare(session);

        setFocusedSession(session);

        final var tab = centerTabPane.addTab(session.getName(), panel.getRoot());
        tab.textProperty().bind(session.nameProperty());
        tab.setUserData(session);
        session.setUiTab(tab);


        tab.setOnCloseRequest(event -> {
            return;
            // TDOO
            /*
            if(!session.isSaved){
                alertresult = displayAlert()
                if(alertresult=CANCEL)
                    event.consume()
            }
             */
        });

        tab.setOnClosed(event -> session.close());

        return true;
    }


    private final ObjectProperty<Session> focusedSession = new SimpleObjectProperty<>(null) {
        @Override
        public void set(Session newValue) {
            if (newValue == null) {
                super.set(null);
                return;
            }
            // TODO: here you can add code that runs when focused session changes
            centerTabPane.getSelectionModel().select(newValue.getUiTab());
            super.set(newValue);
        }
    };

    public Session getFocusedSession() {
        return focusedSession.get();
    }

    public ObjectProperty<Session> focusedSessionProperty() {
        return focusedSession;
    }

    {
        focusedSession.addListener((observable, oldValue, newValue) -> {
            logger.debug("Now focused: " + newValue);
        });
    }

    public void setFocusedSession(Session focusedSession) {
        this.focusedSession.set(focusedSession);
    }

    public ProjectTreePanelController projectTreePanelController;
    public PropertiesPanelController propertiesPanelController;

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

    @FXML
    public MenuItem menuUndo;
    @FXML
    public MenuItem menuRedo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainToolbar.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        centerTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            final Object data = newValue.getUserData();
            if (data instanceof Session session)
                focusedSession.set(session);
        });

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

        // initialize common panels
        projectTreePanelController = ProjectTreePanelController.prepare();
//        propertiesPanelController = PropertiesPanelController.prepare();
        propertiesPanelController = PropertiesPanelController.prepare(this);

        leftTabPane.addTab("Project tree", projectTreePanelController.getRoot());
        rightTabPane.addTab("Properties", propertiesPanelController.getRoot());

        // button for changing theme
        Button changeThemeButton = new Button("Surprise! 🎁");
        changeThemeButton.setOnAction(actionEvent -> {
            // invert style
            mainJMetro.setStyle(mainJMetro.getStyle() == Style.DARK ? Style.LIGHT : Style.DARK);
        });

        // new file button
        Button newFileButton = new Button();
        newFileButton.setGraphic(new Icon("\ue812"));
        newFileButton.setOnAction(event -> {
            try {
                open(new Session());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        mainToolbarLeft.getChildren().addAll(changeThemeButton, newFileButton);
    }


    /**
     * Updates edit menu before showing.
     */
    @FXML
    public void editMenuShowingAction(Event event) {
        logger.debug("UndoHistory (on edit menu showing): " + getFocusedSession().undoHistory.dumpToString(10));

        final var undoable = getFocusedSession().undoHistory.peekUndo();
        if (undoable == null) {
            menuUndo.setDisable(true);
            menuUndo.setText("Undo (Nothing to undo)");
        } else {
            menuUndo.setDisable(false);
            menuUndo.setText("Undo (%s)".formatted(undoable.description()));
        }

        final var redoable = getFocusedSession().undoHistory.peekRedo();
        if (redoable == null) {
            menuRedo.setDisable(true);
            menuRedo.setText("Redo (Nothing to redo)");
        } else {
            menuRedo.setDisable(false);
            menuRedo.setText("Redo (%s)".formatted(redoable.description()));
        }
    }

    /**
     * Updates edit menu after hidden.
     */
    @FXML
    public void editMenuHiddenAction(Event event) {
        // Make sure undo/redo are always enabled, so keyboard accelerator works
        menuUndo.setDisable(false);
        menuRedo.setDisable(false);
    }

    @FXML
    public void undoAction(ActionEvent event) {
        if (!getFocusedSession().undoHistory.undo()) {
            SoundAlertUtils.playWarning();
        }
    }

    @FXML
    public void redoAction(ActionEvent event) {
        if (!getFocusedSession().undoHistory.redo()) {
            SoundAlertUtils.playWarning();
        }
    }
}