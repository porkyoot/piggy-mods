package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.FallPredictionResult;
import is.pig.minecraft.api.spi.PhysicsAdapter;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;

import java.util.Optional;

public class MinecraftPhysicsAdapter implements PhysicsAdapter {

    @Override
    public Optional<FallPredictionResult> simulateFall(Object playerObj) {
        if (!(playerObj instanceof LocalPlayer player)) return Optional.empty();
        ClientLevel world = (ClientLevel) player.level();

        if (player.onClimbable() || player.isInWater() || player.isInLava()) {
            return Optional.empty();
        }

        Vec3 currentPos = player.position();
        Vec3 currentVel = player.getDeltaMovement();
        AABB currentBox = player.getBoundingBox();
        float simulatedFallDistance = player.fallDistance;

        for (int tick = 1; tick <= 100; tick++) {
            Iterable<VoxelShape> collisions = world.getBlockCollisions(player, currentBox.expandTowards(0, currentVel.y, 0));

            if (collisions.iterator().hasNext()) {
                double highestHitY = currentPos.y + currentVel.y;
                
                Vec3[] corners = {
                        new Vec3(currentBox.minX, currentBox.minY, currentBox.minZ),
                        new Vec3(currentBox.maxX, currentBox.minY, currentBox.minZ),
                        new Vec3(currentBox.minX, currentBox.minY, currentBox.maxZ),
                        new Vec3(currentBox.maxX, currentBox.minY, currentBox.maxZ),
                        new Vec3(currentPos.x, currentBox.minY, currentPos.z)
                };
                
                for (Vec3 corner : corners) {
                    net.minecraft.world.phys.HitResult hit = world.clip(new net.minecraft.world.level.ClipContext(
                            corner,
                            corner.add(currentVel.x, currentVel.y - 1.0, currentVel.z),
                            net.minecraft.world.level.ClipContext.Block.COLLIDER,
                            net.minecraft.world.level.ClipContext.Fluid.NONE,
                            player
                    ));
                    if (hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                        highestHitY = Math.max(highestHitY, hit.getLocation().y);
                    }
                }

                Vec3 hitVec = new Vec3(currentPos.x, highestHitY, currentPos.z);
                
                BlockPos impactPos = BlockPos.containing(currentPos.x, highestHitY - 0.001, currentPos.z);
                BlockPos insidePos = BlockPos.containing(currentPos.x, highestHitY + 0.001, currentPos.z);
                
                float expectedDamage = calculateFallDamage(player, simulatedFallDistance, impactPos, insidePos, world);
                boolean isFatal = expectedDamage >= (player.getHealth() + player.getAbsorptionAmount()) || expectedDamage >= 10.0f;

                return Optional.of(new FallPredictionResult(
                        new is.pig.minecraft.api.BlockPos(insidePos.getX(), insidePos.getY(), insidePos.getZ()),
                        new is.pig.minecraft.api.Vec3(hitVec.x, hitVec.y, hitVec.z),
                        tick,
                        simulatedFallDistance,
                        expectedDamage,
                        isFatal
                ));
            }

            simulatedFallDistance -= (float) currentVel.y;
            currentBox = currentBox.move(currentVel);
            currentPos = currentPos.add(currentVel);

            currentVel = new Vec3(currentVel.x, (currentVel.y - 0.08D) * 0.98D, currentVel.z);
        }

        return Optional.empty();
    }

    private float calculateFallDamage(LocalPlayer player, float fallDistance, BlockPos impactPos, BlockPos insidePos, ClientLevel world) {
        net.minecraft.world.level.block.state.BlockState impactState = world.getBlockState(impactPos);
        net.minecraft.world.level.block.state.BlockState insideState = world.getBlockState(insidePos);

        if (impactState.getBlock() instanceof CarpetBlock) {
            impactState = world.getBlockState(impactPos.below());
        }
        
        if (insideState.getFluidState().is(FluidTags.WATER) || impactState.getFluidState().is(FluidTags.WATER)) return 0.0f;
        if (insideState.getFluidState().is(FluidTags.LAVA) || impactState.getFluidState().is(FluidTags.LAVA)) return 0.0f;
        if (insideState.is(Blocks.POWDER_SNOW) || impactState.is(Blocks.POWDER_SNOW)) return 0.0f;
        if (insideState.is(Blocks.COBWEB) || impactState.is(Blocks.COBWEB)) return 0.0f;
        if (impactState.is(Blocks.SLIME_BLOCK) && !player.isSuppressingBounce()) return 0.0f;
        if (insideState.is(BlockTags.CLIMBABLE) || impactState.is(BlockTags.CLIMBABLE)) return 0.0f;
        if (insideState.is(Blocks.SWEET_BERRY_BUSH) || impactState.is(Blocks.SWEET_BERRY_BUSH)) return 0.0f;

        float damageMultiplier = 1.0f;
        if (impactState.is(Blocks.HAY_BLOCK)) damageMultiplier = 0.2f;
        else if (impactState.is(Blocks.HONEY_BLOCK)) damageMultiplier = 0.2f;
        else if (impactState.is(BlockTags.BEDS)) damageMultiplier = 0.5f;

        float damage = Math.max(0, fallDistance - 3.0f);
        if (damage <= 0) return 0.0f;
        damage *= damageMultiplier;

        return applyDamageResistances(player, damage);
    }

    private float applyDamageResistances(LocalPlayer player, float rawDamage) {
        if (rawDamage <= 0) return 0.0f;
        float damage = rawDamage;

        if (player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
            int amplifier = player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier();
            damage -= damage * ((amplifier + 1) * 0.2f);
        }
        if (damage <= 0) return 0.0f;

        int totalEPF = 0;
        try {
            var registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            var featherFalling = registry.getOrThrow(Enchantments.FEATHER_FALLING);
            var protection = registry.getOrThrow(Enchantments.PROTECTION);

            for (ItemStack armor : player.getArmorSlots()) {
                if (!armor.isEmpty()) {
                    totalEPF += EnchantmentHelper.getItemEnchantmentLevel(protection, armor);
                    if (armor == player.getItemBySlot(EquipmentSlot.FEET)) {
                        totalEPF += EnchantmentHelper.getItemEnchantmentLevel(featherFalling, armor) * 3;
                    }
                }
            }
            totalEPF = Math.min(20, totalEPF);
            if (totalEPF > 0) {
                float reduction = totalEPF * 0.04f;
                damage = damage * (1.0f - reduction);
            }
        } catch (Exception ignored) {}
        return damage;
    }
}
