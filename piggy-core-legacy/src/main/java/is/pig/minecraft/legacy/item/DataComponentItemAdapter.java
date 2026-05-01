package is.pig.minecraft.legacy.item;

import is.pig.minecraft.api.spi.ItemDataAdapter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;

import java.util.Optional;

/**
 * Minecraft 1.21.X implementation of ItemDataAdapter using the Data Component API.
 * Decouples Piggy API from net.minecraft.world.item.ItemStack.
 */
public class DataComponentItemAdapter implements ItemDataAdapter {

    @Override
    public boolean areItemsEqual(Object stackA, Object stackB) {
        if (stackA instanceof ItemStack s1 && stackB instanceof ItemStack s2) {
            return ItemStack.isSameItemSameComponents(s1, s2);
        }
        return false;
    }

    @Override
    public boolean hasTag(Object stack, String tagId) {
        if (stack instanceof ItemStack s) {
            ResourceLocation rl = ResourceLocation.parse(tagId);
            TagKey<net.minecraft.world.item.Item> tagKey = TagKey.create(Registries.ITEM, rl);
            return s.is(tagKey);
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
            return s.copyWithCount(count);
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
            ResourceLocation rl = ResourceLocation.parse(itemId);
            return BuiltInRegistries.ITEM.getKey(s.getItem()).equals(rl);
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
            // Check for poisonous components or negative food effects in 1.21
            var foodProperties = s.get(DataComponents.FOOD);
            if (foodProperties != null) {
                // Heuristic: check if any effect is potentially harmful
                // In a real implementation, we would iterate through effects and check for Poison, Wither, etc.
                return foodProperties.effects().stream().anyMatch(e -> !e.effect().value().isBeneficial());
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
        if (stack instanceof ItemStack s) {
            String id = getItemId(s);
            return switch (type.toLowerCase()) {
                case "bucket" -> id.contains("bucket");
                case "bowl" -> id.contains("bowl");
                case "bottle" -> id.contains("bottle");
                default -> false;
            };
        }
        return false;
    }

    @Override
    public String getItemCategory(Object stack) {
        if (stack instanceof ItemStack s) {
            if (isBlockItem(s)) return "block";
            if (s.isDamageableItem()) return "tool";
            if (s.has(DataComponents.FOOD)) return "food";
            if (s.isEnchanted()) return "enchanted";
            return "item";
        }
        return "none";
    }
}
