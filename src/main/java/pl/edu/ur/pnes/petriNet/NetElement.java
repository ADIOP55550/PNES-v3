package pl.edu.ur.pnes.petriNet;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public abstract class NetElement {
    public final StringProperty label = new SimpleStringProperty();
    private final Net net;
    private String id;


    NetElement(Net net) {
        this.net = net;
    }

    public String getId() {
        return id;
    }

    /**
     * method returning styles of the element as key-value pairs.
     * Values should follow those described in GraphStream CSS Reference: https://graphstream-project.org/doc/Advanced-Concepts/GraphStream-CSS-Reference/
     * Those styles will be appended one by one to the ui.style property in the form of `key: value;`
     * @return Dictionary of styles that should be applied to the element
     */
    public Dictionary<String, String> getStyle() {
        return new Hashtable<>();
    }

    /**
     * method returning classes of the element.
     * These classes will be added to ui.class attribute in the form `class1, class2, class3, ...`
     * @return List of classes that should be applied to the element
     */
    public List<String> getClasses() {
        return List.of();
    }

    /**
     * Set id of the element. If an id is already in use in the net, IllegalArgumentException is thrown.
     * @param newId New id of the element
     * @throws IllegalArgumentException If given id is already in use
     */
    public void setId(String newId) throws IllegalArgumentException {
        // If this.id is equal to the newId, do nothing
        if (Objects.equals(newId, this.id))
            return;

        // If id is already used, throw
        if (net.usedIds.contains(newId)) throw new IllegalArgumentException("NetElement with this id already exists.");

        // Remove previous id from used ids
        net.usedIds.remove(this.id);
        // Add new id to used ids
        net.usedIds.add(newId);
        // Set id
        this.id = newId;
    }


}
