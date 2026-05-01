package is.pig.minecraft.api;

/**
 * Agnostic representation of chunk coordinates.
 */
public record ChunkPos(int x, int z) {
    public static ChunkPos fromBlockPos(BlockPos pos) {
        return new ChunkPos(pos.x() >> 4, pos.z() >> 4);
    }
}
