package pl.edu.ur.pnes.petriNet.visualizer.events.attribute;

import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public class VisualizerAttributeChangedEvent extends VisualizerAttributeEvent {

    private final String targetId;
    private final AttributeEventTargetType targetType;
    private final String attributeName;
    private final Object newValue;
    private final Object oldValue;

    public String getAttributeName() {
        return attributeName;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public String getTargetId() {
        return targetId;
    }

    public AttributeEventTargetType getTargetType() {
        return targetType;
    }

    public VisualizerAttributeChangedEvent(AttributeEventTargetType targetType, String targetId, String attributeName, Object oldValue, Object newValue) {
        super(VisualizerEvent.ATTRIBUTE_CHANGED);
        this.targetType = targetType;
        this.targetId = targetId;
        this.attributeName = attributeName;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }
}