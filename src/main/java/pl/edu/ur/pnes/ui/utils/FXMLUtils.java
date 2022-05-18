package pl.edu.ur.pnes.ui.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Locale;

public class FXMLUtils {
    /**
     * Retrieves related FXML loader for given class.
     * @param viewClass Class to find FXML loader for.
     * @return FXML loader for the class.
     */
    static public FXMLLoader getLoader(Class<?> viewClass) {
        String filename = viewClass.getSimpleName();
        if (filename.endsWith("Controller")) {
            filename = filename.substring(0, filename.length() - 10);
        }
        filename = filename.replaceAll("([A-Z])", "-$1").toLowerCase(Locale.ROOT).substring(1);
        filename += ".fxml";
        return new FXMLLoader(viewClass.getResource(filename));
    }
}
