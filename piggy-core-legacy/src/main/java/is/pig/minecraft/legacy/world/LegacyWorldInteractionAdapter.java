package is.pig.minecraft.legacy.world;

import is.pig.minecraft.api.BlockPos;
import is.pig.minecraft.api.BlockHitResult;
import is.pig.minecraft.api.InteractionHand;
import is.pig.minecraft.api.spi.WorldInteractionAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

/**
 * 1.21.X specialized implementation of WorldInteractionAdapter.
 * Respects the split block removal lifecycle and modern interaction results.
 */
public class LegacyWorldInteractionAdapter implements WorldInteractionAdapter {

    @Override
    public void breakBlock(Object client, BlockPos pos) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.level != null) {
            net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
            
            // 1.21 Split Lifecycle: Ensure pre-removal side effects are handled if we were doing manual removal.
            // For player-driven breaking, MultiPlayerGameMode.destroyBlock handles this correctly.
            // It triggers Block.onStateReplaced with appropriate flags (3 | 16 | 32).
            mc.gameMode.destroyBlock(mcPos);
        }
    }

    @Override
    public void useItemOn(Object client, InteractionHand hand, BlockHitResult hitResult) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.player != null) {
            net.minecraft.world.InteractionHand mcHand = (hand == InteractionHand.MAIN_HAND) 
                ? net.minecraft.world.InteractionHand.MAIN_HAND 
                : net.minecraft.world.InteractionHand.OFF_HAND;

            net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(
                hitResult.blockPos().x(), 
                hitResult.blockPos().y(), 
                hitResult.blockPos().z()
            );

            net.minecraft.world.phys.BlockHitResult mcHit = new net.minecraft.world.phys.BlockHitResult(
                new Vec3(hitResult.pos().x(), hitResult.pos().y(), hitResult.pos().z()),
                Direction.values()[hitResult.direction().ordinal()],
                mcPos,
                hitResult.insideBlock()
            );

            // In 1.21, useItemOn returns an InteractionResult which should be checked for success/consumption.
            InteractionResult result = mc.gameMode.useItemOn(mc.player, mcHand, mcHit);
            
            if (result.consumesAction()) {
                mc.player.swing(mcHand);
            }
        }
    }

    @Override
    public void useItem(Object client, InteractionHand hand) {
        Minecraft mc = (Minecraft) client;
        if (mc.player != null && mc.gameMode != null) {
            net.minecraft.world.InteractionHand nativeHand = hand == InteractionHand.MAIN_HAND 
                ? net.minecraft.world.InteractionHand.MAIN_HAND : net.minecraft.world.InteractionHand.OFF_HAND;
            mc.gameMode.useItem(mc.player, nativeHand);
            mc.player.swing(nativeHand);
        }
    }

    @Override
    public void interactEntity(Object client, Object entity, boolean sneak) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.player != null && entity instanceof net.minecraft.world.entity.Entity mcEntity) {
            // Respecting 1.21 interaction logic
            if (sneak) {
                // Handle sneak-interact if necessary, though mc.gameMode.interact usually handles player state
            }
            mc.gameMode.interact(mc.player, mcEntity, net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public void attackEntity(Object client, Object entity) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.player != null && entity instanceof net.minecraft.world.entity.Entity mcEntity) {
            mc.gameMode.attack(mc.player, mcEntity);
            mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }
}
