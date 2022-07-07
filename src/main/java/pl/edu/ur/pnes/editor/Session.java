package pl.edu.ur.pnes.editor;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.scene.control.Tab;
import javafx.stage.Window;
import pl.edu.ur.pnes.editor.history.UndoHistory;
import pl.edu.ur.pnes.parser.PnmlWriter;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.PetriNet;
import pl.edu.ur.pnes.ui.panels.CenterPanelController;

import java.io.File;

/**
 * Represents editor session state while working with individual file.
 */
public class Session {
    final public Net net;

    final public UndoHistory undoHistory = new UndoHistory(this);

    ////////////////////////////////////////////////////////////////////////////////

    ObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>(Mode.EDIT);
    private Tab uiTab;

    public ObjectProperty<Mode> mode() {
        return modeProperty;
    }

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
    private static int unsavedID = 1;

    public final String getName() {
        return name == null ? null : name.get();
    }

    /**
     * Name of the session, to be displayed on working tabs and on main window title.
     */
    public final ReadOnlyStringProperty nameProperty() {
        if (name == null) {
            final var binding = new StringBinding() {
                @Override
                protected String computeValue() {
                    if (getFile() == null)
                        return "unsaved"+unsavedID;
                    else
                        return getFile().getName() + (isModified() ? "(modified)" : ""); //"*" instead modified?
                }
            };
            fileProperty().addListener(observable -> binding.invalidate());
            modifiedProperty().addListener(observable -> binding.invalidate());
            return new SimpleStringProperty() {{
                bind(binding);
            }};
        }
        return name;
    }

    ////////////////////////////////////////////////////////////////////////////////

    public Session() {
        this(new PetriNet());
        unsavedID++;
    }

    public Session(PetriNet net) {
        this.net = net;
    }

    // TODO: session constructor from file (or file path)

    ////////////////////////////////////////////////////////////////////////////////

    public void save(Window scene) { //temp TODO
        PnmlWriter pnmlWriter = new PnmlWriter();
        pnmlWriter.generatePnmlFromNet((PetriNet) net);
        if(fileProperty().get() == null){ //unsaved file
            fileProperty().set(pnmlWriter.saveToNewFile(scene));
        } else {
            pnmlWriter.saveExistedFile(fileProperty().get());
        }

//        if (getFile() == null) {
//            throw new IllegalStateException("Unnamed session needs to be 'saved as'.");
//        }
        // TODO: saving
    }

    ////////////////////////////////////////////////////////////////////////////////

    public void close() {
        // TODO: close session
    }

    public Tab getUiTab() {
        return uiTab;
    }

    public void setUiTab(Tab uiTab) {
        this.uiTab = uiTab;
    }

    // TODO: load (from static function/factory)


    ////////////////////////////////////////////////////////////////////////////////
    private CenterPanelController centerPanelController;

    public CenterPanelController getCenterPanelController() {
        return centerPanelController;
    }

    public void setCenterPanelController(CenterPanelController centerPanelController) {

        this.centerPanelController = centerPanelController;
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(centerPanelController);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    ////////////////////////////////////////////////////////////////////////////////
}
