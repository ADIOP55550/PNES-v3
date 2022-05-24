package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

public abstract class NetElement {
    private final Logger logger = LogManager.getLogger(NetElement.class);
    public final StringProperty label = new SimpleStringProperty();
    protected final Net net;
    private final String id;
    private final StringProperty name = new SimpleStringProperty() {
        @Override
        public void set(String newName) {
            // If this.name is equal to the newName, do nothing
            if (Objects.equals(newName, this.get()))
                return;

            // If name is already used, throw
            if (net.usedNames.contains(newName))
                throw new IllegalArgumentException("NetElement with this id already exists.");

            // Remove previous name from used names
            net.usedNames.remove(this.get());
            // Add new name to used names
            net.usedNames.add(newName);
            // Set name
            super.set(newName);
        }
    };

    /**
     * Indicates that this Element needs to be redrawn
     */
    public final BooleanProperty needsRedraw = new SimpleBooleanProperty(false);

    /**
     * List of ui.class values that should be added to this Element
     * On change, causes redraw
     *
     * @pnes.CausesRedraw
     */
    final ObservableList<String> classesList = FXCollections.observableArrayList();

    NetElement(Net net) {
        this.net = net;

        net.lastId++;
        this.id = String.valueOf(net.lastId);

        needsRedraw.addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                logger.info("Now needs redraw: {}", this.getName());
            else
                logger.info("No longer needs redraw: {}", this.getName());

        });
        classesList.addListener((ListChangeListener<? super String>) change -> {
            logger.info("Classes list changed!");
            System.out.println("needs redraw before: " + needsRedraw.get());
            needsRedraw.set(true);
            logger.info("Now {} shall need redraw!", this.getName());
        });
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Set name of the element. If a name is already in use in the net, IllegalArgumentException is thrown.
     *
     * @param newName New name of the element
     * @throws IllegalArgumentException If given name is already in use
     */
    public void setName(String newName) throws IllegalArgumentException {
        this.name.set(newName);
    }

    /**
     * method returning styles of the element as key-value pairs.
     * Values should follow those described in GraphStream CSS Reference: https://graphstream-project.org/doc/Advanced-Concepts/GraphStream-CSS-Reference/
     * Those styles will be appended one by one to the ui.style property in the form of `key: value;`
     *
     * @return Dictionary of styles that should be applied to the element
     */
    public Dictionary<String, String> getStyle() {
        return new Hashtable<>();
    }

    /**
     * method returning classes of the element.
     * These classes will be added to ui.class attribute in the form `class1, class2, class3, ...`
     *
     * @return List of classes that should be applied to the element
     */
    public ObservableList<String> getClasses() {
        return classesList;
    }
}
