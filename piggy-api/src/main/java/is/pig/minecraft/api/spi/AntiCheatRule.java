package is.pig.minecraft.api.spi;

import is.pig.minecraft.api.ActionContext;
import java.util.UUID;

/**
 * Service Provider Interface for anti-cheat rules.
 * Decoupled from Minecraft internals.
 */
@FunctionalInterface
public interface AntiCheatRule {
    /**
     * Evaluates an action against this rule.
     * 
     * @param playerUuid The UUID of the player performing the action.
     * @param context    The context of the action.
     * @return True if the action is flagged as suspicious.
     */
    boolean evaluate(UUID playerUuid, ActionContext context);
}
