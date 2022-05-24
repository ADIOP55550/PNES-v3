package pl.edu.ur.pnes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.petriNet.*;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class MainApp extends Application {
    public static Stage mainStage;
    public static MainController mainController;
    public static JMetro mainJMetro = new JMetro(Style.DARK); // Default app style

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        System.setProperty("log4j2.configurationFile", Objects.requireNonNull(MainApp.class.getResource("/log4j2.properties")).getPath());

        final FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
        final Scene scene = new Scene(fxmlLoader.load(), 320 * 3, 240 * 3);
        mainController = fxmlLoader.getController();

        final String styles = Objects.requireNonNull(getClass().getResource("/css/font.css")).toExternalForm();
        scene.getStylesheets().add(styles);

        stage.setTitle("PNES");
        stage.setMaximized(true);
        mainJMetro.setScene(scene);
        stage.setScene(scene);
        stage.show();

        // Initial for testing
        final var net = new PetriNet();
        net.setNetType(NetType.PN);
        {
            final Place place1 = new Place(net);
            final Place place2 = new Place(net);
            final Place place3 = new Place(net);
            final Place place4 = new Place(net);
//            place1.setTokensAs(Double.class, 2d);
            place1.setTokensAs(Integer.class, 2);
            final Transition transition1 = new Transition(net);
            final Transition transition2 = new Transition(net);
            final Transition transition3 = new Transition(net);
//            place3.setTokensAs(Double.class, 1d);
            place3.setTokensAs(Integer.class, 1);
            final Arc arc1 = new Arc(net, place1, transition1);
            final Arc arc2 = new Arc(net, transition1, place2);
            final Arc arc3 = new Arc(net, place2, transition2);
            final Arc arc4 = new Arc(net, transition2, place1);
            arc4.setWeight(2);
            final Arc arc5 = new Arc(net, transition2, place3);
            arc5.setWeight(2);
            final Arc arc6 = new Arc(net, place2, transition3);
            final Arc arc7 = new Arc(net, transition3, place4);
            final Arc arc8 = new Arc(net, place3, transition2);
            net.addElements(place1, place2, place3, place4, transition1, transition2, transition3, arc1, arc2, arc3, arc4, arc5, arc6, arc7, arc8);
            // place elements randomly
            Random r = new Random();
            net.getAllNodesStream().forEach(n -> n.setPosition(new Point3D(r.nextDouble(-5, 5), r.nextDouble(-5, 5), 0)));
        }
        mainController.open(new Session(net));
    }

    public static void main(String[] args) {
        launch();
    }
}
