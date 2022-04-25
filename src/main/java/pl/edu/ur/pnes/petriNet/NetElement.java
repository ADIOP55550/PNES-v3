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
import java.util.List;
import java.util.Objects;

public abstract class NetElement {
    private final Logger logger = LogManager.getLogger(NetElement.class);
    public final StringProperty label = new SimpleStringProperty();
    private final Net net;
    private final StringProperty id = new SimpleStringProperty() {
        @Override
        public void set(String newId) {
            // If this.id is equal to the newId, do nothing
            if (Objects.equals(newId, this.get()))
                return;

            // If id is already used, throw
            if (net.usedIds.contains(newId))
                throw new IllegalArgumentException("NetElement with this id already exists.");

            // Remove previous id from used ids
            net.usedIds.remove(this.get());
            // Add new id to used ids
            net.usedIds.add(newId);
            // Set id
            super.set(newId);
        }
    };
    public final BooleanProperty needsRedraw = new SimpleBooleanProperty(false);
    final ObservableList<String> classesList = FXCollections.observableArrayList();

    NetElement(Net net) {
        this.net = net;
        needsRedraw.addListener((observableValue, aBoolean, t1) -> {
            if (t1)
                logger.info("Now needs redraw: {}", this.getId());
            else
                logger.info("No longer needs redraw: {}", this.getId());
        });
        classesList.addListener((ListChangeListener<? super String>) change -> {
            logger.info("Classes list changed!");
            System.out.println("needs redraw before: " + needsRedraw.get());
            needsRedraw.set(true);
            logger.info("Now {} shall need redraw!", this.getId());
        });
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
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
    public List<String> getClasses() {
        return classesList;
    }

    /**
     * Set id of the element. If an id is already in use in the net, IllegalArgumentException is thrown.
     *
     * @param newId New id of the element
     * @throws IllegalArgumentException If given id is already in use
     */
    public void setId(String newId) throws IllegalArgumentException {
        this.id.set(newId);
    }


}
