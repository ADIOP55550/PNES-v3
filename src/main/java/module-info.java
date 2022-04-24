module pl.edu.ur.pnes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;
    requires tiwulfx.dock;
    requires org.jfxtras.styles.jmetro;
    requires gs.ui.javafx;
//    requires gs.algo;
    requires gs.core;
    requires org.apache.logging.log4j;


    opens pl.edu.ur.pnes to javafx.fxml;
    exports pl.edu.ur.pnes;
    exports pl.edu.ur.pnes.ui.panels;
    exports pl.edu.ur.pnes.ui;
    exports pl.edu.ur.pnes.ui.controls;
    opens pl.edu.ur.pnes.ui to javafx.fxml;
    exports pl.edu.ur.pnes.petriNet;

//    exports com.sun.javafx.logging;
}