package pl.edu.ur.pnes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.ui.PanelManager;
import pl.edu.ur.pnes.ui.panels.CenterPanel;
import pl.edu.ur.pnes.ui.panels.ProjectTreePanel;
import pl.edu.ur.pnes.ui.panels.PropertiesPanel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {
    public static MainController mainController;
    public static JMetro mainJMetro = new JMetro(Style.DARK); // Default app style
    public static Stage mainStage;
    public static CenterPanel centerPanel;

    public static Session getSession() {
        // TODO: get session (associated with focused tab) (assuming multiple sessions (files, nets) could be open at once at some point)
        return centerPanel.session;
    }


    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("log4j2.configurationFile", Objects.requireNonNull(MainApp.class.getResource("/log4j2.properties")).getPath());


        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320 * 3, 240 * 3);

        String cssy = Objects.requireNonNull(getClass().getResource("/css/font.css")).toExternalForm();
        scene.getStylesheets().add(cssy);


        stage.setTitle("PNES");
        stage.setMaximized(true);
        mainJMetro.setScene(scene);
        stage.setScene(scene);
        stage.show();
        mainStage = stage;

        mainController = fxmlLoader.getController();

        MainApp.centerPanel = new CenterPanel();


        PanelManager.addCenterPanel(centerPanel);
        PanelManager.addRightPanel(new PropertiesPanel());
        PanelManager.addLeftPanel(new ProjectTreePanel());
    }


    public static void main(String[] args) {
        launch();
    }
}
