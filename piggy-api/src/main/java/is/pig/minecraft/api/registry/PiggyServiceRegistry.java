package is.pig.minecraft.api.registry;

import is.pig.minecraft.api.spi.FeatureProvider;
import is.pig.minecraft.api.spi.ItemDataAdapter;
import is.pig.minecraft.api.spi.PhysicsAdapter;
import is.pig.minecraft.api.spi.RenderPipelineAdapter;
import is.pig.minecraft.api.spi.WorldStateAdapter;
import is.pig.minecraft.api.spi.WorldInteractionAdapter;
import is.pig.minecraft.api.spi.InputAdapter;
import is.pig.minecraft.api.spi.InventoryInteractionAdapter;
import is.pig.minecraft.api.spi.ScreenAdapter;
import is.pig.minecraft.api.spi.MessagingAdapter;
import is.pig.minecraft.api.spi.ModerationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Centralized registry for discovering and caching Piggy Mod Service Providers.
 * Uses {@link java.util.ServiceLoader} for dynamic discovery.
 */
public final class PiggyServiceRegistry {
    private static final ReentrantLock LOCK = new ReentrantLock();
    
    private static List<FeatureProvider> featureProviders;
    private static ItemDataAdapter itemDataAdapter;
    private static RenderPipelineAdapter renderPipelineAdapter;
    private static List<RenderPipelineAdapter> renderPipelineAdapters;
    private static WorldStateAdapter worldStateAdapter;
    private static List<WorldInteractionAdapter> worldInteractionAdapters;
    private static PhysicsAdapter physicsAdapter;
    private static InputAdapter inputAdapter;
    private static InventoryInteractionAdapter inventoryInteractionAdapter;
    private static ScreenAdapter screenAdapter;
    private static MessagingAdapter messagingAdapter;
    private static ModerationAdapter moderationAdapter;

    private PiggyServiceRegistry() {
        // Prevent instantiation
    }

    /**
     * Returns all discovered feature providers.
     */
    public static List<FeatureProvider> getFeatureProviders() {
        if (featureProviders == null) {
            LOCK.lock();
            try {
                if (featureProviders == null) {
                    List<FeatureProvider> discovered = new ArrayList<>();
                    ServiceLoader.load(FeatureProvider.class).forEach(discovered::add);
                    featureProviders = Collections.unmodifiableList(discovered);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return featureProviders;
    }

    /**
     * Returns the primary item data adapter.
     * @throws IllegalStateException if no adapter is found.
     */
    public static ItemDataAdapter getItemDataAdapter() {
        if (itemDataAdapter == null) {
            LOCK.lock();
            try {
                if (itemDataAdapter == null) {
                    itemDataAdapter = ServiceLoader.load(ItemDataAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory ItemDataAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return itemDataAdapter;
    }

    /**
     * Returns the primary render pipeline adapter.
     * @throws IllegalStateException if no adapter is found.
     */
    public static RenderPipelineAdapter getRenderPipelineAdapter() {
        return getRenderPipelineAdapters().get(0);
    }

    /**
     * Returns all discovered render pipeline adapters.
     */
    public static List<RenderPipelineAdapter> getRenderPipelineAdapters() {
        if (renderPipelineAdapters == null) {
            LOCK.lock();
            try {
                if (renderPipelineAdapters == null) {
                    List<RenderPipelineAdapter> discovered = new ArrayList<>();
                    ServiceLoader.load(RenderPipelineAdapter.class).forEach(discovered::add);
                    if (discovered.isEmpty()) {
                        throw new IllegalStateException("Mandatory RenderPipelineAdapter implementation missing!");
                    }
                    renderPipelineAdapters = Collections.unmodifiableList(discovered);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return renderPipelineAdapters;
    }

    /**
     * Returns the primary world state adapter.
     */
    public static WorldStateAdapter getWorldStateAdapter() {
        if (worldStateAdapter == null) {
            LOCK.lock();
            try {
                if (worldStateAdapter == null) {
                    worldStateAdapter = ServiceLoader.load(WorldStateAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory WorldStateAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return worldStateAdapter;
    }

    /**
     * Returns all discovered world interaction adapters.
     */
    public static List<WorldInteractionAdapter> getWorldInteractionAdapters() {
        if (worldInteractionAdapters == null) {
            LOCK.lock();
            try {
                if (worldInteractionAdapters == null) {
                    List<WorldInteractionAdapter> discovered = new ArrayList<>();
                    ServiceLoader.load(WorldInteractionAdapter.class).forEach(discovered::add);
                    worldInteractionAdapters = Collections.unmodifiableList(discovered);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return worldInteractionAdapters;
    }

    /**
     * Returns the primary physics adapter.
     */
    public static PhysicsAdapter getPhysicsAdapter() {
        if (physicsAdapter == null) {
            LOCK.lock();
            try {
                if (physicsAdapter == null) {
                    physicsAdapter = ServiceLoader.load(PhysicsAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory PhysicsAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return physicsAdapter;
    }

    /**
     * Returns the primary input adapter.
     */
    public static InputAdapter getInputAdapter() {
        if (inputAdapter == null) {
            LOCK.lock();
            try {
                if (inputAdapter == null) {
                    inputAdapter = ServiceLoader.load(InputAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory InputAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return inputAdapter;
    }

    /**
     * Returns the primary inventory interaction adapter.
     */
    public static InventoryInteractionAdapter getInventoryInteractionAdapter() {
        if (inventoryInteractionAdapter == null) {
            LOCK.lock();
            try {
                if (inventoryInteractionAdapter == null) {
                    inventoryInteractionAdapter = ServiceLoader.load(InventoryInteractionAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory InventoryInteractionAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return inventoryInteractionAdapter;
    }

    /**
     * Returns the primary screen adapter.
     */
    public static ScreenAdapter getScreenAdapter() {
        if (screenAdapter == null) {
            LOCK.lock();
            try {
                if (screenAdapter == null) {
                    screenAdapter = ServiceLoader.load(ScreenAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory ScreenAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return screenAdapter;
    }

    /**
     * Returns the primary messaging adapter.
     */
    public static MessagingAdapter getMessagingAdapter() {
        if (messagingAdapter == null) {
            LOCK.lock();
            try {
                if (messagingAdapter == null) {
                    messagingAdapter = ServiceLoader.load(MessagingAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory MessagingAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return messagingAdapter;
    }

    /**
     * Returns the primary moderation adapter.
     */
    public static ModerationAdapter getModerationAdapter() {
        if (moderationAdapter == null) {
            LOCK.lock();
            try {
                if (moderationAdapter == null) {
                    moderationAdapter = ServiceLoader.load(ModerationAdapter.class)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Mandatory ModerationAdapter implementation missing!"));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return moderationAdapter;
    }
}
