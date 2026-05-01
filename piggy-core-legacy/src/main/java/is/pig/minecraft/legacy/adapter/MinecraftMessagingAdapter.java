package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.spi.MessagingAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * 1.21.X implementation of MessagingAdapter.
 */
public class MinecraftMessagingAdapter implements MessagingAdapter {

    @Override
    public void sendMessage(Object player, String message, boolean overlay) {
        Component component = Component.literal(message);
        if (player instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
            localPlayer.displayClientMessage(component, overlay);
        } else if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(component);
        }
    }

    @Override
    public void sendClickableMessage(Object player, String message, String clickAction, String clickValue) {
        Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.valueOf(clickAction), clickValue));
        Component component = Component.literal(message).withStyle(style);
        
        if (player instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
            localPlayer.displayClientMessage(component, false);
        } else if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(component);
        }
    }

    @Override
    public void logToConsole(String message) {
        System.out.println("[Piggy] " + message);
    }

    @Override
    public void sendFormattedMessage(Object player, boolean overlay, MessagePart... parts) {
        net.minecraft.network.chat.MutableComponent finalMessage = net.minecraft.network.chat.Component.empty();
        for (MessagePart part : parts) {
            net.minecraft.network.chat.MutableComponent p = net.minecraft.network.chat.Component.literal(part.text());
            net.minecraft.network.chat.Style style = net.minecraft.network.chat.Style.EMPTY;
            if (part.color() != null) {
                style = style.withColor(net.minecraft.ChatFormatting.getByName(part.color()));
            }
            if (part.bold()) style = style.withBold(true);
            if (part.italic()) style = style.withItalic(true);
            finalMessage.append(p.withStyle(style));
        }

        if (player instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
            localPlayer.displayClientMessage(finalMessage, overlay);
        } else if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(finalMessage);
        }
    }
}
