package pl.edu.ur.pnes.petriNet.events;

import pl.edu.ur.pnes.petriNet.NetGroup;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;

/**
 * Event dispatched when net type changes
 */
public class NetTypeChangedEvent extends NetEvent {

    private final NetGroup newGroup;

    /**
     * New net type group
     * same as {@link NetType#netGroup}
     * @return new group value
     */
    public NetGroup getNewGroup() {
        return newGroup;
    }

    /**
     * New net type
     * @return new net type
     */
    public NetType getNewType() {
        return newType;
    }

    private final NetType newType;

    public NetTypeChangedEvent(NetType newType, NetGroup newGroup) {
        super(TYPE_CHANGED);
        this.newType = newType;
        this.newGroup = newGroup;
    }
}
