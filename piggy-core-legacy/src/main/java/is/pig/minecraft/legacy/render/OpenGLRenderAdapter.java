package is.pig.minecraft.legacy.render;

import is.pig.minecraft.api.spi.RenderPipelineAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * OpenGL-based rendering adapter for Minecraft 1.21.X using Mojang's GuiGraphics.
 * Decouples rendering logic from the platform-specific HUD implementation.
 */
public class OpenGLRenderAdapter implements RenderPipelineAdapter {

    @Override
    public void renderText(Object stack, String text, float x, float y, int color) {
        if (stack instanceof GuiGraphics graphics) {
            graphics.drawString(Minecraft.getInstance().font, text, (int) x, (int) y, color);
        }
    }

    @Override
    public void fillRect(Object context, int x, int y, int width, int height, int color) {
        if (context instanceof GuiGraphics graphics) {
            graphics.fill(x, y, x + width, y + height, color);
        }
    }

    @Override
    public void drawLine(Object context, int x1, int y1, int x2, int y2, int color) {
        if (context instanceof GuiGraphics graphics) {
            if (x1 == x2) {
                graphics.fill(x1, Math.min(y1, y2), x1 + 1, Math.max(y1, y2), color);
            } else if (y1 == y2) {
                graphics.fill(Math.min(x1, x2), y1, Math.max(x1, x2), y1 + 1, color);
            }
        }
    }

    @Override
    public void drawTexture(Object context, String textureId, int x, int y, int width, int height) {
        if (context instanceof GuiGraphics graphics) {
            ResourceLocation rl = ResourceLocation.parse(textureId);
            graphics.blit(rl, x, y, 0, 0, width, height, width, height);
        }
    }

    @Override
    public void drawInvertedTexture(Object context, String textureId, int x, int y, int width, int height, float alpha) {
        if (context instanceof GuiGraphics graphics) {
            ResourceLocation rl = ResourceLocation.parse(textureId);
            RenderType invertedType = RenderType.create(
                    "piggy_gui_inverted_" + rl.getPath().replace("/", "_"),
                    DefaultVertexFormat.POSITION_TEX_COLOR,
                    VertexFormat.Mode.QUADS,
                    1536,
                    false,
                    false,
                    RenderType.CompositeState.builder()
                            .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionTexColorShader))
                            .setTextureState(new RenderStateShard.TextureStateShard(rl, false, false))
                            .setTransparencyState(new RenderStateShard.TransparencyStateShard("gui_inverted_transparency", () -> {
                                RenderSystem.enableBlend();
                                RenderSystem.blendFuncSeparate(
                                        GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                                        GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                                        GlStateManager.SourceFactor.ONE,
                                        GlStateManager.DestFactor.ZERO);
                            }, () -> {
                                RenderSystem.disableBlend();
                                RenderSystem.defaultBlendFunc();
                            }))
                            .createCompositeState(false)
            );

            VertexConsumer buffer = graphics.bufferSource().getBuffer(invertedType);
            Matrix4f matrix = graphics.pose().last().pose();

            float x1 = (float) x;
            float y1 = (float) y;
            float x2 = (float) (x + width);
            float y2 = (float) (y + height);

            buffer.addVertex(matrix, x1, y1, 0).setUv(0, 0).setColor(alpha, alpha, alpha, alpha);
            buffer.addVertex(matrix, x1, y2, 0).setUv(0, 1).setColor(alpha, alpha, alpha, alpha);
            buffer.addVertex(matrix, x2, y2, 0).setUv(1, 1).setColor(alpha, alpha, alpha, alpha);
            buffer.addVertex(matrix, x2, y1, 0).setUv(1, 0).setColor(alpha, alpha, alpha, alpha);

