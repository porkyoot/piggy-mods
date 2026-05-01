package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.spi.ItemDataAdapter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 * Legacy implementation of ItemDataAdapter for Minecraft 1.21.X.
 * Uses ItemStack and DataComponents for comparison and data extraction.
 */
public class MinecraftItemDataAdapter implements ItemDataAdapter {

    @Override
    public boolean areItemsEqual(Object stackA, Object stackB) {
        if (stackA instanceof ItemStack a && stackB instanceof ItemStack b) {
            // MojMap name for same item and same components
            return ItemStack.matches(a, b);
        }
        return false;
    }

    @Override
    public boolean hasTag(Object stack, String tagId) {
        if (stack instanceof ItemStack itemStack) {
            ResourceLocation rl = ResourceLocation.parse(tagId);
            return itemStack.getTags().anyMatch(tag -> tag.location().equals(rl));
        }
        return false;
    }

    @Override
    public String getItemId(Object stack) {
        if (stack instanceof ItemStack itemStack) {
            return BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
        }
        return "minecraft:air";
    }

    @Override
    public int getCount(Object stack) {
        if (stack instanceof ItemStack itemStack) {
            return itemStack.getCount();
        }
        return 0;
    }

    @Override
    public int getMaxStackSize(Object stack) {
        if (stack instanceof ItemStack itemStack) {
            return itemStack.getMaxStackSize();
        }
        return 64;
    }

    @Override
    public Object copy(Object stack) {
        if (stack instanceof ItemStack itemStack) {
            return itemStack.copy();
        }
        return getEmptyStack();
    }

    @Override
    public Object copyWithCount(Object stack, int count) {
        if (stack instanceof ItemStack itemStack) {
            ItemStack copy = itemStack.copy();
            copy.setCount(count);
            return copy;
        }
        return getEmptyStack();
    }

    @Override
    public void grow(Object stack, int amount) {
        if (stack instanceof ItemStack itemStack) {
            itemStack.grow(amount);
        }
    }

    @Override
    public void shrink(Object stack, int amount) {
        if (stack instanceof ItemStack itemStack) {
            itemStack.shrink(amount);
        }
    }

    @Override
    public boolean isItem(Object stack, String itemId) {
        if (stack instanceof ItemStack itemStack) {
            return getItemId(itemStack).equals(itemId);
        }
        return false;
    }

    @Override
    public Object getEmptyStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public Object getStackInSlot(Object container, int slot) {
        if (container instanceof net.minecraft.world.Container mcContainer) {
            return mcContainer.getItem(slot);
        }
        return getEmptyStack();
    }

    @Override
    public int getContainerSize(Object container) {
        if (container instanceof net.minecraft.world.Container mcContainer) {
            return mcContainer.getContainerSize();
        }
        return 0;
    }

    @Override
    public boolean isHarmful(Object stack) {
        if (stack instanceof ItemStack itemStack) {
            net.minecraft.world.food.FoodProperties food = itemStack.get(DataComponents.FOOD);
            if (food != null) {
                for (net.minecraft.world.food.FoodProperties.PossibleEffect effect : food.effects()) {
                    if (!effect.effect().getEffect().value().isBeneficial()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isBlockItem(Object stack) {
        if (stack instanceof ItemStack itemStack) {
            return itemStack.getItem() instanceof net.minecraft.world.item.BlockItem;
        }
        return false;
    }

    @Override
    public boolean isBaseContainer(Object stack, String type) {
        if (stack instanceof ItemStack itemStack) {
            net.minecraft.world.item.Item item = itemStack.getItem();
            return switch (type) {
                case "bucket" -> item == net.minecraft.world.item.Items.BUCKET;
                case "bottle" -> item == net.minecraft.world.item.Items.GLASS_BOTTLE;
                case "bowl" -> item == net.minecraft.world.item.Items.BOWL;
                default -> false;
            };
        }
        return false;
    }

    @Override
    public String getItemCategory(Object stack) {
        if (stack instanceof ItemStack itemStack) {
            if (itemStack.isEmpty()) return "other";
            net.minecraft.world.item.Item item = itemStack.getItem();

            if (itemStack.get(DataComponents.FOOD) != null) return "food";
            
            if (item instanceof net.minecraft.world.item.SwordItem || 
                item instanceof net.minecraft.world.item.TridentItem || 
                item instanceof net.minecraft.world.item.BowItem || 
                item instanceof net.minecraft.world.item.CrossbowItem) {
                return "weapon";
            }

            if (item instanceof net.minecraft.world.item.DiggerItem || 
                item instanceof net.minecraft.world.item.ShearsItem || 
                item instanceof net.minecraft.world.item.FlintAndSteelItem || 
                item instanceof net.minecraft.world.item.FishingRodItem) {
                return "tool";
            }

            if (item instanceof net.minecraft.world.item.BlockItem) return "block";
        }
        return "other";
    }
}
