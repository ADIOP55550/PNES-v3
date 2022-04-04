package pl.edu.ur.pnes;

import com.panemu.tiwulfx.control.dock.DetachableTabPane;
import com.panemu.tiwulfx.control.dock.TabStageFactory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.util.Arrays;
import java.util.function.Function;

import static pl.edu.ur.pnes.MainApp.mainJMetro;

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
}