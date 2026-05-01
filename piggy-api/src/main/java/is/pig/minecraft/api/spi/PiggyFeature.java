package is.pig.minecraft.api.spi;

/**
 * Represents a stateful feature within the Piggy Mod suite.
 * 
 * @param id          Unique identifier (e.g., "piggy-build:auto-mlg")
 * @param name        Human-readable name
 * @param enabled     Current toggle state
 * @param description    Short summary of capabilities
 * @param featureInstance The actual instance of the feature tool (e.g., ModerationChecker)
 */
public record PiggyFeature(String id, String name, boolean enabled, String description, Object featureInstance) {}
