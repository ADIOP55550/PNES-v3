package pl.edu.ur.pnes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import pl.edu.ur.pnes.panels.CenterPanel;
import pl.edu.ur.pnes.panels.ProjectTreePanel;
import pl.edu.ur.pnes.panels.PropertiesPanel;

import java.io.IOException;

public class MainApp extends Application {
    public static MainController mainController;
    public static JMetro mainJMetro = new JMetro(Style.DARK); // Default app style
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320 * 3, 240 * 3);
        stage.setTitle("PNES");
        stage.setMaximized(true);
        mainJMetro.setScene(scene);
        stage.setScene(scene);
        stage.show();
        mainStage = stage;

        MainController controller = fxmlLoader.getController();
        mainController = controller;

//        controller.leftTabPane.setOnClosedPassSibling((sibling) -> controller.leftTabPane = sibling);

        PanelManager.addCenterPanel(new CenterPanel());
        PanelManager.addRightPanel(new PropertiesPanel());
        PanelManager.addLeftPanel(new ProjectTreePanel());

        // example tab adding
//        DetachableTab tab1 = new DetachableTab("Test Innej", new Button("Hello"));
//        tab1.setClosable(false);
//        controller.leftTabPane.getTabs().add(tab1);
    }


    public static void main(String[] args) {
        launch();
    }
}