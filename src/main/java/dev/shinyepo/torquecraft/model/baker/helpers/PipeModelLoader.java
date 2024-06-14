package dev.shinyepo.torquecraft.model.baker.helpers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.model.baker.PipeBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class PipeModelLoader implements IGeometryLoader<PipeModelLoader.PipeModelGeometry> {
    public static final ResourceLocation GENERATOR_LOADER = fromNamespaceAndPath(TorqueCraft.MODID, "pipe_loader");

    @Override
    public PipeModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        boolean facade = jsonObject.has("facade") && jsonObject.get("facade").getAsBoolean();
        return new PipeModelGeometry(facade);
    }

    public static class PipeModelGeometry implements IUnbakedGeometry<PipeModelGeometry> {

        private final boolean facade;

        public PipeModelGeometry(boolean facade) {
            this.facade = facade;
        }

        @Override
        public PipeBakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            return new PipeBakedModel(context);
        }
    }
}
