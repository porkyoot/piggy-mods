package is.pig.minecraft.api.spi;

import is.pig.minecraft.api.FallPredictionResult;
import java.util.Optional;

/**
 * Interface for version-specific physics simulations.
 */
public interface PhysicsAdapter {
    /**
     * Simulates a fall for the given player object.
     * 
     * @param player the player entity object
     * @return an optional fall prediction result
     */
    Optional<FallPredictionResult> simulateFall(Object player);
}
