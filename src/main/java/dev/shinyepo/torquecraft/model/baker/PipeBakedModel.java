package dev.shinyepo.torquecraft.model.baker;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.block.prefab.pipes.FluidPipe;
import dev.shinyepo.torquecraft.block.prefab.pipes.SteamPipe;
import dev.shinyepo.torquecraft.model.baker.helpers.PipeConnection;
import dev.shinyepo.torquecraft.model.baker.helpers.PipePatterns;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static dev.shinyepo.torquecraft.model.baker.helpers.BakedModelHelper.quad;
import static dev.shinyepo.torquecraft.model.baker.helpers.BakedModelHelper.v;
import static dev.shinyepo.torquecraft.model.baker.helpers.PipeConnection.*;
import static dev.shinyepo.torquecraft.model.baker.helpers.PipePatterns.Pattern;
import static dev.shinyepo.torquecraft.model.baker.helpers.PipePatterns.QuadSetting;
import static dev.shinyepo.torquecraft.model.baker.helpers.PipePatterns.SpriteIdx.*;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;
import static net.minecraft.resources.ResourceLocation.withDefaultNamespace;


public class PipeBakedModel implements IDynamicBakedModel {
    private final IGeometryBakingContext context;
    private BlockState pipeState;
//    private final boolean facade;

    private TextureAtlasSprite spriteConnector;
    private TextureAtlasSprite spriteSide;
    private TextureAtlasSprite spriteOutputConnector;
    private TextureAtlasSprite spriteOutputSide;
    private TextureAtlasSprite spriteInputConnector;
    private TextureAtlasSprite spriteInputSide;
    private TextureAtlasSprite spriteNonePipe;
    private TextureAtlasSprite spriteNormalPipe;
    private TextureAtlasSprite spriteEndPipe;
    private TextureAtlasSprite spriteCornerPipe;
    private TextureAtlasSprite spriteThreePipe;
    private TextureAtlasSprite spriteCrossPipe;

    static {
        // For all possible patterns we define the sprite to use and the rotation. Note that each
        // pattern looks at the existance of a pipe section for each of the four directions
        // excluding the one we are looking at.
        PipePatterns.PATTERNS.put(Pattern.of(false, false, false, false), QuadSetting.of(SPRITE_NONE, 0));
        PipePatterns.PATTERNS.put(Pattern.of(true, false, false, false), QuadSetting.of(SPRITE_END, 3));
        PipePatterns.PATTERNS.put(Pattern.of(false, true, false, false), QuadSetting.of(SPRITE_END, 0));
        PipePatterns.PATTERNS.put(Pattern.of(false, false, true, false), QuadSetting.of(SPRITE_END, 1));
        PipePatterns.PATTERNS.put(Pattern.of(false, false, false, true), QuadSetting.of(SPRITE_END, 2));
        PipePatterns.PATTERNS.put(Pattern.of(true, true, false, false), QuadSetting.of(SPRITE_CORNER, 0));
        PipePatterns.PATTERNS.put(Pattern.of(false, true, true, false), QuadSetting.of(SPRITE_CORNER, 1));
        PipePatterns.PATTERNS.put(Pattern.of(false, false, true, true), QuadSetting.of(SPRITE_CORNER, 2));
        PipePatterns.PATTERNS.put(Pattern.of(true, false, false, true), QuadSetting.of(SPRITE_CORNER, 3));
        PipePatterns.PATTERNS.put(Pattern.of(false, true, false, true), QuadSetting.of(SPRITE_STRAIGHT, 0));
        PipePatterns.PATTERNS.put(Pattern.of(true, false, true, false), QuadSetting.of(SPRITE_STRAIGHT, 1));
        PipePatterns.PATTERNS.put(Pattern.of(true, true, true, false), QuadSetting.of(SPRITE_THREE, 0));
        PipePatterns.PATTERNS.put(Pattern.of(false, true, true, true), QuadSetting.of(SPRITE_THREE, 1));
        PipePatterns.PATTERNS.put(Pattern.of(true, false, true, true), QuadSetting.of(SPRITE_THREE, 2));
        PipePatterns.PATTERNS.put(Pattern.of(true, true, false, true), QuadSetting.of(SPRITE_THREE, 3));
        PipePatterns.PATTERNS.put(Pattern.of(true, true, true, true), QuadSetting.of(SPRITE_CROSS, 0));
    }

