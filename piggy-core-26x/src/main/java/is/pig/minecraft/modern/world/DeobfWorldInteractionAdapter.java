package is.pig.minecraft.modern.world;

import is.pig.minecraft.api.BlockPos;
import is.pig.minecraft.api.BlockHitResult;
import is.pig.minecraft.api.InteractionHand;
import is.pig.minecraft.api.spi.WorldInteractionAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

/**
 * Modern, fully deobfuscated implementation of WorldInteractionAdapter for Minecraft 26.X.
 * Uses raw Mojang source names for all interaction logic.
 */
public class DeobfWorldInteractionAdapter implements WorldInteractionAdapter {

    @Override
    public void breakBlock(Object client, BlockPos pos) {
        if (client instanceof Minecraft mc && mc.gameMode != null) {
            net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
            // In 26.X, destroyBlock is the deobfuscated name for breaking blocks
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

            // Modern 26.X interaction logic: directly calling the deobfuscated useItemOn
            InteractionResult result = mc.gameMode.useItemOn(mc.player, mcHand, mcHit);
            
            if (result.consumesAction()) {
                mc.player.swing(mcHand);
            }
        }
    }

    @Override
    public void interactEntity(Object client, Object entity, boolean sneak) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.player != null && entity instanceof net.minecraft.world.entity.Entity mcEntity) {
            // Unobfuscated entity interaction logic
            mc.gameMode.interact(mc.player, mcEntity, net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public void useItem(Object client, InteractionHand hand) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.player != null) {
            net.minecraft.world.InteractionHand mcHand = (hand == InteractionHand.MAIN_HAND) 
                ? net.minecraft.world.InteractionHand.MAIN_HAND 
                : net.minecraft.world.InteractionHand.OFF_HAND;
            
            mc.gameMode.useItem(mc.player, mcHand);
        }
    }
}
