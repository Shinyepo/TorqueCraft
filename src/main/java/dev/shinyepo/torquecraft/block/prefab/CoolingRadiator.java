package dev.shinyepo.torquecraft.block.prefab;

import com.mojang.serialization.MapCodec;
import dev.shinyepo.torquecraft.config.UsageConfig;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CoolingRadiator extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<UsageConfig> USAGE = TorqueAttributes.USAGE;
    public static final VoxelShape[] SHAPE = new VoxelShape[] {
            Block.box(0,0,0,16,1,16),
            Block.box(0,0,0,16,5,16),
            Block.box(0,0,0,16,9,16),
            Block.box(0,0,0,16,13,16),
            Block.box(0,0,0,16,16,16)
    };

    public CoolingRadiator(Properties pProperties) {
        super(pProperties);

        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(USAGE, UsageConfig.FULL));
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE[pState.getValue(USAGE).ordinal()];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(USAGE);
    }

    public EnumProperty<UsageConfig> getUsage() {
        return USAGE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS_NO_ITEM_USED;
        BlockState newState = state.setValue(USAGE,state.getValue(USAGE).getNext());
        level.setBlockAndUpdate(pos, newState);
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection())
                .setValue(USAGE, UsageConfig.FULL);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

}
