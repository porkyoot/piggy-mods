package is.pig.minecraft.api.registry;

import is.pig.minecraft.api.spi.FeatureProvider;
import is.pig.minecraft.api.spi.RenderPipelineAdapter;

import java.util.ServiceLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Central orchestrator that manages the lifecycle of all modular features.
 * Loads FeatureProviders via ServiceLoader.
 */
public class FeatureOrchestrator {
    private static final FeatureOrchestrator INSTANCE = new FeatureOrchestrator();
    private final List<FeatureProvider> providers = new ArrayList<>();

    private FeatureOrchestrator() {
        ServiceLoader.load(FeatureProvider.class).forEach(providers::add);
    }

    public static FeatureOrchestrator getInstance() {
        return INSTANCE;
    }

    public void initialize() {
        providers.forEach(FeatureProvider::onInitialize);
    }

    public void tick(Object client) {
        providers.forEach(p -> p.onTick(client));
    }

    public void render(Object client, Object stack, float partialTicks) {
        providers.forEach(p -> p.onRender(client, stack, partialTicks));
        
        // Also delegate specialized rendering to the pipeline if needed
        RenderPipelineAdapter renderer = PiggyServiceRegistry.getRenderPipelineAdapters().stream().findFirst().orElse(null);
        if (renderer != null) {
            renderer.renderLightLevel(client, stack, partialTicks);
            renderer.renderBuildSession(client, stack, partialTicks);
            renderer.renderPlacementSession(client, stack, partialTicks);
        }
    }

    public List<FeatureProvider> getProviders() {
        return providers;
    }
}
