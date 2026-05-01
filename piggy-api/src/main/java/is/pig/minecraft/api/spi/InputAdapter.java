package is.pig.minecraft.api.spi;

/**
 * Platform-agnostic input state queries.
 */
public interface InputAdapter {
    
    /**
     * Checks if a specific key is currently held down.
     * 
     * @param keyId e.g., "piggy-build:directional" or "minecraft:jump"
     */
    boolean isKeyDown(String keyId);

    /**
     * Sets the state of a specific key.
     */
    void setKeyDown(String keyId, boolean down);

    /**
     * Registers a key mapping.
     */
    void registerKey(String keyId, String defaultKey, String category);
}
