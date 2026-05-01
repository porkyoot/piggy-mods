package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.spi.ModerationAdapter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.ChatType;
import java.util.UUID;

/**
 * Legacy implementation of ModerationAdapter for Minecraft 1.21.X.
 * Handles the complexities of signed chat messages and player list broadcasting.
 */
public class LegacyModerationAdapter implements ModerationAdapter {

    @Override
    public String getMessageContent(Object messageObj) {
        if (messageObj instanceof PlayerChatMessage message) {
            return message.signedContent();
        }
        return "";
    }

    @Override
    public void broadcastMessage(Object playerObj, Object messageObj, Object paramsObj) {
        if (playerObj instanceof ServerPlayer player && 
            messageObj instanceof PlayerChatMessage message && 
            paramsObj instanceof ChatType.Bound params) {
            
            player.server.execute(() -> {
                player.server.getPlayerList().broadcastChatMessage(message, player, params);
            });
        }
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof ServerPlayer p) {
            return p.getName().getString();
        }
        return "Unknown";
    }

    @Override
    public int getServerTickCount(Object player) {
        if (player instanceof ServerPlayer p) {
            return p.getServer().getTickCount();
        }
        return 0;
    }

    @Override
    public UUID getPlayerUUID(Object player) {
        if (player instanceof ServerPlayer p) {
            return p.getUUID();
        }
        return UUID.randomUUID();
    }
}
