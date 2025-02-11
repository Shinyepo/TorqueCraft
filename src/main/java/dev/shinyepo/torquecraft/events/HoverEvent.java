package dev.shinyepo.torquecraft.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class HoverEvent {

    private boolean outlineArea = false;

    private static void renderShape(
            PoseStack pPoseStack,
            VertexConsumer pConsumer,
            VoxelShape pShape,
            double pX,
            double pY,
            double pZ,
            float pRed,
            float pGreen,
            float pBlue,
            float pAlpha
    ) {
        PoseStack.Pose posestack$pose = pPoseStack.last();
        pShape.forAllEdges(
                (p_323073_, p_323074_, p_323075_, p_323076_, p_323077_, p_323078_) -> {
                    float f = (float)(p_323076_ - p_323073_);
                    float f1 = (float)(p_323077_ - p_323074_);
                    float f2 = (float)(p_323078_ - p_323075_);
                    float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
                    f /= f3;
                    f1 /= f3;
                    f2 /= f3;
                    pConsumer.addVertex(posestack$pose, (float) (p_323073_ + pX), (float) (p_323074_ + pY), (float) (p_323075_ + pZ))
                            .setColor(pRed, pGreen, pBlue, pAlpha)
                            .setNormal(posestack$pose, f, f1, f2);
                    pConsumer.addVertex(posestack$pose, (float) (p_323076_ + pX), (float) (p_323077_ + pY), (float) (p_323078_ + pZ))
                            .setColor(pRed, pGreen, pBlue, pAlpha)
                            .setNormal(posestack$pose, f, f1, f2);
                }
        );
    }

    @SubscribeEvent
    public void onBlockHover(RenderHighlightEvent.Block event) {
        //playground
//        Player player = Minecraft.getInstance().player;
//        if (player == null) {
//            return;
//        }
//        BlockHitResult rayTraceResult = event.getTarget();
//        if (rayTraceResult.getType() != HitResult.Type.MISS) {
//            Level world = player.level();
//            BlockPos pos = rayTraceResult.getBlockPos();
//            MultiBufferSource renderer = event.getMultiBufferSource();
//            Camera info = event.getCamera();
//            PoseStack matrix = event.getPoseStack();
//            ProfilerFiller profiler = world.getProfiler();
//            BlockState blockState = world.getBlockState(pos);
//
//            profiler.push("areaMineOutline");
//
//
//            if (!outlineArea) {
//                Map<BlockPos, BlockState> blocks = new HashMap<>(4);
//                blocks.put(pos, blockState);
//                blocks.put(pos.relative(Direction.NORTH), world.getBlockState(pos.relative(Direction.NORTH)));
//                blocks.put(pos.relative(Direction.EAST), world.getBlockState(pos.relative(Direction.EAST)));
//                blocks.put(pos.relative(Direction.WEST), world.getBlockState(pos.relative(Direction.WEST)));
//                blocks.put(pos.relative(Direction.SOUTH), world.getBlockState(pos.relative(Direction.SOUTH)));
//                outlineArea = true;
//
//                Vec3 renderView = info.getPosition();
//                LevelRenderer levelRenderer = event.getLevelRenderer();
//                Lazy<VertexConsumer> lineConsumer = Lazy.of(() -> renderer.getBuffer(RenderType.lines()));
//                for (Map.Entry<BlockPos, BlockState> block : blocks.entrySet()) {
//                    BlockPos blastingTarget = block.getKey();
//                    if (!pos.equals(blastingTarget) && !ClientHooks.onDrawHighlight(levelRenderer, info, rayTraceResult, event.getPartialTick(), matrix, renderer)) {
//                        renderShape(
//                                matrix,
//                                lineConsumer.get(),
//                                block.getValue().getShape(Minecraft.getInstance().level, blastingTarget, CollisionContext.of(player)),
//                                (double) blastingTarget.getX() - renderView.x,
//                                (double) blastingTarget.getY() - renderView.y,
//                                (double) blastingTarget.getZ() - renderView.z,
//                                0.0F,
//                                0.0F,
//                                0.0F,
//                                0.4F
//                        );
//                    }
//                }
//                outlineArea = false;
//            }
//            profiler.pop();

//        }
    }

    @SubscribeEvent
    public void drawLine(RenderLevelStageEvent e) {
        //playground
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
            return;

//        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
//        PoseStack pose = e.getPoseStack();
//        EntityRenderDispatcher erd = Minecraft.getInstance().getEntityRenderDispatcher();
//        double renderPosX = erd.camera.getPosition().x();
//        double renderPosY = erd.camera.getPosition().y();
//        double renderPosZ = erd.camera.getPosition().z();


//        float r = 1.0f, g = 0.0f, b = 0.0f, a = 1.0f;
//        float x1 = 1, y1 = -59, z1 = 0;
//        float x2 = 13, y2 = -59, z2 = 0;
//        pose.pushPose();
//        pose.translate(-renderPosX, -renderPosY, -renderPosZ);
//        VertexConsumer builder = buffers.getBuffer(RenderType.solid());
//        Matrix4f four = pose.last().pose();
//        builder.vertex(four, x1, y1, z1).color(r, g, b, a).normal(pose.last(),0, 0, 0).endVertex();
//        builder.vertex(four, x1+1, y1, z1).color(r, g, b, a).normal(pose.last(),0, 0, 0).endVertex();
//        builder.vertex(four, x1+1, y1+1, z1).color(r, g, b, a).normal(pose.last(),0, 0, 0).endVertex();
//        builder.vertex(four, x1+1, y1+1, z1+1).color(r, g, b, a).normal(pose.last(),0, 0, 0).endVertex();
////        builder.vertex(four, x2, y2, z2).color(r, g, b, a).normal(pose.last(),0, 0, 0).endVertex();
//
//        pose.popPose();
//        buffers.endBatch(RenderType.solid());


//        double drawRadius = 0.5D;
//        double minCorner = 0.5D - drawRadius;
//        double maxCorner = 0.5D + drawRadius;
//        Vector4d vector4dMin = new Vector4d(minCorner, minCorner, minCorner, 1.0D);
//        Vector4d vector4dMax = new Vector4d(maxCorner, maxCorner, maxCorner, 1.0D);
//
//        BlockPos worldSpot = new BlockPos(1,-59,0);
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        RenderSystem.disableBlend();
//        RenderSystem.disableCull();
//        RenderSystem.enableDepthTest();
//
//        Tesselator tesselator = Tesselator.getInstance();
//        BufferBuilder bufferbuilder = tesselator.getBuilder();
//
//        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//        pose.pushPose();
//
//        if (!(e.getLevelRenderer()).getFrustum().isVisible(new AABB(
//                worldSpot.getX() + minCorner,
//                worldSpot.getY() + minCorner,
//                worldSpot.getZ() + minCorner,
//                worldSpot.getX() + maxCorner,
//                worldSpot.getY() + maxCorner,
//                worldSpot.getZ() + maxCorner)))
//        {
//            tesselator.end();
//            pose.popPose();
//            pose.popPose();
//
//            return;
//        }
//
//        TorqueRenders.renderQuadBox(bufferbuilder, pose.last().pose(), (float) (vector4dMin.x() + worldSpot.getX()),
//                (float) (vector4dMin.y() + worldSpot.getY()),
//                (float) (vector4dMin.z() + worldSpot.getZ() ),
//                (float) (vector4dMax.x() + worldSpot.getX()),
//                (float) (vector4dMax.y() + worldSpot.getY()),
//                (float) (vector4dMax.z() + worldSpot.getZ()),
//                (int) (255f),
//                (int) (255f),
//                (int) (255f),
//                (int) (240f));
//        BufferUploader.drawWithShader(bufferbuilder.end());
//        pose.popPose();
//        pose.popPose();
//        tesselator.end();
    }
}
