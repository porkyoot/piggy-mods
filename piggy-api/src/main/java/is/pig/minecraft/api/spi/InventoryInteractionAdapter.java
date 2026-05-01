package is.pig.minecraft.api.spi;

import is.pig.minecraft.api.ClickType;

/**
 * Platform-agnostic orchestration for inventory and container interactions.
 */
public interface InventoryInteractionAdapter {

    /**
     * Clicks a specific slot in a container.
     * 
     * @param client The client object
     * @param containerId The ID of the container being interacted with
     * @param slotIndex The index of the slot
     * @param button The mouse button (0 for left, 1 for right)
     * @param clickType The type of click (e.g., "PICKUP", "QUICK_MOVE")
     */
    void clickSlot(Object client, int containerId, int slotIndex, int button, ClickType clickType);

    /**
     * Closes the currently open screen.
     */
    void closeScreen(Object client);

    /**
     * Swaps an item into a specific hotbar slot.
     */
    void swapToSlot(Object client, int slotIndex);

    /**
     * Returns the container ID of the currently open screen.
     */
    int getCurrentContainerId(Object client);

    /**
     * Drops an item from a slot.
     */
    void dropItem(Object client, int slotIndex, boolean entireStack);
}
