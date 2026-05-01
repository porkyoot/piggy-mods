package is.pig.minecraft.legacy;

import is.pig.minecraft.api.registry.FeatureOrchestrator;
import is.pig.minecraft.api.registry.PiggyServiceRegistry;
import is.pig.minecraft.api.spi.FeatureProvider;
import is.pig.minecraft.lib.action.PiggyActionQueue;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entrypoint for the Piggy Core Legacy module (Minecraft 1.21.X).
 * Responsible for initializing all decoupled features.
 */
public class PiggyLegacyMod implements ModInitializer, ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("piggy-core-legacy");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Piggy Core Legacy (1.21.X)...");

        // Agnostic Lib Init
        is.pig.minecraft.lib.PiggyLibClient.initAgnostic();

        // Admin Init
        is.pig.minecraft.admin.config.PiggyServerConfig.load();
        is.pig.minecraft.admin.storage.HistoryManager.init();
        
        // Register Commands (1.21.1)
        net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            is.pig.minecraft.admin.command.PiggyAdminCommand.register(dispatcher);
            is.pig.minecraft.admin.command.PiggyLogCommand.register(dispatcher);
        });

        // Register Network Receivers (1.21.1)
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playS2C().register(
                is.pig.minecraft.lib.network.SyncConfigPayload.TYPE,
                is.pig.minecraft.lib.network.SyncConfigPayload.CODEC);
        is.pig.minecraft.admin.network.SyncModerationPayload.register();
        is.pig.minecraft.admin.network.UpdateAdminConfigPayload.register();

        // Server Events (1.21.1)
        net.fabricmc.fabric.api.message.v1.ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            if (sender == null || !is.pig.minecraft.admin.config.PiggyServerConfig.getInstance().moderationEnabled) {
                return true;
            }
            if (is.pig.minecraft.admin.moderation.ModerationEngine.getInstance().isModerated(message)) {
                return true;
            }
            is.pig.minecraft.admin.moderation.ModerationEngine.getInstance().processMessage(sender, message, params);
            return false;
        });

        // ... Rest of events ...

        // Delegate to the central orchestrator to initialize all discovered features
        FeatureOrchestrator.getInstance().initialize();

        // Register Server-Side Anti-Cheat events (1.21.1 specific)
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents.CHUNK_LOAD.register((level, chunk) -> {
            is.pig.minecraft.admin.anticheat.OreCacheManager.INSTANCE.scanAndCacheChunk(
                level.dimension().location().toString(),
                new is.pig.minecraft.api.ChunkPos(chunk.getPos().x, chunk.getPos().z)
            );
        });

        net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            is.pig.minecraft.admin.anticheat.OreCacheManager.INSTANCE.removeCachedOre(
                new is.pig.minecraft.api.BlockPos(pos.getX(), pos.getY(), pos.getZ())
            );
        });
        
        LOGGER.info("Piggy Core Legacy initialization complete.");
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Piggy Core Legacy Client...");

        // Bootstrapping the agnostic lib parts
        is.pig.minecraft.lib.PiggyLibClient.initAgnostic();

        // Register Action Queue processing (1.21.1 specific registration)
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            PiggyActionQueue.getInstance().tick(client);
            is.pig.minecraft.inventory.handler.AutoRefillHandler.getInstance().onTick(client);
            FeatureOrchestrator.getInstance().tick(client);
        });

        // Register World Rendering (1.21.1 specific)
        WorldRenderEvents.LAST.register(context -> {
            FeatureOrchestrator.getInstance().render(
                Minecraft.getInstance(), 
                context.matrixStack(), 
                context.tickCounter().getTickDelta(true)
            );
        });

        // Register HUD Rendering (1.21.1 specific)
        net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            is.pig.minecraft.lib.ui.IconQueueOverlay.render(graphics);
        });
    }
}
