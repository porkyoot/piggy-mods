package is.pig.minecraft.api.spi;

/**
 * Service Provider Interface for sending messages to players and the server.
 */
public interface MessagingAdapter {
    /**
     * Sends a chat message to a specific player (or the local player).
     */
    void sendMessage(Object player, String message, boolean overlay);

    /**
     * Sends a formatted message with a click event or hover event (abstracted).
     */
    void sendClickableMessage(Object player, String message, String clickAction, String clickValue);

    /**
     * Logs a message to the server console.
     */
    void logToConsole(String message);
    /**
     * Sends a complex message with multiple parts and colors.
     */
    void sendFormattedMessage(Object player, boolean overlay, MessagePart... parts);

    record MessagePart(String text, String color, boolean bold, boolean italic) {
        public static MessagePart of(String text, String color) {
            return new MessagePart(text, color, false, false);
        }
        public static MessagePart bold(String text, String color) {
            return new MessagePart(text, color, true, false);
        }
    }
}
