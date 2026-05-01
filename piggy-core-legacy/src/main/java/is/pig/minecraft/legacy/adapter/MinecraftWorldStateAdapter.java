package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.BlockPos;
import is.pig.minecraft.api.spi.WorldStateAdapter;
import is.pig.minecraft.admin.anticheat.OreCacheManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import is.pig.minecraft.admin.PiggyAdmin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bridge between Piggy API and Minecraft world state (Server and Client).
 */
public class MinecraftWorldStateAdapter implements WorldStateAdapter {

    @Override
    public void openScreen(Object client, Object screen) {
        if (client instanceof net.minecraft.client.Minecraft mc && screen instanceof net.minecraft.client.gui.screens.Screen s) {
            mc.setScreen(s);
        }
    }

    @Override
    public is.pig.minecraft.api.HitResult getCrosshairTarget(Object client) {
        if (client instanceof net.minecraft.client.Minecraft mc) {
            var hit = mc.hitResult;
            if (hit instanceof net.minecraft.world.phys.BlockHitResult bhr) {
                return new is.pig.minecraft.api.BlockHitResult(
                    is.pig.minecraft.lib.util.TypeConverter.fromMinecraft(bhr.getLocation()),
                    is.pig.minecraft.lib.util.TypeConverter.fromMinecraft(bhr.getDirection()),
                    is.pig.minecraft.lib.util.TypeConverter.fromMinecraft(bhr.getBlockPos()),
                    bhr.isInside()
                );
            }
        }
        return null;
    }

    @Override
    public double getPlayerReachDistance(Object client) {
        if (client instanceof net.minecraft.client.Minecraft mc && mc.player != null) {
            return mc.player.blockInteractionRange();
        }
        return 4.5;
    }

