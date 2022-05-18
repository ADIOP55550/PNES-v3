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
    requires org.jetbrains.annotations;


    opens pl.edu.ur.pnes to javafx.fxml;
    opens pl.edu.ur.pnes.ui to javafx.fxml;
    opens pl.edu.ur.pnes.ui.panels to javafx.fxml;
    exports pl.edu.ur.pnes;
    exports pl.edu.ur.pnes.editor;
    exports pl.edu.ur.pnes.editor.history;
    exports pl.edu.ur.pnes.ui.panels;
    exports pl.edu.ur.pnes.ui;
    exports pl.edu.ur.pnes.ui.controls;
    exports pl.edu.ur.pnes.petriNet;
    exports pl.edu.ur.pnes.petriNet.simulator;
    exports pl.edu.ur.pnes.petriNet.events;
    exports pl.edu.ur.pnes.ui.utils;
    opens pl.edu.ur.pnes.ui.utils to javafx.fxml;

//    exports com.sun.javafx.logging;
}