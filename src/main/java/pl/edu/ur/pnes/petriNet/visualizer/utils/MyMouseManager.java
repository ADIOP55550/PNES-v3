package pl.edu.ur.pnes.petriNet.visualizer.utils;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.InteractiveElement;
import org.graphstream.ui.view.util.MouseManager;

import java.util.EnumSet;

public class MyMouseManager implements MouseManager {
    /**
     * The view this manager operates upon.
     */
    protected View view;

    /**
     * The graph to modify according to the view actions.
     */
    protected GraphicGraph graph;

    final private EnumSet<InteractiveElement> types;

    @Override
    public EnumSet<InteractiveElement> getManagedTypes() {
        return types;
    }

    public MyMouseManager() {
        this(EnumSet.of(InteractiveElement.NODE, InteractiveElement.SPRITE));
    }

    public MyMouseManager(EnumSet<InteractiveElement> types) {
        this.types = types;
    }

    @Override
    public void init(GraphicGraph graph, View view) {
        this.view = view;
        this.graph = graph;
        view.addListener(MouseEvent.MOUSE_PRESSED, (EventHandler<MouseEvent>) this::onMousePressed);
        view.addListener(MouseEvent.MOUSE_DRAGGED, (EventHandler<MouseEvent>) this::onMouseDragged);
        view.addListener(MouseEvent.MOUSE_RELEASED, (EventHandler<MouseEvent>) this::onMouseReleased);
    }

    @Override
    public void release() {
        view.removeListener(MouseEvent.MOUSE_PRESSED, (EventHandler<MouseEvent>) this::onMousePressed);
        view.removeListener(MouseEvent.MOUSE_DRAGGED, (EventHandler<MouseEvent>) this::onMouseDragged);
        view.removeListener(MouseEvent.MOUSE_RELEASED, (EventHandler<MouseEvent>) this::onMouseReleased);
    }

    protected GraphicElement element;
    protected double x1, y1;

    protected void onMousePressed(MouseEvent event) {
        element = view.findGraphicElementAt(types, event.getX(), event.getY());
        x1 = event.getX();
        y1 = event.getY();
        if (element == null) {
            view.requireFocus();
            view.beginSelectionAt(x1, y1);
        }
        else {
            view.freezeElement(element, true);
            element.setAttribute("ui.clicked");
        }
    }

    protected void onMouseDragged(MouseEvent event) {
        if (element == null) {
            view.selectionGrowsAt(event.getX(), event.getY());
        }
        else {
            if (event.isShiftDown() || !element.hasAttribute("ui.selected")) {
                view.moveElementAtPx(element, event.getX(), event.getY());
            }
            else {
                final Point3 p = view.getCamera().transformPxToGu(event.getX(), event.getY());
                final double dx = p.x - element.getX();
                final double dy = p.y - element.getY();
                graph.nodes().filter(n -> n.hasAttribute("ui.selected")).forEach(node -> {
                    final var element = (GraphicElement) node;
                    element.move(
                            element.getX() + dx,
                            element.getY() + dy,
                            element.getZ()
                    );
                });
            }
        }
    }

    protected void onMouseReleased(MouseEvent event) {
        if (element == null) {
            double x2 = event.getX();
            double y2 = event.getY();

            if (x1 > x2) {
                double t = x1;
                x1 = x2;
                x2 = t;
            }
            if (y1 > y2) {
                double t = y1;
                y1 = y2;
                y2 = t;
            }

            final var elements = view.allGraphicElementsIn(types, x1, y1, x2, y2);
            if (event.isShiftDown()) {
                boolean anyNew = false;
                for (GraphicElement element : elements) {
                    if (!element.hasAttribute("ui.selected")) {
                        anyNew = true;
                    }
                }
                if (anyNew) {
                    // if anything new and shift held, add the selection
                    for (GraphicElement element : elements) {
                        if (!element.hasAttribute("ui.selected")) {
                            element.setAttribute("ui.selected");
                        }
                    }
                } else {
                    // if nothing new and shift down, subtract the selection
                    for (GraphicElement element : elements) {
                        if (element.hasAttribute("ui.selected")) {
                            element.removeAttribute("ui.selected");
                        }
                    }
                }
            }
            else {
                // if no shift, replace selection
                unselectAll();
                for (GraphicElement element : elements) {
                    if (!element.hasAttribute("ui.selected")) {
                        element.setAttribute("ui.selected");
                    }
                }
            }

            view.endSelectionAt(event.getX(), event.getY());
        }
        else {
            if (element.hasAttribute("ui.clicked")) {
                element.removeAttribute("ui.clicked");
            }
            if (event.isShiftDown()) {
                // toggle selection on shift-clicking
                if (element.hasAttribute("ui.selected")) {
                    element.removeAttribute("ui.selected");
                }
                else {
                    element.setAttribute("ui.selected");
                }
            }
        }
    }

    protected void unselectAll() {
        graph.nodes().filter(n -> n.hasAttribute("ui.selected")).forEach(n -> n.removeAttribute("ui.selected"));
        graph.sprites().filter(s -> s.hasAttribute("ui.selected")).forEach(s -> s.removeAttribute("ui.selected"));
        graph.edges().filter(e -> e.hasAttribute("ui.selected")).forEach(e -> e.removeAttribute("ui.selected"));
        element = null;
    }
}
