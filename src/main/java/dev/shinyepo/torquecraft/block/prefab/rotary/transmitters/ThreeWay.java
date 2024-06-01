package dev.shinyepo.torquecraft.block.prefab.rotary.transmitters;

import com.mojang.serialization.MapCodec;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ThreeWayEntity;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.factory.rotary.render.IRotaryIO;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ThreeWay extends HorizontalDirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public ThreeWay(Properties pProperties) {
        super(pProperties);

        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, (pContext.getPlayer().isShiftKeyDown() ? pContext.getHorizontalDirection() : pContext.getHorizontalDirection().getOpposite()));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ThreeWayEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return (pLevel1, pPos, pState1, pBlockEntity) -> {
                if (pBlockEntity instanceof IRotaryIO rotary) {
                    rotary.renderTick();
                }
            };
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pBlockEntity instanceof RotaryTransmitter rT) {
                rT.tick(pLevel1, pPos, pState1);

            }
        };
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof RotaryTransmitter rIO) {
                rIO.setProgress(0F);
            }
        }
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof RotaryNetworkDevice<?> rotary) {
                pLevel.removeBlockEntity(pPos);
                rotary.removeDevice();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
