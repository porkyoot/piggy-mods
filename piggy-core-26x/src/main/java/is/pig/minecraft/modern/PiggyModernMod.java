package is.pig.minecraft.modern;

import is.pig.minecraft.api.registry.FeatureOrchestrator;
import is.pig.minecraft.api.registry.PiggyServiceRegistry;
import is.pig.minecraft.api.spi.FeatureProvider;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entrypoint for the modern Piggy Core 26.X module.
 * Bootstraps all decoupled features using the deobfuscated SPI implementation.
 */
public class PiggyModernMod implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("piggy-core-26x");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Piggy Modern (26.X) with deobfuscated SPI...");

        // Load all discovered feature providers
        for (FeatureProvider provider : PiggyServiceRegistry.getFeatureProviders()) {
            LOGGER.info("Registered Feature Provider: {}", provider.getClass().getName());
        }

        // Bootstrap the orchestrator to initialize modular logic
        FeatureOrchestrator.getInstance().initialize();

        LOGGER.info("Piggy Modern (26.X) successfully bootstrapped.");
    }
}
