package is.pig.minecraft.api;

import java.util.Map;

/**
 * An interface for telemetry entries that have a machine-readable key and structured data.
 */
public interface StructuredEvent extends TelemetryEntry {

    /**
     * Gets the unique event key for this event type.
     *
     * @return the event key
     */
    String getEventKey();

    /**
     * Gets a map of key-value pairs representing the structured data of this event.
     *
     * @return the event data map
     */
    Map<String, Object> getEventData();

    /**
     * Gets an icon (typically a Unicode emoji) representing the category of this event.
     *
     * @return the category icon string
     */
    default String getCategoryIcon() {
        return "🔹";
    }

    /**
     * Indicates if this event is notable and should be reported as a high-signal admin alert.
     *
     * @return true if notable, false otherwise
     */
    default boolean isNotable() {
        return false;
    }

    /**
     * Indicates if this event represents a system failure or an unsuccessful meta-action.
     *
     * @return true if failure, false otherwise
     */
    default boolean isFailure() {
        return false;
    }
}
