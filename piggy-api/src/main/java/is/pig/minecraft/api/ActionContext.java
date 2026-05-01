package is.pig.minecraft.api;

/**
 * Platform-agnostic context for an action being evaluated by anti-cheat.
 * 
 * @param worldId  The identifier of the world/dimension.
 * @param pos      The block position of the action.
 * @param stateId  The platform-specific identifier of the block state (optional/opaque).
 * @param eyePos   The player's eye position.
 * @param lookVec  The player's view vector.
 */
public record ActionContext(String worldId, BlockPos pos, String stateId, Vec3 eyePos, Vec3 lookVec) {}
