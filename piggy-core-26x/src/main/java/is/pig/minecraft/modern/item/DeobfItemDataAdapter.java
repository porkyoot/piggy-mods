package is.pig.minecraft.modern.item;

import is.pig.minecraft.api.spi.ItemDataAdapter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;

import java.util.Optional;

/**
 * Modern implementation of ItemDataAdapter for Minecraft 26.X.
 * Uses fully deobfuscated Mojang names and the refined Data Component API.
 */
public class DeobfItemDataAdapter implements ItemDataAdapter {

    @Override
    public boolean areItemsEqual(Object stackA, Object stackB) {
        if (stackA instanceof ItemStack a && stackB instanceof ItemStack b) {
            return ItemStack.isSameItemSameComponents(a, b);
        }
        return stackA == stackB;
    }

    @Override
    public boolean hasTag(Object stack, String tagId) {
        if (stack instanceof ItemStack s) {
            var resourceLocation = ResourceLocation.parse(tagId);
            var tagKey = net.minecraft.core.registries.Registries.ITEM.getTagKey(resourceLocation);
            return tagKey.map(s::is).orElse(false);
        }
        return false;
    }

    @Override
    public String getItemId(Object stack) {
        if (stack instanceof ItemStack s) {
            return BuiltInRegistries.ITEM.getKey(s.getItem()).toString();
        }
        return "minecraft:air";
    }

    @Override
    public int getCount(Object stack) {
        if (stack instanceof ItemStack s) {
            return s.getCount();
        }
        return 0;
    }

    @Override
    public int getMaxStackSize(Object stack) {
        if (stack instanceof ItemStack s) {
            return s.getMaxStackSize();
        }
        return 64;
    }

    @Override
    public Object copy(Object stack) {
        if (stack instanceof ItemStack s) {
            return s.copy();
        }
        return getEmptyStack();
    }

    @Override
    public Object copyWithCount(Object stack, int count) {
        if (stack instanceof ItemStack s) {
            ItemStack copy = s.copy();
            copy.setCount(count);
            return copy;
        }
        return getEmptyStack();
    }

    @Override
    public void grow(Object stack, int amount) {
        if (stack instanceof ItemStack s) {
            s.grow(amount);
        }
    }

    @Override
    public void shrink(Object stack, int amount) {
        if (stack instanceof ItemStack s) {
            s.shrink(amount);
        }
    }

    @Override
    public boolean isItem(Object stack, String itemId) {
        if (stack instanceof ItemStack s) {
            return getItemId(s).equals(itemId);
        }
        return false;
    }

    @Override
    public Object getEmptyStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public Object getStackInSlot(Object container, int slot) {
        if (container instanceof Container c) {
            return c.getItem(slot);
        }
        return getEmptyStack();
    }

    @Override
    public int getContainerSize(Object container) {
        if (container instanceof Container c) {
            return c.getContainerSize();
        }
        return 0;
    }

    @Override
    public boolean isHarmful(Object stack) {
        if (stack instanceof ItemStack s) {
            // Modern 26.X uses the Food component directly for harm detection
            var food = s.get(DataComponents.FOOD);
            if (food != null) {
                return food.effects().stream().anyMatch(e -> !e.effect().value().getCategory().isBeneficial());
            }
        }
        return false;
    }

    @Override
    public boolean isBlockItem(Object stack) {
        if (stack instanceof ItemStack s) {
            return s.getItem() instanceof net.minecraft.world.item.BlockItem;
        }
        return false;
    }

    @Override
    public boolean isBaseContainer(Object stack, String type) {
        String id = getItemId(stack);
        return switch (type.toLowerCase()) {
            case "bucket" -> id.contains("bucket");
            case "bowl" -> id.contains("bowl");
            case "bottle" -> id.contains("bottle");
            default -> false;
        };
    }

    @Override
    public String getItemCategory(Object stack) {
        if (stack instanceof ItemStack s) {
            // 26.X has refined item groups as part of the data components
            var group = s.get(DataComponents.ITEM_NAME); // Placeholder for modern category lookup
            return getItemId(s).split(":")[1].split("_")[0]; // Fallback to first word of ID
        }
        return "none";
    }
}
