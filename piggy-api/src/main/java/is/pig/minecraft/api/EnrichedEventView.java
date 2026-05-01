package is.pig.minecraft.api;

import java.util.Map;

/**
 * Internal view of an event after enrichment.
 * 
 * @param parent       The original structured event.
 * @param enrichedData The mutable data map after global enrichment.
 */
public record EnrichedEventView(StructuredEvent parent, Map<String, Object> enrichedData) {}
