package is.pig.minecraft.api.spi;

/**
 * Service Provider Interface isolating high-performance 2D/HUD rendering logic.
 */
public interface RenderPipelineAdapter {
    
    /**
     * Draws anti-aliased text overlay.
     * 
     * @param context Render context or DrawContext wrapper passed from Mixin
     */
    void renderText(Object stack, String text, float x, float y, int color);

    void renderOverlayIcon(Object stack, String iconId, float r, float g, float b, float a);

    void renderLightLevel(Object client, Object stack, float partialTicks);

    /**
     * Fills a rectangular screen bounds with flat HSL/RGB colors.
     */
    void fillRect(Object context, int x, int y, int width, int height, int color);

    /**
     * Draws a line between arbitrary screen coordinates.
     */
    void drawLine(Object context, int x1, int y1, int x2, int y2, int color);

    void renderBuildSession(Object client, Object stack, float partialTicks);

    void renderPlacementSession(Object client, Object stack, float partialTicks);

    /**
     * Pulls resources and paints a designated texture asset onto the buffer.
     */
    void drawTexture(Object context, String textureId, int x, int y, int width, int height);

    /**
     * Draws a texture with an inverted color blend mode and custom alpha.
     */
    void drawInvertedTexture(Object context, String textureId, int x, int y, int width, int height, float alpha);

    int getScreenWidth(Object stack);

    int getScreenHeight(Object stack);
}
