package is.pig.minecraft.api;

/**
 * Data structure to hold the results of a fall prediction.
 *
 * @param landingPos      The exact block the player will hit.
 * @param hitVec          The exact coordinate of impact.
 * @param ticksToImpact   How many ticks until collision.
 * @param fallDistance    The accumulated fall distance upon impact.
 * @param expectedDamage  The calculated health loss.
 * @param isFatal         True if expectedDamage >= player's current health.
 */
public record FallPredictionResult(
        BlockPos landingPos,
        Vec3 hitVec,
        int ticksToImpact,
        float fallDistance,
        float expectedDamage,
        boolean isFatal
) {
}
