package is.pig.minecraft.modern.render;

import is.pig.minecraft.api.ResourceLocation;
import is.pig.minecraft.api.spi.RenderPipelineAdapter;

/**
 * Modern Vulkan-based rendering adapter for Minecraft 26.X.
 * Strictly adheres to the 26.X Vulkan-only rendering constraints.
 * No MatrixStack or RenderSystem legacy calls allowed.
 */
public class VulkanRenderAdapter implements RenderPipelineAdapter {

    @Override
    public void renderText(Object context, String text, float x, float y, int color) {
        // In 26.X, we use the unified VulkanDrawContext which handles the CommandBuffer orchestration
        if (context instanceof net.minecraft.client.gui.VulkanDrawContext vulkan) {
            vulkan.drawText(text, x, y, color, true); // True for anti-aliasing (Vulkan default)
        }
    }

    @Override
    public void fillRect(Object context, int x, int y, int width, int height, int color) {
        if (context instanceof net.minecraft.client.gui.VulkanDrawContext vulkan) {
            // Using the Vulkan-backed primitive batcher
            vulkan.fill(x, y, x + width, y + height, color);
        }
    }

    @Override
    public void drawLine(Object context, int x1, int y1, int x2, int y2, int color) {
        if (context instanceof net.minecraft.client.gui.VulkanDrawContext vulkan) {
            // 26.X introduced native high-performance line drawing in the HUD pipeline
            vulkan.drawLine(x1, y1, x2, y2, color, 1.0f); // Width = 1.0f
        }
    }

    @Override
    public void drawTexture(Object context, String textureId, int x, int y, int width, int height) {
        if (context instanceof net.minecraft.client.gui.VulkanDrawContext vulkan) {
            // Using the modern ResourceKey based texture lookup
            var res = net.minecraft.resources.ResourceLocation.parse(textureId);
            vulkan.drawTexture(res, x, y, width, height, 0, 0, 1.0f, 1.0f);
        }
    }

    @Override
    public void renderOverlayIcon(Object context, ResourceLocation icon, float r, float g, float b, float a) {
        if (context instanceof net.minecraft.client.gui.VulkanDrawContext vulkan) {
            var res = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(icon.namespace(), icon.path());
            // Vulkan batching: Apply color transform directly to the draw call
            vulkan.drawTexture(res, 0, 0, 16, 16, 0, 0, 1.0f, 1.0f, r, g, b, a);
        }
    }

    @Override
    public void renderLightLevel(Object client, Object context, float partialTicks) {
        // Implementation for modern 26.X world rendering would use the new Vulkan Shader Pipeline
        // This is a placeholder for the actual complex Vulkan implementation
    }

    @Override
    public void renderBuildSession(Object client, Object context, float partialTicks) {
        // Implementation for modern 26.X would use the new Compute Shader based shape rendering
    }

    @Override
    public void renderPlacementSession(Object client, Object context, float partialTicks) {
        // Implementation for modern 26.X would use the new Holographic Overlay pipeline
    }

    @Override
    public int getScreenWidth(Object context) {
        if (context instanceof net.minecraft.client.gui.VulkanDrawContext vulkan) {
            return vulkan.getFramebufferWidth();
        }
        return net.minecraft.client.Minecraft.getInstance().getWindow().getWidth();
    }

    @Override
    public int getScreenHeight(Object context) {
        if (context instanceof net.minecraft.client.gui.VulkanDrawContext vulkan) {
            return vulkan.getFramebufferHeight();
        }
        return net.minecraft.client.Minecraft.getInstance().getWindow().getHeight();
    }
}
