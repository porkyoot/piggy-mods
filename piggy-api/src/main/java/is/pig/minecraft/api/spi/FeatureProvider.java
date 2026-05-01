package is.pig.minecraft.api.spi;

import java.util.List;

/**
 * Service Provider Interface for discovering and initializing Piggy Mod features.
 */
public interface FeatureProvider {
    /**
     * Discovers all modular features available in the current context.
     *
     * @return A standard Java List of PiggyFeatures.
     */
    List<PiggyFeature> getFeatures();

    /**
     * Called during mod initialization.
     */
    default void onInitialize() {}

    /**
     * Called every client or server tick.
     */
    default void onTick(Object client) {}

    /**
     * Called during the world rendering phase.
     */
    default void onRender(Object client, Object stack, float partialTicks) {}

    /**
     * Returns the unique identifier for this feature set.
     */
    String getFeatureId();

    /**
     * Dispatched by the core orchestrator when a global state change occurs.
     *
     * @param featureId The target feature ID to toggle.
     * @param state     True to enable, false to disable.
     */
    default void onFeatureToggle(String featureId, boolean state) {
        // Optional hook for cleanup or dynamic loading
    }
}
