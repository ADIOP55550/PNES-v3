package pl.edu.ur.pnes.ui.controls;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import pl.edu.ur.pnes.MainApp;

public class Icon extends Label {

    public Icon() {
        this("");
    }

    public Icon(String iconText) {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("ui/controls/Icon.fxml"));
        fxmlLoader.setRoot(this);
//        this.setFont(new Font("pnesfont", 20));
        this.setText(iconText);
    }

}