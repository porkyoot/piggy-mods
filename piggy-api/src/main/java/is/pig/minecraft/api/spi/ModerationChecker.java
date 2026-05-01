package is.pig.minecraft.api.spi;

import is.pig.minecraft.api.ModerationResult;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service Provider Interface for chat and behavior moderation.
 * Decoupled from Minecraft internals.
 */
public interface ModerationChecker {
    /**
     * Checks if a message should be blocked.
     * 
     * @param playerUuid The UUID of the player who sent the message.
     * @param message    The message content.
     * @return A CompletableFuture that completes with the ModerationResult.
     */
    CompletableFuture<ModerationResult> check(UUID playerUuid, String message);

    /**
     * @return The human-readable name of this checker.
     */
    String getName();
}
