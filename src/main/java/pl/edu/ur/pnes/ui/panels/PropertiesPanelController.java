package pl.edu.ur.pnes.ui.panels;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.edu.ur.pnes.MainController;
import pl.edu.ur.pnes.petriNet.NetElement;
import pl.edu.ur.pnes.petriNet.netTypes.NetGroup;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;
import pl.edu.ur.pnes.petriNet.netTypes.annotations.*;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;
import pl.edu.ur.pnes.ui.utils.FXMLUtils;
import pl.edu.ur.pnes.ui.utils.Rooted;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PropertiesPanelController implements Rooted, Initializable {
    @FXML
    VBox controlsWrapper;

    public PropertiesPanelController(MainController parent) {
        parent.focusedSessionProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("PropertiesPanelController.initialize");
            if (newValue == null) return;
            VisualizerFacade visualizerFacade = newValue.getCenterPanelController().visualizerFacade;
            visualizerFacade.addEventHandler(VisualizerEvent.MOUSE_NODE_CLICKED, event -> {
                var element = visualizerFacade.getElementById(event.getClickedNodeId()).orElseThrow();
                onSelectedElementsChange(List.of(element), newValue.net.getNetType());
            });
        });
    }

    @Override
    public javafx.scene.Node getRoot() {
        return controlsWrapper;
    }


    public static final Map<NetGroup, Set<NetType>> NET_GROUP_TO_TYPES_MAP = new HashMap<>();

    static {
        EnumSet.allOf(NetType.class).forEach(netType -> {
            if (!NET_GROUP_TO_TYPES_MAP.containsKey(netType.netGroup))
                NET_GROUP_TO_TYPES_MAP.put(netType.netGroup, new HashSet<>());
            NET_GROUP_TO_TYPES_MAP.get(netType.netGroup).add(netType);
        });
    }


    private void onSelectedElementsChange(List<NetElement> netElements, NetType currentNetType) {
        Platform.runLater(() -> {
            controlsWrapper.getChildren().clear();
            createUIControls(getCommonProperties(netElements), currentNetType, netElements);
        });
    }

    private Set<NetType> getNetTypesThatFieldCanBeUsedIn(Field field) {
        Set<NetType> result = new HashSet<>();


        var l = Arrays.stream(field.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(AnnotationsPriority.class))
                .sorted(Comparator.comparingInt(a -> a.annotationType().getAnnotation(AnnotationsPriority.class).value()))
                .toList();

        if (l.isEmpty()) {
            return EnumSet.allOf(NetType.class);
        }

        l.forEach(annotation -> {
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
        return getFieldsUpTo(element.getClass(), Object.class).stream().filter(field -> field.isAnnotationPresent(EditableInGUI.class)).collect(Collectors.toUnmodifiableSet());
    }


    private Set<Field> getCommonProperties(List<NetElement> selectedElements) {
        //FIXME: allow for multiple elements
        if (selectedElements.size() != 1) {
            return Set.of();
        }
        return getSettableAttributes(selectedElements.get(0));
    }


    private void createUIControls(Set<Field> fields, NetType currentNetType, Collection<NetElement> netElementsToSet) {
        var enableFields =
                fields.stream().filter(field ->
                        getNetTypesThatFieldCanBeUsedIn(field)
                                .contains(currentNetType)
                ).toList();

        for (var field :
                enableFields) {
            var control = createUIControl(currentNetType, field, netElementsToSet);

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.TOP_LEFT);
            hBox.getChildren().add(new Label(field.getName()));
            hBox.getChildren().add(control);

            controlsWrapper.getChildren().add(hBox);
        }
    }

    public static Collection<Field> getFieldsUpTo(@NotNull Class<?> startClass,
                                                  @Nullable Class<?> exclusiveParent) {

        List<Field> currentClassFields = new ArrayList<>(List.of(startClass.getDeclaredFields()));
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && !Objects.equals(parentClass, exclusiveParent)) {
            List<Field> parentClassFields =
                    (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

    public static Collection<Method> getMethodsUpTo(@NotNull Class<?> startClass,
                                                    @Nullable Class<?> exclusiveParent) {

        List<Method> currentClassMethods = new ArrayList<>(List.of(startClass.getDeclaredMethods()));
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && !Objects.equals(parentClass, exclusiveParent)) {
            List<Method> parentClassMethods =
                    (List<Method>) getMethodsUpTo(parentClass, exclusiveParent);
            currentClassMethods.addAll(parentClassMethods);
        }

        return currentClassMethods;
    }

    private Object getFieldValue(Field field, Object fromObject) throws InvocationTargetException, IllegalAccessException {
        if (!field.isAnnotationPresent(EditableInGUI.class))
            throw new IllegalStateException("Field has no annotation EditableInGUI");

        var eig = field.getAnnotation(EditableInGUI.class);

        if (eig.useGetter()) {
            var getter = getMethodsUpTo(fromObject.getClass(), Object.class).stream().filter(f -> f.isAnnotationPresent(GetterFor.class) && Objects.equals(f.getAnnotation(GetterFor.class).value(), field.getName())).findAny().orElseThrow();
            return getter.invoke(fromObject);
        }

        return field.get(fromObject);
    }

    private void setFieldValue(Field field, Object onObject, Object value) throws InvocationTargetException, IllegalAccessException {
        if (!field.isAnnotationPresent(EditableInGUI.class))
            throw new IllegalStateException("Field has no annotation EditableInGUI");

        var eig = field.getAnnotation(EditableInGUI.class);

        if (eig.useSetter()) {
            var setter = getMethodsUpTo(onObject.getClass(), Object.class).stream().filter(f -> f.isAnnotationPresent(SetterFor.class) && Objects.equals(f.getAnnotation(SetterFor.class).value(), field.getName())).findAny().orElseThrow();
            setter.invoke(onObject, value);
            return;
        }

        field.set(onObject, value);
    }

    private Object getCommonFieldValue(Field field, Collection<?> fromObjects, Object defaultIfDifferent) {
        if (!field.isAnnotationPresent(EditableInGUI.class))
            throw new IllegalStateException("Field has no annotation EditableInGUI");

        var eig = field.getAnnotation(EditableInGUI.class);

        Object firstValue = null;

        if (fromObjects.isEmpty())
            throw new NoSuchElementException("fromObjects is empty");

        var firstObject = fromObjects.stream().findFirst().orElseThrow();

        if (eig.useGetter()) {
            var getter = getMethodsUpTo(firstObject.getClass(), Object.class).stream().filter(f -> f.isAnnotationPresent(GetterFor.class) && Objects.equals(f.getAnnotation(GetterFor.class).value(), field.getName())).findAny().orElseThrow();
            try {
                firstValue = getter.invoke(firstObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            for (var ob :
                    fromObjects) {
                Object val = null;
                try {
                    val = getter.invoke(ob);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (!Objects.equals(firstValue, val))
                    return defaultIfDifferent;
            }
            return firstValue;
        }

        try {
            firstValue = field.get(firstObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (var ob :
                fromObjects) {
            Object val = null;
            try {
                val = field.get(ob);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!Objects.equals(firstValue, val))
                return defaultIfDifferent;
        }
        return firstValue;
    }

    private void setCommonFieldValue(Field field, Collection<?> onObjects, Object value) {
        if (!field.isAnnotationPresent(EditableInGUI.class))
            throw new IllegalStateException("Field has no annotation EditableInGUI");

        var eig = field.getAnnotation(EditableInGUI.class);


        if (onObjects.isEmpty())
            throw new NoSuchElementException("onObjects is empty");

        var firstObject = onObjects.stream().findFirst().orElseThrow();


        if (eig.useSetter()) {
            var setter = getMethodsUpTo(firstObject.getClass(), Object.class).stream().filter(f -> f.isAnnotationPresent(SetterFor.class) && Objects.equals(f.getAnnotation(SetterFor.class).value(), field.getName())).findAny().orElseThrow();

            for (var ob :
                    onObjects) {
                try {
                    setter.invoke(ob, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            return;
        }

        for (var ob :
                onObjects) {
            try {
                field.set(ob, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Control createUIControl(NetType currentNetType, Field field, Collection<NetElement> netElementsToSet) {
        Class<?> usedType = null;

        try {
            if (field.isAnnotationPresent(TypesInTypesContainer.class))
                usedType = Class.forName(
                        Arrays.stream(field.getAnnotation(TypesInTypesContainer.class).value()).filter(typeInNetType ->
                                        typeInNetType.netType() == currentNetType)
                                .findFirst().map(typeInNetType -> typeInNetType.type().getName())
                                .orElseGet(() -> {
                                    if (field.isAnnotationPresent(DefaultType.class))
                                        return field.getAnnotation(DefaultType.class).value().getName();
                                    return field.getType().getName();
                                })
                );
            else
                usedType = Class.forName(Optional.ofNullable(field.getAnnotation(DefaultType.class)).map(v -> v.value().getName()).orElse(field.getType().getName()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (Objects.equals(usedType, String.class)) {
            TextField textField = new TextField((String) getCommonFieldValue(field, (netElementsToSet), ""));
            textField.setOnKeyTyped(actionEvent -> {
                setCommonFieldValue(field, (netElementsToSet), textField.getText());
            });
            return textField;
        } else if (Objects.equals(usedType, Integer.class)) {
            Spinner<Integer> integerSpinner = new Spinner<>(Integer.MIN_VALUE, Integer.MAX_VALUE, (Integer) getCommonFieldValue(field, netElementsToSet, 0));
            integerSpinner.setEditable(true);
            var factory = integerSpinner.getValueFactory();
            TextFormatter<Integer> formatter = new TextFormatter<>(factory.getConverter(), factory.getValue());
            integerSpinner.getEditor().setTextFormatter(formatter);
            // bidi-bind the values
            factory.valueProperty().bindBidirectional(formatter.valueProperty());

            integerSpinner.getEditor().setOnAction(actionEvent -> {
                setCommonFieldValue(field, (netElementsToSet), integerSpinner.getValue());
            });
            return integerSpinner;
        } else if (Objects.equals(usedType, Double.class)) {
            Spinner<Double> doubleSpinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, (Double) getCommonFieldValue(field, netElementsToSet, 0), 0.1);
            doubleSpinner.setEditable(true);
            var factory = doubleSpinner.getValueFactory();
            TextFormatter<Double> formatter = new TextFormatter<>(factory.getConverter(), factory.getValue());
            doubleSpinner.getEditor().setTextFormatter(formatter);
            // bidi-bind the values
            factory.valueProperty().bindBidirectional(formatter.valueProperty());

            doubleSpinner.getEditor().setOnAction(actionEvent -> {
                setCommonFieldValue(field, (netElementsToSet), doubleSpinner.getValue());
            });
            return doubleSpinner;
        }
//        else if (usedType != null && usedType.isAssignableFrom(Set.class)) {
//            ChoiceBox<?> choiceBox = new ChoiceBox<>();
//
//            choiceBox.
//
//        }
        throw new IllegalStateException("Unknown type for control: " + usedType.getName());
    }

    /**
     * Loads new instance of the panel.
     */
    static public PropertiesPanelController prepare(MainController parent) {

        final var loader = FXMLUtils.getLoader(PropertiesPanelController.class);
        final var controller = new PropertiesPanelController(parent);
        try {
            loader.setController(controller);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