            graphics.flush();
        }
    }

    @Override
    public void renderOverlayIcon(Object stack, String iconId, float r, float g, float b, float a) {
        if (stack instanceof GuiGraphics graphics) {
            ResourceLocation rl = ResourceLocation.parse(iconId);
            graphics.setColor(r, g, b, a);
            graphics.blit(rl, 0, 0, 0, 0, 16, 16, 16, 16);
            graphics.setColor(1f, 1f, 1f, 1f);
        }
    }

    @Override
    public void renderLightLevel(Object client, Object stack, float partialTicks) {
        if (client instanceof Minecraft mc && stack instanceof com.mojang.blaze3d.vertex.PoseStack poseStack) {
            var cameraPos = mc.gameRenderer.getMainCamera().getPosition();
            var buffers = mc.renderBuffers().bufferSource();
            is.pig.minecraft.build.mvc.view.LightLevelOverlayRenderer.render(mc, cameraPos, poseStack, buffers);
        }
    }

    @Override
    public void renderBuildSession(Object client, Object stack, float partialTicks) {
        if (client instanceof Minecraft mc && stack instanceof com.mojang.blaze3d.vertex.PoseStack poseStack) {
            // Logic from PiggyBuildClient.renderBuildShapes
            is.pig.minecraft.build.mvc.model.BuildSession session = is.pig.minecraft.build.mvc.model.BuildSession.getInstance();
            if (!session.isActive()) return;

            var cameraPos = mc.gameRenderer.getMainCamera().getPosition();
            var buffers = mc.renderBuffers().bufferSource();
            var builder = buffers.getBuffer(is.pig.minecraft.build.mvc.view.HighlightRenderType.TYPE);
            
            float[] rgba = is.pig.minecraft.build.config.PiggyBuildConfig.getInstance().getHighlightColor().getComponents(null);
            
            double rx = session.getAnchorPos().x() - cameraPos.x;
            double ry = session.getAnchorPos().y() - cameraPos.y;
            double rz = session.getAnchorPos().z() - cameraPos.z;

            poseStack.pushPose();
            poseStack.translate(rx, ry, rz);

            switch (session.getShape()) {
                case BLOCK -> is.pig.minecraft.build.mvc.view.WorldShapeRenderer.drawBlock(builder, poseStack.last().pose(), 0, 0, 0, rgba[0], rgba[1], rgba[2], rgba[3]);
                case LINE -> is.pig.minecraft.build.mvc.view.WorldShapeRenderer.drawLine(builder, poseStack.last().pose(), 
                    is.pig.minecraft.lib.util.TypeConverter.toMinecraft(session.getAnchorAxis()),
                    (float) session.getRadius(), rgba[0], rgba[1], rgba[2], rgba[3]);
                case SPHERE -> is.pig.minecraft.build.mvc.view.WorldShapeRenderer.drawSphere(builder, poseStack.last().pose(), (float) session.getRadius(), rgba[0], rgba[1], rgba[2], rgba[3]);
                case RING -> is.pig.minecraft.build.mvc.view.WorldShapeRenderer.drawRing(builder, poseStack.last().pose(), 
                    is.pig.minecraft.lib.util.TypeConverter.toMinecraft(session.getAnchorAxis()),
                    (float) session.getRadius(), rgba[0], rgba[1], rgba[2], rgba[3]);
            }

            poseStack.popPose();
            buffers.endBatch(is.pig.minecraft.build.mvc.view.HighlightRenderType.TYPE);
        }
    }

    @Override
    public void renderPlacementSession(Object client, Object stack, float partialTicks) {
        if (client instanceof Minecraft mc && stack instanceof com.mojang.blaze3d.vertex.PoseStack poseStack) {
            // Logic from PiggyBuildClient.renderDirectionalPlacement
            is.pig.minecraft.build.mvc.model.PlacementSession session = is.pig.minecraft.build.mvc.model.PlacementSession.getInstance();
            if (!session.isActive()) return;
            if (!is.pig.minecraft.build.config.PiggyBuildConfig.getInstance().isFeatureFlexiblePlacementEnabled()) return;

            if (mc.hitResult != null && mc.hitResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                var hit = (net.minecraft.world.phys.BlockHitResult) mc.hitResult;
                var cameraPos = mc.gameRenderer.getMainCamera().getPosition();
                var buffers = mc.renderBuffers().bufferSource();
                
                is.pig.minecraft.api.Direction offset = session.getCurrentOffset();
                ResourceLocation tex = (offset == null)
                        ? is.pig.minecraft.build.mvc.view.DirectionalPlacementRenderer.getCenterTexture()
                        : is.pig.minecraft.build.mvc.view.DirectionalPlacementRenderer.getArrowTexture();

                var overlayBuilder = buffers.getBuffer(is.pig.minecraft.build.mvc.view.DirectionalPlacementRenderer.getRenderType(tex));

                double rx = hit.getBlockPos().getX() - cameraPos.x;
                double ry = hit.getBlockPos().getY() - cameraPos.y;
                double rz = hit.getBlockPos().getZ() - cameraPos.z;

                poseStack.pushPose();
                poseStack.translate(rx, ry, rz);

                float[] placementRgba = is.pig.minecraft.build.config.PiggyBuildConfig.getInstance().getPlacementOverlayColor().getComponents(null);

                is.pig.minecraft.build.mvc.view.DirectionalPlacementRenderer.render(
                        overlayBuilder,
                        poseStack,
                        hit.getDirection(),
                        is.pig.minecraft.lib.util.TypeConverter.toMinecraft(offset),
                        placementRgba[0], placementRgba[1], placementRgba[2], placementRgba[3]);

                poseStack.popPose();
                buffers.endBatch(is.pig.minecraft.build.mvc.view.DirectionalPlacementRenderer.getRenderType(tex));
            }
        }
    }

    @Override
    public int getScreenWidth(Object stack) {
        if (stack instanceof GuiGraphics graphics) {
            return graphics.guiWidth();
        }
        return Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }

    @Override
    public int getScreenHeight(Object stack) {
        if (stack instanceof GuiGraphics graphics) {
            return graphics.guiHeight();
        }
        return Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }
}
