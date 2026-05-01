package is.pig.minecraft.api;

/**
 * Pure Java equivalent of Minecraft BlockHitResult.
 */
public record BlockHitResult(
    Vec3 pos,
    Direction side,
    BlockPos blockPos,
    boolean insideBlock
) implements HitResult {
    @Override
    public Vec3 hitVec() {
        return pos;
    }

    @Override
    public Type type() {
        return Type.BLOCK;
    }
}
