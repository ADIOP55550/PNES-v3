package pl.edu.ur.pnes.parser.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ParserUtils {
    public static Optional<ButtonType> parserAlert(Alert.AlertType type, String message, String header, ButtonType ... buttons){
        Alert alert = new Alert(type, message, buttons);
        alert.setHeaderText(header);
        return alert.showAndWait(); //return optionally clicked button
    }
}
