package dev.shinyepo.torquecraft.block.prefab.rotary;

import com.mojang.serialization.MapCodec;
import dev.shinyepo.torquecraft.block.entities.rotary.ShaftEntity;
import dev.shinyepo.torquecraft.capabilities.TorqueCustomCapabilities;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.factory.rotary.render.IRotaryIO;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Shaft extends HorizontalDirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public Shaft(Properties pProperties) {
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
        return new ShaftEntity(pPos, pState);
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
            if (pBlockEntity instanceof ShaftEntity sE) {
                sE.tick(pLevel1, pPos, pState1);

            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        player.displayClientMessage(Component.literal("State Face: " + state.getValue(FACING) + ", Hit Face: " + hit.getDirection()), false);
        IRotaryHandler handler = level.getCapability(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK, pos.relative(state.getValue(FACING).getOpposite()), state.getValue(FACING));
        if (handler != null) {
            player.displayClientMessage(Component.literal("POWER: " + handler.getPower()), false);
        }
        return InteractionResult.SUCCESS;
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
            if (blockEntity instanceof RotaryTransmitter rotary) {
                pLevel.removeBlockEntity(pPos);
                rotary.removeTransmitter();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
