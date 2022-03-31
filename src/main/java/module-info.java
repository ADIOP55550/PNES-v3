module pl.edu.ur.pnes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;
    requires tiwulfx.dock;
    requires org.jfxtras.styles.jmetro;
    requires gs.core;
    requires gs.ui.javafx;


    opens pl.edu.ur.pnes to javafx.fxml;
    exports pl.edu.ur.pnes;
    exports pl.edu.ur.pnes.panels;

//    exports com.sun.javafx.logging;
}