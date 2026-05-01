package is.pig.minecraft.api.spi;

import java.util.List;

/**
 * Platform-agnostic queries for open screens and their contents.
 */
public interface ScreenAdapter {

    /**
     * Returns true if a container-based screen is currently open.
     */
    boolean isContainerScreenOpen(Object client);

    /**
     * Returns the list of slot indices that belong to the player's inventory in the current screen.
     */
    List<Integer> getPlayerSlotIndices(Object client);

    /**
     * Returns the list of slot indices that belong to the storage/container in the current screen.
     */
    List<Integer> getStorageSlotIndices(Object client);

    /**
     * Returns the item stack in a specific slot index of the current screen.
     */
    Object getStackInSlot(Object client, int slotIndex);

    /**
     * Returns the container ID of the currently open screen.
     */
    int getContainerId(Object client);

    /**
     * Checks if a slot index belongs to the player inventory part of a container.
     */
    boolean isPlayerInventorySlot(Object client, int slotIndex);

    /**
     * Checks if the given screen object is a container screen.
     */
    boolean isContainerScreen(Object screen);

    /**
     * Checks if the given screen object is the main player inventory screen.
     */
    boolean isInventoryScreen(Object screen);

    void initScreen(Object screen, Object client, int width, int height);

    int getSlotX(Object client, int slotIndex);
    int getSlotY(Object client, int slotIndex);

    /**
     * Checks if a slot object belongs to the player's inventory.
     */
    boolean isPlayerInventorySlot(Object slot);

    /**
     * Returns the index of a slot object.
     */
    int getSlotIndex(Object slot);

    /**
     * Opens a radial menu.
     */
    <T extends is.pig.minecraft.api.RadialMenuItem> void openRadialMenu(
            Object client,
            String title,
            T centerItem,
            List<T> radialItems,
            T currentSelection,
            java.util.function.Consumer<T> onSelectionChanged,
            Runnable onCloseCallback
    );
}
