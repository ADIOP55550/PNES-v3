module pl.edu.ur.pnes {
    requires javafx.controls;
    requires javafx.fxml;
    requires tiwulfx.dock;
    requires org.jfxtras.styles.jmetro;


    opens pl.edu.ur.pnes to javafx.fxml;
    exports pl.edu.ur.pnes;
    exports pl.edu.ur.pnes.panels;
}