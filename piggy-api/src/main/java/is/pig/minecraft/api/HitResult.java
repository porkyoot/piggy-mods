package is.pig.minecraft.api;

public interface HitResult {
    Vec3 hitVec();
    Type type();

    enum Type {
        MISS,
        BLOCK,
        ENTITY
    }
}
