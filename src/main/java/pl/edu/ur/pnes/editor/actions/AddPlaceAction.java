package pl.edu.ur.pnes.editor.actions;

import pl.edu.ur.pnes.editor.history.UndoableAction;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Place;

public class AddPlaceAction extends UndoableAction {
    public final Net net;
    public final double[] position;
    protected Place place;

    public AddPlaceAction(Net net, double[] position) {
        this.net = net;
        this.position = position;
    }

    @Override
    public String description() {
        return "Add place";
    }

    @Override
    public String details() {
        return "Add place at (%f, %f)".formatted(position[0], position[1]);
    }

    @Override
    public void undo() {
        net.removeElement(place);
        applied = false;
    }

    @Override
    public void redo() {
        apply();
    }

    public void apply() {
        place = new Place(net);
        net.addElement(place, position);
        applied = true;
    }
}
