package is.pig.minecraft.api.spi;

import is.pig.minecraft.api.BlockPos;
import is.pig.minecraft.api.BlockHitResult;
import is.pig.minecraft.api.InteractionHand;

/**
 * High-level orchestration wrapper targeting physical interactions.
 */
public interface WorldInteractionAdapter {
    
    /**
     * Attempts a survival or creative block break sequence.
     */
    void breakBlock(Object client, BlockPos pos);

    /**
     * Places a specific loaded item at target bounds.
     */
    void useItemOn(Object client, InteractionHand hand, BlockHitResult hitResult);

    /**
     * Interacts with entity wrappers using target priority queues.
     */
    void interactEntity(Object client, Object entity, boolean sneak);

    /**
     * Performs a standard item usage action (equivalent to right-click).
     */
    void useItem(Object client, InteractionHand hand);

    /**
     * Attacks a specific entity.
     */
    void attackEntity(Object client, Object entity);
}

