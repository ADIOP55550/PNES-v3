package pl.edu.ur.pnes.petriNet.visualizer.events.attribute;

public class VisualizerAttributeRemovedEvent extends VisualizerAttributeEvent {

    private final String targetId;
    private final AttributeEventTargetType targetType;
    private final String attributeName;

    public String getAttributeName() {
        return attributeName;
    }

    public String getTargetId() {
        return targetId;
    }

    public AttributeEventTargetType getTargetType() {
        return targetType;
    }

    public VisualizerAttributeRemovedEvent(AttributeEventTargetType targetType, String targetId, String attributeName) {
        super(ATTRIBUTE_REMOVED);
        this.targetType = targetType;
        this.targetId = targetId;
        this.attributeName = attributeName;
    }
}
