package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.spi.InventoryInteractionAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;

import java.util.Locale;

public class MinecraftInventoryInteractionAdapter implements InventoryInteractionAdapter {

    @Override
    public void clickSlot(Object client, int containerId, int slotIndex, int button, String clickType) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.player != null) {
            ClickType type = ClickType.valueOf(clickType.toUpperCase(Locale.ROOT));
            mc.gameMode.handleInventoryMouseClick(containerId, slotIndex, button, type, mc.player);
        }
    }

    @Override
    public void closeScreen(Object client) {
        if (client instanceof Minecraft mc && mc.player != null) {
            mc.player.closeContainer();
        }
    }

    @Override
    public void swapToSlot(Object client, int slotIndex) {
        if (client instanceof Minecraft mc && mc.player != null) {
            mc.player.getInventory().selected = slotIndex;
        }
    }

    @Override
    public int getCurrentContainerId(Object client) {
        if (client instanceof Minecraft mc && mc.player != null) {
            return mc.player.containerMenu.containerId;
        }
        return -1;
    }

    @Override
    public void dropItem(Object client, int slotIndex, boolean entireStack) {
        if (client instanceof Minecraft mc && mc.gameMode != null && mc.player != null) {
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, slotIndex, entireStack ? 1 : 0, ClickType.THROW, mc.player);
        }
    }
}
