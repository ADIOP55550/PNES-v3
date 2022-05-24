package pl.edu.ur.pnes.petriNet.netTypes;

import pl.edu.ur.pnes.petriNet.netTypes.annotations.UsedInNetGroup;

/**
 * Groups multiple {@link pl.edu.ur.pnes.petriNet.netTypes.NetType}s
 * Allow for conditional enabling/disabling of fields when used with {@link UsedInNetGroup} and {@link pl.edu.ur.pnes.petriNet.netTypes.annotations.NotInNetGroup}
 *
 * @see pl.edu.ur.pnes.petriNet.netTypes.NetType
 * @see pl.edu.ur.pnes.petriNet.netTypes.annotations.NotInNetGroup
 * @see UsedInNetGroup
 */
public enum NetGroup {
    NONE("NONE"),
    CLASSICAL("Classical"),
    NON_CLASSICAL("Non-classical");

    public String getName() {
        return name;
    }

    String name;

    NetGroup(String name) {
        this.name = name;
    }
}
