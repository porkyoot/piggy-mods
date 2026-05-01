package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.spi.ScreenAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class MinecraftScreenAdapter implements ScreenAdapter {

    @Override
    public boolean isContainerScreenOpen(Object client) {
        if (client instanceof Minecraft mc) {
            return mc.screen instanceof AbstractContainerScreen;
        }
        return false;
    }

    @Override
    public List<Integer> getPlayerSlotIndices(Object client) {
        List<Integer> indices = new ArrayList<>();
        if (client instanceof Minecraft mc && mc.screen instanceof AbstractContainerScreen<?> screen) {
            var menu = screen.getMenu();
            var playerInventory = mc.player.getInventory();
            for (Slot slot : menu.slots) {
                if (slot.container == playerInventory) {
                    indices.add(slot.index);
                }
            }
            // Fallback if no direct container match (common in modded containers)
            if (indices.isEmpty() && menu.slots.size() >= 36) {
                int total = menu.slots.size();
                for (int i = total - 36; i < total; i++) {
                    indices.add(i);
                }
            }
        }
        return indices;
    }

    @Override
    public List<Integer> getStorageSlotIndices(Object client) {
        List<Integer> indices = new ArrayList<>();
        if (client instanceof Minecraft mc && mc.screen instanceof AbstractContainerScreen<?> screen) {
            var menu = screen.getMenu();
            var playerIndices = getPlayerSlotIndices(client);
            for (Slot slot : menu.slots) {
                if (!playerIndices.contains(slot.index)) {
                    indices.add(slot.index);
                }
            }
        }
        return indices;
    }

    @Override
    public Object getStackInSlot(Object client, int slotIndex) {
        if (client instanceof Minecraft mc && mc.screen instanceof AbstractContainerScreen<?> screen) {
            var menu = screen.getMenu();
            if (slotIndex >= 0 && slotIndex < menu.slots.size()) {
                return menu.slots.get(slotIndex).getItem();
            }
        }
        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    @Override
    public int getContainerId(Object client) {
        if (client instanceof Minecraft mc && mc.screen instanceof AbstractContainerScreen<?> screen) {
            return screen.getMenu().containerId;
        }
        return -1;
    }

    @Override
    public boolean isPlayerInventorySlot(Object slot) {
        if (slot instanceof Slot s) {
            return s.container instanceof net.minecraft.world.entity.player.Inventory;
        }
        return false;
    }

    @Override
    public int getSlotIndex(Object slot) {
        if (slot instanceof net.minecraft.world.inventory.Slot s) {
            return s.index;
        }
        return -1;
    }

    @Override
    public boolean isContainerScreen(Object screen) {
        return screen instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
    }

    @Override
    public boolean isInventoryScreen(Object screen) {
        return screen instanceof net.minecraft.client.gui.screens.inventory.InventoryScreen;
    }

    @Override
    public void initScreen(Object screen, Object client, int width, int height) {
        if (screen instanceof net.minecraft.client.gui.screens.Screen s && client instanceof net.minecraft.client.Minecraft mc) {
            s.init(mc, width, height);
        }
    }

    @Override
    public int getSlotX(Object client, int slotIndex) {
        if (client instanceof net.minecraft.client.Minecraft mc && mc.player != null && mc.player.containerMenu != null) {
            if (slotIndex >= 0 && slotIndex < mc.player.containerMenu.slots.size()) {
                return mc.player.containerMenu.slots.get(slotIndex).x;
            }
        }
        return 0;
    }

    @Override
    public int getSlotY(Object client, int slotIndex) {
        if (client instanceof net.minecraft.client.Minecraft mc && mc.player != null && mc.player.containerMenu != null) {
            if (slotIndex >= 0 && slotIndex < mc.player.containerMenu.slots.size()) {
                return mc.player.containerMenu.slots.get(slotIndex).y;
            }
        }
        return 0;
    }

    @Override
    public <T extends is.pig.minecraft.api.RadialMenuItem> void openRadialMenu(
            Object client,
            String title,
            T centerItem,
            java.util.List<T> radialItems,
            T currentSelection,
            java.util.function.Consumer<T> onSelectionChanged,
            Runnable onCloseCallback
    ) {
        if (client instanceof net.minecraft.client.Minecraft mc) {
            mc.execute(() -> {
                mc.setScreen(new is.pig.minecraft.legacy.ui.GenericRadialMenuScreen<>(
                        net.minecraft.network.chat.Component.literal(title),
                        centerItem,
                        radialItems,
                        currentSelection,
                        com.mojang.blaze3d.platform.InputConstants.UNKNOWN,
                        onSelectionChanged,
                        onCloseCallback,
                        null,
                        null
                ));
            });
        }
    }
}
