package is.pig.minecraft.api.spi;

import java.util.UUID;

/**
 * Service Provider Interface for server-side chat and sign moderation.
 */
public interface ModerationAdapter {
    
    /**
     * Extracts the content of a message handle.
     */
    String getMessageContent(Object messageObj);

    /**
     * Re-broadcasts a message to the player list if it passed moderation.
     */
    void broadcastMessage(Object player, Object message, Object params);

    /**
     * Returns the name of the player.
     */
    String getPlayerName(Object player);

    /**
     * Returns the current server tick count.
     */
    int getServerTickCount(Object player);

    /**
     * Returns the player's UUID.
     */
    UUID getPlayerUUID(Object player);
}
