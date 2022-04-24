package pl.edu.ur.pnes.ui.controls;


import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pl.edu.ur.pnes.MainApp;

public class Icon extends Label {

    public Icon() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ui/controls/Icon.fxml"));
        fxmlLoader.setRoot(this);
//        this.setFont(new Font("pnesfont", 20));
    }

}