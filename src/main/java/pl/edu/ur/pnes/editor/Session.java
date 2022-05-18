package pl.edu.ur.pnes.editor;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import pl.edu.ur.pnes.editor.history.UndoHistory;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.PetriNet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents editor session state while working with individual file.
 */
public class Session {
    final public Net net;

    final public UndoHistory undoHistory = new UndoHistory();

    ////////////////////////////////////////////////////////////////////////////////

    private ObjectProperty<File> file;

    public File getFile() {
        return file.get();
    }
    public void setFile(File file) {
        this.file.set(file);
    }

    public ObjectProperty<File> fileProperty() {
        if (file == null) {
            file = new SimpleObjectProperty<>();
        }
        return file;
    }

    ////////////////////////////////////////////////////////////////////////////////

    private BooleanProperty modified;

    public boolean isModified() {
        return modified.get();
    }
    public void setModified(boolean modified) {
        this.modified.set(modified);
    }

    public BooleanProperty modifiedProperty() {
        if (modified == null) {
            modified = new SimpleBooleanProperty(false);
        }
        return modified;
    }

    ////////////////////////////////////////////////////////////////////////////////

    private ReadOnlyStringProperty name;

    public final String getName() {
        return name == null ? null : name.get();
    }

    /**
     * Name of the session, to be displayed on working tabs and on main window title.
     */
    public final ReadOnlyStringProperty nameProperty() {
        if (name == null) {
            name = new ReadOnlyStringProperty() {
                @Override
                public Object getBean() {
                    return null;
                }
                @Override
                public String getName() {
                    return "";
                }

                @Override
                public String get() {
                    if (getFile() == null) {
                        return "(new)";
                    }
                    else {
                        return getFile().getName() + (isModified() ? "(modified)" : "");
                    }
                }

                @Override
                public void addListener(ChangeListener<? super String> listener) {
                    throw new UnsupportedOperationException();
                }
                @Override
                public void removeListener(ChangeListener<? super String> listener) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void addListener(InvalidationListener listener) {
                    fileProperty().addListener(listener);
                    modifiedProperty().addListener(listener);
                }
                @Override
                public void removeListener(InvalidationListener listener) {
                    fileProperty().removeListener(listener);
                    modifiedProperty().removeListener(listener);
                }
            };
        }
        return name;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public Session() {
        this.net = new PetriNet();
    }

    public Session(PetriNet net) {
        this.net = net;
    }

    // TODO: session constructor from file (or file path)

    ////////////////////////////////////////////////////////////////////////////////

    public void save() {
        if (file == null) {
            throw new IllegalStateException("Unnamed session needs to be 'saved as'.");
        }
        // TODO: saving
    }

    // TODO: load (from static function/factory)
}