    @Override
    public boolean isReplaceable(String worldId, BlockPos pos) {
        Level level = getLevel(worldId);
        if (level != null) {
            return level.getBlockState(new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z())).canBeReplaced();
        }
        return false;
    }

    @Override
    public boolean isExposed(String worldId, BlockPos pos) {
        Level level = getLevel(worldId);
        if (level == null) return false;

        net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
            BlockState neighborState = level.getBlockState(mcPos.relative(dir));
            if (neighborState.isAir() || neighborState.is(Blocks.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getBlockStateId(String worldId, is.pig.minecraft.api.BlockPos pos) {
        Level level = getLevel(worldId);
        if (level == null) return "minecraft:air";
        net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
        return level.getBlockState(mcPos).toString();
    }

    @Override
    public boolean isEmpty(String worldId, BlockPos pos) {
        Level level = getLevel(worldId);
        if (level == null) return true;
        net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
        return level.isEmptyBlock(mcPos);
    }

    @Override
    public boolean isEntityIntersecting(Object entity, BlockPos pos) {
        if (entity instanceof net.minecraft.world.entity.Entity mcEntity) {
            net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
            net.minecraft.world.phys.AABB extendedBlockBox = new net.minecraft.world.phys.AABB(mcPos).expandTowards(0, 0.1, 0);
            return mcEntity.getBoundingBox().intersects(extendedBlockBox);
        }
        return false;
    }

    @Override
    public boolean isPlayerOnGround(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.onGround();
        }
        return false;
    }

    @Override
    public boolean isPlayerSprinting(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.isSprinting();
        }
        return false;
    }

    @Override
    public Vec3 getPlayerDeltaMovement(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            net.minecraft.world.phys.Vec3 vel = p.getDeltaMovement();
            return new Vec3(vel.x, vel.y, vel.z);
        }
        return new Vec3(0, 0, 0);
    }

    @Override
    public Vec3 getPlayerPosition(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            net.minecraft.world.phys.Vec3 pos = p.position();
            return new Vec3(pos.x, pos.y, pos.z);
        }
        return new Vec3(0, 0, 0);
    }

    @Override
    public BlockPos getPlayerBlockPos(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            net.minecraft.core.BlockPos pos = p.blockPosition();
            return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        }
        return new BlockPos(0, 0, 0);
    }

    @Override
    public Object getPlayerInventory(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.getInventory();
        }
        return null;
    }

    @Override
    public Object getPlayerMainHandItem(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.getMainHandItem();
        }
        return null;
    }

    @Override
    public Object getPlayerOffhandItem(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.getOffhandItem();
        }
        return null;
    }

    @Override
    public boolean isPlayerDeadOrDying(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.isDeadOrDying();
        }
        return false;
    }

    @Override
    public boolean isBlockReplaceable(String worldId, BlockPos pos) {
        Level level = getLevel(worldId);
        if (level != null) {
            return level.getBlockState(new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z())).canBeReplaced();
        }
        return false;
    }

    @Override
    public String getCurrentWorldId() {
        var client = net.minecraft.client.Minecraft.getInstance();
        if (client.level != null) {
            return client.level.dimension().location().toString();
        }
        return "minecraft:overworld";
    }

    @Override
    public void sendMessage(Object player, String message) {
        sendMessage(player, message, false);
    }

    @Override
    public void setInteractionDelay(Object client, int delay) {
        if (client instanceof net.minecraft.client.Minecraft mc) {
            ((is.pig.minecraft.build.mixin.client.MinecraftAccessorMixin) mc).setRightClickDelay(delay);
        }
    }

    @Override
    public void sendMessage(Object player, String message, boolean overlay) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            if (p instanceof net.minecraft.client.player.LocalPlayer lp) {
                lp.displayClientMessage(net.minecraft.network.chat.Component.literal(message), overlay);
            } else {
                p.sendSystemMessage(net.minecraft.network.chat.Component.literal(message));
            }
        }
    }

    @Override
    public is.pig.minecraft.api.Vec3 getPlayerVelocity(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return is.pig.minecraft.lib.util.TypeConverter.fromMinecraft(p.getDeltaMovement());
        }
        return new is.pig.minecraft.api.Vec3(0, 0, 0);
    }

    @Override
    public int getPlayerLatency(Object client) {
        if (client instanceof net.minecraft.client.Minecraft mc && mc.getConnection() != null && mc.player != null) {
            var entry = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            return entry != null ? entry.getLatency() : 0;
        }
        return 0;
    }

    @Override
    public boolean isWorldUltraWarm() {
        var client = net.minecraft.client.Minecraft.getInstance();
        if (client.level != null) {
            return client.level.dimensionType().ultraWarm();
        }
        return false;
    }

    @Override
    public void setPlayerPosition(Object player, Vec3 pos) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            p.setPos(pos.x(), pos.y(), pos.z());
            if (p instanceof net.minecraft.client.player.LocalPlayer lp && lp.connection != null) {
                lp.connection.send(new net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos(
                        pos.x(), pos.y(), pos.z(), p.onGround()));
            }
        }
    }

    @Override
    public double getPlayerEyeHeight(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.getEyeHeight();
        }
        return 1.62;
    }

    @Override
    public boolean isCreative(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.isCreative();
        }
        return false;
    }

    @Override
    public boolean isOnGround(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.onGround();
        }
        return false;
    }

    @Override
    public boolean isFalling(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.getDeltaMovement().y < 0 && !p.isFallFlying();
        }
        return false;
    }

    @Override
    public int getPing() {
        var client = net.minecraft.client.Minecraft.getInstance();
        if (client.player != null && client.getConnection() != null) {
            var entry = client.getConnection().getPlayerInfo(client.player.getUUID());
            return entry != null ? entry.getLatency() : 0;
        }
        return 0;
    }

    @Override
    public Vec3 getPlayerEyePosition(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            net.minecraft.world.phys.Vec3 pos = p.getEyePosition();
            return new Vec3(pos.x, pos.y, pos.z);
        }
        return new Vec3(0, 0, 0);
    }

    @Override
    public List<BlockPos> getOresInRadius(String worldId, BlockPos pos, int radius) {
        Level level = getLevel(worldId);
        if (level == null) return List.of();

        net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
        // OreCacheManager might only be server-side, but we can query directly if needed
        return OreCacheManager.INSTANCE.getOresInRadius(level, mcPos, radius).stream()
                .map(p -> new BlockPos(p.getX(), p.getY(), p.getZ()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isType(String stateId, String type) {
        return stateId.contains(type);
    }

    @Override
    public boolean isContainer(String worldId, BlockPos pos) {
        Level level = getLevel(worldId);
        if (level == null) return false;
        net.minecraft.core.BlockPos mcPos = new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z());
        
        BlockState state = level.getBlockState(mcPos);
        if (state.getMenuProvider(level, mcPos) != null) return true;

        net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(mcPos);
        if (be instanceof net.minecraft.world.level.block.entity.EnderChestBlockEntity) return true;

        // Mod compatibility (Sophisticated Storage, etc.)
        if (be != null) {
            String className = be.getClass().getName().toLowerCase();
            if (className.contains("sophisticatedstorage") || className.contains("sophisticatedbackpacks")) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean isContainer(Object entity) {
        if (entity instanceof net.minecraft.world.MenuProvider || entity instanceof net.minecraft.world.entity.vehicle.ContainerEntity) {
            return true;
        }
        return false;
    }

    @Override
    public void swingHand(Object client, InteractionHand hand) {
        if (client instanceof net.minecraft.client.Minecraft mc && mc.player != null) {
            mc.player.swing(hand == InteractionHand.MAIN_HAND ? net.minecraft.world.InteractionHand.MAIN_HAND : net.minecraft.world.InteractionHand.OFF_HAND);
        }
    }

    @Override
    public Object getClient() {
        return net.minecraft.client.Minecraft.getInstance();
    }
    
    @Override
    public int getSelectedSlot(Object player) {
        if (player instanceof net.minecraft.world.entity.player.Player p) {
            return p.getInventory().selected;
        }
        return -1;
    }

    @Override
    public boolean isAlive(Object entity) {
        if (entity instanceof net.minecraft.world.entity.Entity mcEntity) {
            return mcEntity.isAlive();
        }
        return false;
    }

    @Override
    public Object getBlockState(String worldId, BlockPos pos) {
        Level level = getLevel(worldId);
        if (level != null) {
            return level.getBlockState(new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z()));
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public float getDestroySpeed(Object blockState, String worldId, BlockPos pos) {
        if (blockState instanceof BlockState state) {
            Level level = getLevel(worldId);
            if (level != null) {
                return state.getDestroySpeed(level, new net.minecraft.core.BlockPos(pos.x(), pos.y(), pos.z()));
            }
        }
        return 0.0f;
    }

    @Override
    public String getBlockId(Object blockState) {
        if (blockState instanceof BlockState state) {
            return BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        }
        return "minecraft:air";
    }

    private Level getLevel(String worldId) {
        // Try client first
        try {
            if (Minecraft.getInstance().level != null) {
                return Minecraft.getInstance().level;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError ignored) {
            // Not on client
        }

        MinecraftServer server = PiggyAdmin.getServer();
        if (server == null) return null;
        
        ResourceLocation rl = ResourceLocation.parse(worldId);
        return server.getLevel(ResourceKey.create(Registries.DIMENSION, rl));
    }
}
