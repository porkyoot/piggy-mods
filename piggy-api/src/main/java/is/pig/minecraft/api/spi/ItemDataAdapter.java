package is.pig.minecraft.api.spi;

/**
 * Pure Java contract for data extraction and item manipulation.
 * Avoids any direct imports of net.minecraft.world.item.ItemStack.
 */
public interface ItemDataAdapter {
    
    /**
     * Compares two agnostic item containers for type and component equality.
     * 
     * @param stackA Primary stack (mapped as Object)
     * @param stackB Comparison stack (mapped as Object)
     * @return true if the item types and data components match
     */
    boolean areItemsEqual(Object stackA, Object stackB);

    /**
     * Verifies if an item stack belongs to a specific namespace tag.
     * 
     * @param stack Item stack Object
     * @param tagId e.g., "minecraft:coals" or "minecraft:axes"
     */
    boolean hasTag(Object stack, String tagId);

    /**
     * Extracts the raw Registry identifier for logging.
     */
    String getItemId(Object stack);

    /**
     * Safely reads the current count of items.
     */
    int getCount(Object stack);

    /**
     * Safely reads the maximum allowed stack size.
     */
    int getMaxStackSize(Object stack);

    /**
     * Creates a deep copy of the item stack.
     */
    Object copy(Object stack);

    /**
     * Creates a copy of the stack with a different count.
     */
    Object copyWithCount(Object stack, int count);

    /**
     * Increases the stack count.
     */
    void grow(Object stack, int amount);

    /**
     * Decreases the stack count.
     */
    void shrink(Object stack, int amount);

    /**
     * Checks if the stack is of a specific item type.
     * @param itemId e.g., "minecraft:bucket"
     */
    boolean isItem(Object stack, String itemId);

    /**
     * Returns an empty stack object.
     */
    Object getEmptyStack();

    /**
     * Safely reads an item stack from a container/inventory slot.
     * 
     * @param container The container or inventory object (e.g., net.minecraft.world.Container)
     * @param slot The slot index
     * @return The item stack in that slot
     */
    Object getStackInSlot(Object container, int slot);

    /**
     * Returns the total number of slots in a container.
     */
    int getContainerSize(Object container);

    /**
     * Checks if the item is considered "harmful" (e.g., negative food effects).
     */
    boolean isHarmful(Object stack);

    /**
     * Checks if the item is a block item.
     */
    boolean isBlockItem(Object stack);

    /**
     * Checks if the item is a specific base container (e.g., bucket, bowl, bottle).
     */
    boolean isBaseContainer(Object stack, String type);

    /**
     * Returns a category identifier for the item.
     */
    String getItemCategory(Object stack);

    /**
     * @return The level of the specified enchantment on the item.
     */
    int getEnchantmentLevel(Object stack, String enchantmentId);

    /**
     * @return True if the item is damageable.
     */
    boolean isDamageable(Object stack);

    /**
     * @return The remaining durability of the item.
     */
    int getRemainingDurability(Object stack);

    /**
     * @return True if the item is considered a "correct tool" for the given block state.
     */
    boolean isCorrectToolForBlock(Object stack, Object blockState);

    /**
     * @return The mining speed multiplier of this item against the block state.
     */
    float getMiningSpeed(Object stack, Object blockState);
}
