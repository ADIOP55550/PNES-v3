package pl.edu.ur.pnes.petriNet.simulator;

import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Place;

import java.util.HashMap;
import java.util.Map;

/**
 * A class saving current state of a net to be restored later
 * Currently saves:
 * - tokens in Places
 */
public class NetSnapshot {
    private Integer netHashCode = null;
    private final Map<String, Double> tokens = new HashMap<>();

    public NetSnapshot(Net net) {
        System.out.println("Creating snapshot of the net " + net.hashCode());
        net.getPlaces().forEach(place -> {
            tokens.put(place.getId(), place.getTokens());
        });
        this.netHashCode = net.hashCode();
    }

    public static NetSnapshot of(Net net) {
        return new NetSnapshot(net);
    }

    public void restoreTo(Net net) {
        if (netHashCode == null) {
            throw new IllegalStateException("Snapshot not yet created");
        }

        System.out.println("Restoring snapshot of the net " + net.hashCode() + " to the net " + net.hashCode());

        this.tokens.forEach((placeId, tokensCount) -> {
            ((Place)
                    net
                            .getElementById(placeId)
                            .orElseThrow(() -> new IllegalStateException("Cannot restore to this net, place with id " + placeId + " not found"))
            )
                    .setTokens(tokensCount);
        });
    }
}