    public PipeBakedModel(IGeometryBakingContext context) {
        this.context = context;
//        this.facade = facade;
    }

    private void initTextures() {
        BlockState pipeState = getPipeState();
        String model = this.context.getModelName();
        if (pipeState != null || spriteConnector == null) {
            if (model.equals("torquecraft:item/fluid_pipe") || (pipeState != null && pipeState.getBlock() instanceof FluidPipe)) {
                spriteConnector = getTexture("block/pipe/fluid/not_configured_connector");
                spriteSide = getTexture("block/pipe/fluid/not_configured_side");
                spriteInputConnector = getTexture("block/pipe/fluid/input_connector");
                spriteInputSide = getTexture("block/pipe/fluid/input_side");
                spriteOutputConnector = getTexture("block/pipe/fluid/output_connector");
                spriteOutputSide = getTexture("block/pipe/fluid/output_side");
                spriteNormalPipe = getTexture("block/pipe/fluid/normal");
                spriteNonePipe = getTexture("block/pipe/fluid/none");
                spriteEndPipe = getTexture("block/pipe/fluid/end");
                spriteCornerPipe = getTexture("block/pipe/fluid/corner");
                spriteThreePipe = getTexture("block/pipe/fluid/three");
                spriteCrossPipe = getTexture("block/pipe/fluid/cross");
            } else if (model.equals("torquecraft:item/steam_pipe")|| (pipeState != null && pipeState.getBlock() instanceof SteamPipe)) {
                spriteConnector = getTexture("block/pipe/steam/connector");
                spriteNormalPipe = getTexture("block/pipe/steam/normal");
                spriteNonePipe = getTexture("block/pipe/steam/none");
                spriteEndPipe = getTexture("block/pipe/steam/end");
                spriteCornerPipe = getTexture("block/pipe/steam/corner");
                spriteThreePipe = getTexture("block/pipe/steam/three");
                spriteCrossPipe = getTexture("block/pipe/steam/cross");
                spriteSide = getTexture("block/pipe/steam/side");
            }
        }
    }

