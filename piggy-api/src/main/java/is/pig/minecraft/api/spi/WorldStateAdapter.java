package is.pig.minecraft.api.spi;

import is.pig.minecraft.api.BlockPos;
import is.pig.minecraft.api.Vec3;
import java.util.List;

/**
 * Service Provider Interface for querying world state without Minecraft dependencies.
 */
public interface WorldStateAdapter {
    /**
     * Checks if a block position is considered "exposed" to air or water.
     */
    boolean isExposed(String worldId, BlockPos pos);

    /**
     * Opens a screen.
     */
    void openScreen(Object client, Object screen);

    is.pig.minecraft.api.HitResult getCrosshairTarget(Object client);

    double getPlayerReachDistance(Object client);

    boolean isReplaceable(String worldId, BlockPos pos);

    /**
     * Returns the block state identifier at the given position.
     */
    String getBlockStateId(String worldId, BlockPos pos);

    /**
     * Checks if the block at the given position is air or empty.
     */
    boolean isEmpty(String worldId, BlockPos pos);

    /**
     * Checks if an entity is currently intersecting with the given block position.
     */
    boolean isEntityIntersecting(Object entity, BlockPos pos);

    /**
     * Checks if a player is on the ground.
     */
    boolean isPlayerOnGround(Object player);

    boolean isPlayerSprinting(Object player);

    Vec3 getPlayerDeltaMovement(Object player);

    Vec3 getPlayerPosition(Object player);

    BlockPos getPlayerBlockPos(Object player);

    Object getPlayerInventory(Object player);

    Object getPlayerMainHandItem(Object player);

    Object getPlayerOffhandItem(Object player);

    boolean isPlayerDeadOrDying(Object player);

    boolean isBlockReplaceable(String worldId, BlockPos pos);

    int getPing();

    String getCurrentWorldId();

    void sendMessage(Object player, String message);

    void setInteractionDelay(Object client, int delay);

    void sendMessage(Object player, String message, boolean overlay);

    Vec3 getPlayerVelocity(Object player);
    
    int getPlayerLatency(Object client);

    boolean isWorldUltraWarm();

    void setPlayerPosition(Object player, Vec3 pos);

    double getPlayerEyeHeight(Object player);

    boolean isCreative(Object player);

    boolean isOnGround(Object player);

    boolean isFalling(Object player);

    /**
     * Returns the player's eye position as an API Vec3.
     */
    Vec3 getPlayerEyePosition(Object player);

    /**
     * Finds nearby valuable ores within a localized scope.
     */
    List<BlockPos> getOresInRadius(String worldId, BlockPos pos, int radius);

    /**
     * Checks if a block state ID matches a specific type (e.g., "stone").
     */
    boolean isType(String stateId, String type);

    /**
     * Checks if the block at the given position is a container or has a menu provider.
     */
    boolean isContainer(String worldId, BlockPos pos);

    /**
     * Checks if the given entity is a container or has a menu provider.
     */
    boolean isContainer(Object entity);

    void swingHand(Object client, InteractionHand hand);

    Object getClient();

    /**
     * Returns the index of the currently selected hotbar slot.
     */
    int getSelectedSlot(Object player);

    /**
     * Checks if the given entity is alive.
     */
    boolean isAlive(Object entity);

    /**
     * Returns the agnostic block state at the given position.
     */
    Object getBlockState(String worldId, BlockPos pos);

    /**
     * Returns the destruction speed of the block state at the given position.
     */
    float getDestroySpeed(Object blockState, String worldId, BlockPos pos);

    /**
     * Returns the unique identifier of the block (e.g., "minecraft:stone").
     */
    String getBlockId(Object blockState);
}
