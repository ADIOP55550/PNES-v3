package pl.edu.ur.pnes.ui.panels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import pl.edu.ur.pnes.MainApp;
import pl.edu.ur.pnes.petriNet.NetElement;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.EditableInGUI;
import pl.edu.ur.pnes.ui.utils.FXMLUtils;
import pl.edu.ur.pnes.ui.utils.Rooted;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectTreePanelController implements Rooted, Initializable {
    @FXML
    AnchorPane root;

    @Override
    public javafx.scene.Node getRoot() {
        return root;
    }

    private void onSelectedElementsChange(Collection<NetElement> netElements) {

    }

    private Set<Field> getSettableAttributes(NetElement element) {
        return Arrays.stream(element.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(EditableInGUI.class)).collect(Collectors.toUnmodifiableSet());
    }

    private Set<Field> getCommonProperties(List<NetElement> selectedElements) {
       //FIXME: allow for multiple elements
        if (selectedElements.size() != 1) {
            return Set.of();
        }
        return getSettableAttributes(selectedElements.get(0));
    }

//    private void createUIControls(Set<Field> fields){
//        //TODO: przefiltrowac fields ktore moga byc uzywane w obecnym NetType lub NetGroup
//        fields.forEach(field -> field);
//
//        fields.fi
//
//    }



    /**
     * Loads new instance of the panel.
     */
    static public ProjectTreePanelController prepare() {
        final var loader = FXMLUtils.getLoader(ProjectTreePanelController.class);
        final var controller = new ProjectTreePanelController();
        try {
            loader.setController(controller);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    protected ProjectTreePanelController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        MainApp.mainController.focusedSessionProperty().addListener((observableValue, session, currentFocusedSession) -> {
            //dostosuj PP do focused session

        });


    }
}
