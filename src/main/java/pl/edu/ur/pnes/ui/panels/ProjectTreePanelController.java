package pl.edu.ur.pnes.ui.panels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import pl.edu.ur.pnes.MainApp;
import pl.edu.ur.pnes.petriNet.NetElement;
import pl.edu.ur.pnes.petriNet.netTypes.NetGroup;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.*;
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


    public static final Map<NetGroup, Set<NetType>> NET_GROUP_TO_TYPES_MAP = new HashMap<>();

    static {
        EnumSet.allOf(NetType.class).forEach(netType -> {
            if (!NET_GROUP_TO_TYPES_MAP.containsKey(netType.netGroup))
                NET_GROUP_TO_TYPES_MAP.put(netType.netGroup, new HashSet<>());
            NET_GROUP_TO_TYPES_MAP.get(netType.netGroup).add(netType);
        });
    }

    ;

    private void onSelectedElementsChange(Collection<NetElement> netElements) {

    }

    private Set<NetType> canBeUsedIn(Field field) {
        Set<NetType> result = new HashSet<>();

        Arrays.stream(field.getAnnotations())
                .filter(annotation -> annotation.annotationType() == AnnotationsPriority.class)
                .sorted(Comparator.comparingInt(o -> ((AnnotationsPriority) o).value()))
                .forEachOrdered(annotation -> {
                    if (annotation instanceof UsedInNetGroups usedInNetGroups) {
                        for (UsedInNetGroup usedInNetGroup : usedInNetGroups.value()) {
                            result.addAll(NET_GROUP_TO_TYPES_MAP.get(usedInNetGroup.value()));
                        }

                    } else if (annotation instanceof NotInNetGroups notInNetGroups) {
                        for (NotInNetGroup notInNetGroup : notInNetGroups.value()) {
                            result.removeAll(NET_GROUP_TO_TYPES_MAP.get(notInNetGroup.value()));
                        }

                    } else if (annotation instanceof UsedInNetTypes usedInNetTypes) {
                        for (UsedInNetType usedInNetType : usedInNetTypes.value()) {
                            result.add(usedInNetType.value());
                        }

                    } else if (annotation instanceof NotInNetTypes notInNetTypes) {
                        for (NotInNetType notInNetType : notInNetTypes.value()) {
                            result.remove(notInNetType.value());
                        }
                    } else {
                        throw new IllegalStateException();
                    }
                });

        return result;
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

    private void createUIControls(Set<Field> fields, NetType currentNetType) {
        var enableFields =
                fields.stream().filter(field ->
                        canBeUsedIn(field)
                                .contains(currentNetType)
                ).toList();

        for (var field :
                enableFields) {
            Class<?> usedType = null;

            try {
                usedType = Class.forName(
                        Arrays.stream(field.getAnnotation(TypesInTypesContainer.class).value()).filter(typeInNetType ->
                                        typeInNetType.netType() == currentNetType)
                                .findFirst().map(typeInNetType -> typeInNetType.type().getName())
                                .orElse(field.getType().getName())
                );
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            //dla kazdej mozliwej opcji wyciagniecia danych z obiektu lub przypisania
//            if (Objects.equals(usedType, String.class)) {
//                TextField textField = new TextField();
//                textField.setOnKeyTyped(actionEvent -> {
//                    try {
//                        field.set(this, textField.getText());
//
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                });
//            } else if (Objects.equals(usedType, Integer.class)) {
//                Spinner<Integer> integerSpinner = new Spinner<>();
//
//                try {
//                    integerSpinner.getValueFactory().setValue((Integer) field.get(this));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//                integerSpinner.getEditor().setOnAction(actionEvent -> {
//                    try {
//                        field.set(this, integerSpinner.getValue());
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                });
//            } else if (usedType.equals(Double.class)) {
//                Spinner<Double> doubleSpinner = new Spinner<>();
//                try {
//                    doubleSpinner.getValueFactory().setValue((Double) field.get(this));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//                doubleSpinner.getEditor().setOnAction(actionEvent -> {
//                    try {
//                        field.set(this, doubleSpinner.getValue());
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
        }
    }

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
