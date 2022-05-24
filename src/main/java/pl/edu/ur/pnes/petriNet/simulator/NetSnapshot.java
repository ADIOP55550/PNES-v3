package pl.edu.ur.pnes.petriNet.simulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final Map<String, Object> tokens = new HashMap<>();
    private boolean alreadyRestored = false;

    private static final Logger logger = LogManager.getLogger(NetSnapshot.class);

    public NetSnapshot(Net net) {
        logger.debug("Creating snapshot of the net " + net.hashCode());
        net.getPlaces().forEach(place -> {
            tokens.put(place.getId(), place.getTokens());
        });
        this.netHashCode = net.hashCode();
    }

    public static NetSnapshot of(Net net) {
        return new NetSnapshot(net);
    }

    /**
     * Restores this snapshot to the given net.
     * If the hash of given net is not equal to when the snapshot was created, throws.
     *
     * @param net Where to restore the snapshot to.
     * @throws IllegalStateException if Net hash changed or any Node cannot be found
     */
    public void restoreTo(Net net) {
        if (netHashCode == null) {
            throw new IllegalStateException("Snapshot not yet created");
        }

        logger.debug("Restoring snapshot of the net " + netHashCode + " to the net " + net.hashCode());

        if (netHashCode != net.hashCode()) {
            logger.error("Net was changed after creating snapshot, cannot safely restore!");
            return;
        }

        this.tokens.forEach((placeId, tokensCount) -> {
            ((Place)
                    net
                            .getElementById(placeId)
                            .orElseThrow(() -> new IllegalStateException("Cannot restore to this net, place with id " + placeId + " not found"))
            )
                    .setTokens(tokensCount);
        });

        alreadyRestored = true;
    }

    public boolean isAlreadyRestored() {
        return alreadyRestored;
    }
}
