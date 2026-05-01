package is.pig.minecraft.legacy;

import is.pig.minecraft.api.registry.PiggyServiceRegistry;
import is.pig.minecraft.api.spi.FeatureProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class LegacyModInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Initialize all feature providers
        for (FeatureProvider provider : PiggyServiceRegistry.getFeatureProviders()) {
            provider.onClientInitialize();
        }

        // Register global tick
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            for (FeatureProvider provider : PiggyServiceRegistry.getFeatureProviders()) {
                provider.onTick(client);
            }
        });
    }
}
