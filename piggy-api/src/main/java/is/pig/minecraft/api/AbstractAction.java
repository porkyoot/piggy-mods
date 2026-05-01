package is.pig.minecraft.api;

import java.util.Optional;
import java.util.logging.Logger;

public abstract class AbstractAction implements Action {
    private static final Logger LOGGER = Logger.getLogger(AbstractAction.class.getName());

    private boolean initiated = false;
    private int waitTicks = 0;
    private final int timeoutTicks;
    private final String sourceMod;
    private final ActionPriority priority;
    private boolean ignoreGlobalCps = false;
    private Optional<ActionCallback> callback = Optional.empty();

    public AbstractAction(String sourceMod, ActionPriority priority, int timeoutTicks) {
        this.sourceMod = sourceMod;
        this.priority = priority;
        this.timeoutTicks = timeoutTicks;
    }

    public AbstractAction(String sourceMod, ActionPriority priority) {
        this(sourceMod, priority, 40);
    }

    public AbstractAction(String sourceMod) {
        this(sourceMod, ActionPriority.NORMAL, 40);
    }

    protected abstract void onExecute(Object client);

    /**
     * @return Optional.empty() while waiting, Optional.of(true/false) upon definitive result.
     */
    protected abstract Optional<Boolean> verify(Object client);

    @Override
    public Optional<Boolean> execute(Object client) {
        if (!initiated) {
            if (!checkPreconditions(client)) {
                LOGGER.warning("Preconditions failed for action '" + getName() + "' - Aborting");
                return Optional.of(false);
            }
            onExecute(client);
            initiated = true;
            return verify(client); // Check immediately (supports 0-tick actions)
        }

        waitTicks++;
        Optional<Boolean> verificationResult = verify(client);
        
        if (verificationResult.isPresent()) {
            return verificationResult;
        }

        if (waitTicks >= timeoutTicks) {
            LOGGER.warning("Action '" + getName() + "' from '" + getSourceMod() + "' timed out after " + waitTicks + " ticks");
            return Optional.of(false);
        }

        return Optional.empty(); // Keep waiting
    }

    @Override
    public ActionPriority getPriority() { return priority; }
    @Override
    public String getSourceMod() { return sourceMod; }
    @Override
    public boolean isInitiated() { return initiated; }
    public void setIgnoreGlobalCps(boolean ignore) { this.ignoreGlobalCps = ignore; }
    @Override
    public boolean ignoreGlobalCps() { return ignoreGlobalCps; }
    @Override
    public Optional<ActionCallback> getCallback() { return callback; }
    public void setCallback(ActionCallback callback) { this.callback = Optional.ofNullable(callback); }
    @Override
    public String getName() { return getClass().getSimpleName(); }
}
