package dev.shinyepo.torquecraft.block.prefab;

import com.mojang.serialization.MapCodec;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CanolaCrop extends CropBlock {
    public static final MapCodec<CanolaCrop> CODEC = simpleCodec(CanolaCrop::new);
    public static final int MAX_AGE = 5;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
    public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0,0,0,16,3,16),
            Block.box(0,0,0,16,5,16),
            Block.box(0,0,0,16,11,16),
            Block.box(0,0,0,16,15,16),
            Block.box(0,0,0,16,15,16),
            Block.box(0,0,0,16,15,16),
    };

    public CanolaCrop(Properties properties) {
        super(properties);
    }

    public @NotNull MapCodec<CanolaCrop> codec() { return CODEC; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[pState.getValue(AGE)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return TorqueItems.CANOLA_SEEDS.get();
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }
}
