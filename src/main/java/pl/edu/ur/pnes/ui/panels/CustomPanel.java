package pl.edu.ur.pnes.ui.panels;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Locale;

public abstract class CustomPanel {
    protected String filename = getClass().getSimpleName().replaceAll("([A-Z])", "-$1").toLowerCase(Locale.ROOT).substring(1) + ".fxml";

    public Parent loadPanel() throws IOException {
        System.out.println(filename);
        var loader = new FXMLLoader(getClass().getResource(filename));
        return loader.load();
    }
}