    // All textures are baked on a big texture atlas. This function gets the texture from that atlas
    private TextureAtlasSprite getTexture(String path) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fromNamespaceAndPath(TorqueCraft.MODID, path));
    }

    private void setPipeState(BlockState pipeState) {
        this.pipeState = pipeState;
    }

    private BlockState getPipeState() {
        return this.pipeState;
    }

    private TextureAtlasSprite getSpriteNormal(PipePatterns.SpriteIdx idx) {
        initTextures();
        return switch (idx) {
            case SPRITE_NONE -> spriteNonePipe;
            case SPRITE_END -> spriteEndPipe;
            case SPRITE_STRAIGHT -> spriteNormalPipe;
            case SPRITE_CORNER -> spriteCornerPipe;
            case SPRITE_THREE -> spriteThreePipe;
            case SPRITE_CROSS -> spriteCrossPipe;
        };
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType layer) {
        if (state != null) setPipeState(state);
        initTextures();
        List<BakedQuad> quads = new ArrayList<>();
        if (side == null && (layer == null || layer.equals(RenderType.solid()))) {
            // Called with the blockstate from our block. Here we get the values of the six properties and pass that to
            // our baked model implementation. If state == null we are called from the inventory and we use the default
            // values for the properties
            PipeConnection north, south, west, east, up, down;
            if (state != null) {
                north = state.getValue(FluidPipe.NORTH);
                south = state.getValue(FluidPipe.SOUTH);
                west = state.getValue(FluidPipe.WEST);
                east = state.getValue(FluidPipe.EAST);
                up = state.getValue(FluidPipe.UP);
                down = state.getValue(FluidPipe.DOWN);
            } else {
                // If we are a facade and we are an item then we render as the 'side' texture as a full block
//                if (facade) {
//                    quads.add(quad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteSide));
//                    quads.add(quad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteSide));
//                    quads.add(quad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteSide));
//                    quads.add(quad(v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), v(0, 0, 0), spriteSide));
//                    quads.add(quad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteSide));
//                    quads.add(quad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteSide));
//                    return quads;
//                }
                north = south = west = east = up = down = NONE;
            }

            TextureAtlasSprite spritePipe = spriteNormalPipe;
            Function<PipePatterns.SpriteIdx, TextureAtlasSprite> spriteGetter = this::getSpriteNormal;

            double o = .3;      // Thickness of the pipe. .0 would be full block, .5 is infinitely thin.
            double p = .1;      // Thickness of the connector as it is put on the connecting block
            double q = .3;      // The wideness of the connector

            // For each side we either cap it off if there is no similar block adjacent on that side
            // or else we extend so that we touch the adjacent block:
            if (up == PIPE) {
                quads.add(quad(v(1 - o, 1, o), v(1 - o, 1, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), spritePipe));
                quads.add(quad(v(o, 1, 1 - o), v(o, 1, o), v(o, 1 - o, o), v(o, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(o, 1, o), v(1 - o, 1, o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spritePipe));
                quads.add(quad(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1, 1 - o), v(o, 1, 1 - o), spritePipe));
            } else if (up == BLOCK || up == INPUT || up == OUTPUT) {
                TextureAtlasSprite sideSprite = up == BLOCK ? spriteSide : up == INPUT ? spriteInputSide : spriteOutputSide;
                TextureAtlasSprite connectorSprite = up == BLOCK ? spriteConnector : up == INPUT ? spriteInputConnector : spriteOutputConnector;
                quads.add(quad(v(1 - o, 1 - p, o), v(1 - o, 1 - p, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), spritePipe));
                quads.add(quad(v(o, 1 - p, 1 - o), v(o, 1 - p, o), v(o, 1 - o, o), v(o, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(o, 1 - p, o), v(1 - o, 1 - p, o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spritePipe));
                quads.add(quad(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - p, 1 - o), v(o, 1 - p, 1 - o), spritePipe));

                quads.add(quad(v(1 - q, 1 - p, q), v(1 - q, 1, q), v(1 - q, 1, 1 - q), v(1 - q, 1 - p, 1 - q), sideSprite));
                quads.add(quad(v(q, 1 - p, 1 - q), v(q, 1, 1 - q), v(q, 1, q), v(q, 1 - p, q), sideSprite));
                quads.add(quad(v(q, 1, q), v(1 - q, 1, q), v(1 - q, 1 - p, q), v(q, 1 - p, q), sideSprite));
                quads.add(quad(v(q, 1 - p, 1 - q), v(1 - q, 1 - p, 1 - q), v(1 - q, 1, 1 - q), v(q, 1, 1 - q), sideSprite));

                quads.add(quad(v(q, 1 - p, q), v(1 - q, 1 - p, q), v(1 - q, 1 - p, 1 - q), v(q, 1 - p, 1 - q), connectorSprite));
                quads.add(quad(v(q, 1, q), v(q, 1, 1 - q), v(1 - q, 1, 1 - q), v(1 - q, 1, q), sideSprite));
            } else {
                QuadSetting pattern = PipePatterns.findPattern(west, south, east, north);
                quads.add(quad(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (down == PIPE) {
                quads.add(quad(v(1 - o, o, o), v(1 - o, o, 1 - o), v(1 - o, 0, 1 - o), v(1 - o, 0, o), spritePipe));
                quads.add(quad(v(o, o, 1 - o), v(o, o, o), v(o, 0, o), v(o, 0, 1 - o), spritePipe));
                quads.add(quad(v(o, o, o), v(1 - o, o, o), v(1 - o, 0, o), v(o, 0, o), spritePipe));
                quads.add(quad(v(o, 0, 1 - o), v(1 - o, 0, 1 - o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spritePipe));
            } else if (down == BLOCK || down == INPUT || down == OUTPUT) {
                TextureAtlasSprite sideSprite = down == BLOCK ? spriteSide : down == INPUT ? spriteInputSide : spriteOutputSide;
                TextureAtlasSprite connectorSprite = down == BLOCK ? spriteConnector : down == INPUT ? spriteInputConnector : spriteOutputConnector;
                quads.add(quad(v(1 - o, o, o), v(1 - o, o, 1 - o), v(1 - o, p, 1 - o), v(1 - o, p, o), spritePipe));
                quads.add(quad(v(o, o, 1 - o), v(o, o, o), v(o, p, o), v(o, p, 1 - o), spritePipe));
                quads.add(quad(v(o, o, o), v(1 - o, o, o), v(1 - o, p, o), v(o, p, o), spritePipe));
                quads.add(quad(v(o, p, 1 - o), v(1 - o, p, 1 - o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spritePipe));

                quads.add(quad(v(1 - q, 0, q), v(1 - q, p, q), v(1 - q, p, 1 - q), v(1 - q, 0, 1 - q), sideSprite));
                quads.add(quad(v(q, 0, 1 - q), v(q, p, 1 - q), v(q, p, q), v(q, 0, q), sideSprite));
                quads.add(quad(v(q, p, q), v(1 - q, p, q), v(1 - q, 0, q), v(q, 0, q), sideSprite));
                quads.add(quad(v(q, 0, 1 - q), v(1 - q, 0, 1 - q), v(1 - q, p, 1 - q), v(q, p, 1 - q), sideSprite));

                quads.add(quad(v(q, p, 1 - q), v(1 - q, p, 1 - q), v(1 - q, p, q), v(q, p, q), connectorSprite));
                quads.add(quad(v(q, 0, 1 - q), v(q, 0, q), v(1 - q, 0, q), v(1 - q, 0, 1 - q), sideSprite));
            } else {
                QuadSetting pattern = PipePatterns.findPattern(west, north, east, south);
                quads.add(quad(v(o, o, o), v(1 - o, o, o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (east == PIPE) {
                quads.add(quad(v(1, 1 - o, 1 - o), v(1, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(1, o, o), v(1, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, o), spritePipe));
                quads.add(quad(v(1, 1 - o, o), v(1, o, o), v(1 - o, o, o), v(1 - o, 1 - o, o), spritePipe));
                quads.add(quad(v(1, o, 1 - o), v(1, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spritePipe));
            } else if (east == BLOCK || east == INPUT || east == OUTPUT) {
                TextureAtlasSprite sideSprite = east == BLOCK ? spriteSide : east == INPUT ? spriteInputSide : spriteOutputSide;
                TextureAtlasSprite connectorSprite = east == BLOCK ? spriteConnector : east == INPUT ? spriteInputConnector : spriteOutputConnector;
                quads.add(quad(v(1 - p, 1 - o, 1 - o), v(1 - p, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(1 - p, o, o), v(1 - p, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, o), spritePipe));
                quads.add(quad(v(1 - p, 1 - o, o), v(1 - p, o, o), v(1 - o, o, o), v(1 - o, 1 - o, o), spritePipe));
                quads.add(quad(v(1 - p, o, 1 - o), v(1 - p, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spritePipe));

                quads.add(quad(v(1 - p, 1 - q, 1 - q), v(1, 1 - q, 1 - q), v(1, 1 - q, q), v(1 - p, 1 - q, q), sideSprite));
                quads.add(quad(v(1 - p, q, q), v(1, q, q), v(1, q, 1 - q), v(1 - p, q, 1 - q), sideSprite));
                quads.add(quad(v(1 - p, 1 - q, q), v(1, 1 - q, q), v(1, q, q), v(1 - p, q, q), sideSprite));
                quads.add(quad(v(1 - p, q, 1 - q), v(1, q, 1 - q), v(1, 1 - q, 1 - q), v(1 - p, 1 - q, 1 - q), sideSprite));

                quads.add(quad(v(1 - p, q, 1 - q), v(1 - p, 1 - q, 1 - q), v(1 - p, 1 - q, q), v(1 - p, q, q), connectorSprite));
                quads.add(quad(v(1, q, 1 - q), v(1, q, q), v(1, 1 - q, q), v(1, 1 - q, 1 - q), sideSprite));
            } else {
                QuadSetting pattern = PipePatterns.findPattern(down, north, up, south);
                quads.add(quad(v(1 - o, o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (west == PIPE) {
                quads.add(quad(v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(0, 1 - o, o), v(0, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(o, o, o), v(o, o, 1 - o), v(0, o, 1 - o), v(0, o, o), spritePipe));
                quads.add(quad(v(o, 1 - o, o), v(o, o, o), v(0, o, o), v(0, 1 - o, o), spritePipe));
                quads.add(quad(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(0, 1 - o, 1 - o), v(0, o, 1 - o), spritePipe));
            } else if (west == BLOCK || west == INPUT || west == OUTPUT) {
                TextureAtlasSprite sideSprite = west == BLOCK ? spriteSide : west == INPUT ? spriteInputSide : spriteOutputSide;
                TextureAtlasSprite connectorSprite = west == BLOCK ? spriteConnector : west == INPUT ? spriteInputConnector : spriteOutputConnector;
                quads.add(quad(v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(p, 1 - o, o), v(p, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(o, o, o), v(o, o, 1 - o), v(p, o, 1 - o), v(p, o, o), spritePipe));
                quads.add(quad(v(o, 1 - o, o), v(o, o, o), v(p, o, o), v(p, 1 - o, o), spritePipe));
                quads.add(quad(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(p, 1 - o, 1 - o), v(p, o, 1 - o), spritePipe));

                quads.add(quad(v(0, 1 - q, 1 - q), v(p, 1 - q, 1 - q), v(p, 1 - q, q), v(0, 1 - q, q), sideSprite));
                quads.add(quad(v(0, q, q), v(p, q, q), v(p, q, 1 - q), v(0, q, 1 - q), sideSprite));
                quads.add(quad(v(0, 1 - q, q), v(p, 1 - q, q), v(p, q, q), v(0, q, q), sideSprite));
                quads.add(quad(v(0, q, 1 - q), v(p, q, 1 - q), v(p, 1 - q, 1 - q), v(0, 1 - q, 1 - q), sideSprite));

                quads.add(quad(v(p, q, q), v(p, 1 - q, q), v(p, 1 - q, 1 - q), v(p, q, 1 - q), connectorSprite));
                quads.add(quad(v(0, q, q), v(0, q, 1 - q), v(0, 1 - q, 1 - q), v(0, 1 - q, q), sideSprite));
            } else {
                QuadSetting pattern = PipePatterns.findPattern(down, south, up, north);
                quads.add(quad(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(o, o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (north == PIPE) {
                quads.add(quad(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 0), v(o, 1 - o, 0), spritePipe));
                quads.add(quad(v(o, o, 0), v(1 - o, o, 0), v(1 - o, o, o), v(o, o, o), spritePipe));
                quads.add(quad(v(1 - o, o, 0), v(1 - o, 1 - o, 0), v(1 - o, 1 - o, o), v(1 - o, o, o), spritePipe));
                quads.add(quad(v(o, o, o), v(o, 1 - o, o), v(o, 1 - o, 0), v(o, o, 0), spritePipe));
            } else if (north == BLOCK || north == INPUT || north == OUTPUT) {
                TextureAtlasSprite sideSprite = north == BLOCK ? spriteSide : north == INPUT ? spriteInputSide : spriteOutputSide;
                TextureAtlasSprite connectorSprite = north == BLOCK ? spriteConnector : north == INPUT ? spriteInputConnector : spriteOutputConnector;
                quads.add(quad(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, p), v(o, 1 - o, p), spritePipe));
                quads.add(quad(v(o, o, p), v(1 - o, o, p), v(1 - o, o, o), v(o, o, o), spritePipe));
                quads.add(quad(v(1 - o, o, p), v(1 - o, 1 - o, p), v(1 - o, 1 - o, o), v(1 - o, o, o), spritePipe));
                quads.add(quad(v(o, o, o), v(o, 1 - o, o), v(o, 1 - o, p), v(o, o, p), spritePipe));

                quads.add(quad(v(q, 1 - q, p), v(1 - q, 1 - q, p), v(1 - q, 1 - q, 0), v(q, 1 - q, 0), sideSprite));
                quads.add(quad(v(q, q, 0), v(1 - q, q, 0), v(1 - q, q, p), v(q, q, p), sideSprite));
                quads.add(quad(v(1 - q, q, 0), v(1 - q, 1 - q, 0), v(1 - q, 1 - q, p), v(1 - q, q, p), sideSprite));
                quads.add(quad(v(q, q, p), v(q, 1 - q, p), v(q, 1 - q, 0), v(q, q, 0), sideSprite));

                quads.add(quad(v(q, q, p), v(1 - q, q, p), v(1 - q, 1 - q, p), v(q, 1 - q, p), connectorSprite));
                quads.add(quad(v(q, q, 0), v(q, 1 - q, 0), v(1 - q, 1 - q, 0), v(1 - q, q, 0), sideSprite));
            } else {
                QuadSetting pattern = PipePatterns.findPattern(west, up, east, down);
                quads.add(quad(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, o, o), v(o, o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (south == PIPE) {
                quads.add(quad(v(o, 1 - o, 1), v(1 - o, 1 - o, 1), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, 1), v(o, o, 1), spritePipe));
                quads.add(quad(v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, 1), v(1 - o, o, 1), spritePipe));
                quads.add(quad(v(o, o, 1), v(o, 1 - o, 1), v(o, 1 - o, 1 - o), v(o, o, 1 - o), spritePipe));
            } else if (south == BLOCK || south == INPUT || south == OUTPUT) {
                TextureAtlasSprite sideSprite = south == BLOCK ? spriteSide : south == INPUT ? spriteInputSide : spriteOutputSide;
                TextureAtlasSprite connectorSprite = south == BLOCK ? spriteConnector : south == INPUT ? spriteInputConnector : spriteOutputConnector;
                quads.add(quad(v(o, 1 - o, 1 - p), v(1 - o, 1 - o, 1 - p), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spritePipe));
                quads.add(quad(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, 1 - p), v(o, o, 1 - p), spritePipe));
                quads.add(quad(v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - p), v(1 - o, o, 1 - p), spritePipe));
                quads.add(quad(v(o, o, 1 - p), v(o, 1 - o, 1 - p), v(o, 1 - o, 1 - o), v(o, o, 1 - o), spritePipe));

                quads.add(quad(v(q, 1 - q, 1), v(1 - q, 1 - q, 1), v(1 - q, 1 - q, 1 - p), v(q, 1 - q, 1 - p), sideSprite));
                quads.add(quad(v(q, q, 1 - p), v(1 - q, q, 1 - p), v(1 - q, q, 1), v(q, q, 1), sideSprite));
                quads.add(quad(v(1 - q, q, 1 - p), v(1 - q, 1 - q, 1 - p), v(1 - q, 1 - q, 1), v(1 - q, q, 1), sideSprite));
                quads.add(quad(v(q, q, 1), v(q, 1 - q, 1), v(q, 1 - q, 1 - p), v(q, q, 1 - p), sideSprite));

                quads.add(quad(v(q, 1 - q, 1 - p), v(1 - q, 1 - q, 1 - p), v(1 - q, q, 1 - p), v(q, q, 1 - p), connectorSprite));
                quads.add(quad(v(q, 1 - q, 1), v(q, q, 1), v(1 - q, q, 1), v(1 - q, 1 - q, 1), sideSprite));
            } else {
                QuadSetting pattern = PipePatterns.findPattern(west, down, east, up);
                quads.add(quad(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }
        }

        // Render the facade if we have one in addition to the pipe above. Note that the facade comes from the model data property
        // (FACADEID)
//        BlockState facadeId = extraData.get(P.FACADEID);
//        if (facadeId != null) {
//            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(facadeId);
//            ChunkRenderTypeSet renderTypes = model.getRenderTypes(facadeId, rand, extraData);
//            if (layer == null || renderTypes.contains(layer)) { // always render in the null layer or the block-breaking textures don't show up
//                try {
//                    quads.addAll(model.getQuads(state, side, rand, ModelData.EMPTY, layer));
//                } catch (Exception ignored) {
//                }
//            }
//        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    // Because we can potentially mimic other blocks we need to render on all render types
    @Override
    @Nonnull
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.all();
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return spriteNormalPipe == null
                ? Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply((withDefaultNamespace("missingno")))
                : spriteNormalPipe;
    }

    // To let our pipe/facade render correctly as an item (both in inventory and on the ground) we
    // get the correct transforms from the context
    @Nonnull
    @Override
    public ItemTransforms getTransforms() {
        return context.getTransforms();
    }

    @Nonnull
    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}
