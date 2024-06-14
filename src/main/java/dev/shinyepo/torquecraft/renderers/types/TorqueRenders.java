package dev.shinyepo.torquecraft.renderers.types;

import com.mojang.blaze3d.vertex.BufferBuilder;
import org.joml.Matrix4f;

public class TorqueRenders {
    public static void renderQuadBox(BufferBuilder builder, Matrix4f pose, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int red, int green, int blue, int alpha) {
        builder.addVertex(pose, maxX, minY, minZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, minX, minY, minZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, minX, maxY, minZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, maxX, maxY, minZ).setColor(red, green, blue, alpha).setNormal(1.0F, 1.0F, 0.0F);

        builder.addVertex(pose, maxX, maxY, maxZ).setColor(red, green, blue, alpha).setNormal(1.0F, 1.0F, 0.0F);
        builder.addVertex(pose, minX, maxY, maxZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, minX, minY, maxZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, maxX, minY, maxZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);

        builder.addVertex(pose, maxX, minY, maxZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 1.0F);
        builder.addVertex(pose, minX, minY, maxZ).setColor(red, green, blue, alpha).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, minX, minY, minZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, maxX, minY, minZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);

        builder.addVertex(pose, maxX, maxY, minZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, minX, maxY, minZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, minX, maxY, maxZ).setColor(red, green, blue, alpha).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, maxX, maxY, maxZ).setColor(red, green, blue, alpha).setNormal(1.0F, 0.0F, 1.0F);

        builder.addVertex(pose, minX, minY, minZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, minX, minY, maxZ).setColor(red, green, blue, alpha).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, minX, maxY, maxZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 1.0F);
        builder.addVertex(pose, minX, maxY, minZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 0.0F);

        builder.addVertex(pose, maxX, maxY, minZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, maxX, maxY, maxZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 1.0F);
        builder.addVertex(pose, maxX, minY, maxZ).setColor(red, green, blue, alpha).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, maxX, minY, minZ).setColor(red, green, blue, alpha).setNormal(0.0F, 1.0F, 0.0F);
    }
}
