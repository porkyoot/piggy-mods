package is.pig.minecraft.modern.adapter;

import is.pig.minecraft.api.spi.MessagingAdapter;

/**
 * Modern 26.X implementation of MessagingAdapter using deobfuscated Mojang names.
 */
public class DeobfMessagingAdapter implements MessagingAdapter {

    @Override
    public void sendMessage(Object player, String message, boolean overlay) {
        if (player instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
            // Unobfuscated name for 26.X
            localPlayer.sendSystemMessage(net.minecraft.network.chat.Component.literal(message), overlay);
        }
    }

    @Override
    public void sendClickableMessage(Object player, String message, String clickAction, String clickValue) {
        // ... Implementation for 26.X ...
    }

    @Override
    public void logToConsole(String message) {
        System.out.println("[Piggy Modern] " + message);
    }
}
