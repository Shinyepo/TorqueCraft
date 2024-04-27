package dev.shinyepo.torquecraft.block.prefab;

import com.mojang.serialization.MapCodec;
import dev.shinyepo.torquecraft.attributes.MachineAttributes;
import dev.shinyepo.torquecraft.block.entities.SteamEngineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SteamEngine extends HorizontalDirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPERATIONAL = MachineAttributes.OPERATIONAL;
    private static final VoxelShape SHAPE = Block.box(0,0,0,16,4,16);

    public SteamEngine(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPERATIONAL, false));

    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, OPERATIONAL);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        boolean isNetherrack = pLevel.getBlockState(pPos.below()).is(Blocks.NETHERRACK);
        boolean isPowered = pState.getValue(OPERATIONAL);
        if (isPowered != isNetherrack) {
            if (isPowered) {
                pLevel.scheduleTick(pPos, this, 4);
            } else {
                pLevel.setBlock(pPos, pState.cycle(OPERATIONAL), 2);
            }

        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getPlayer().isShiftKeyDown() ? pContext.getHorizontalDirection() : pContext.getHorizontalDirection().getOpposite())
                .setValue(OPERATIONAL, pContext.getLevel().getBlockState(pContext.getClickedPos().below()).is(Blocks.NETHERRACK));
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(OPERATIONAL) && !pLevel.getBlockState(pPos.below()).is(Blocks.NETHERRACK)) {
            pLevel.setBlock(pPos, pState.cycle(OPERATIONAL), 2);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
//        if (pHit.getDirection()) {return InteractionResult.CONSUME;}
        System.out.println(pLevel.getBlockState(pPos.below()).is(Blocks.NETHERRACK));
//        System.out.println("we've been hit "+ pState.getValue(LIT));
//        System.out.println("Facing - " + pState.getValue(FACING));
//        System.out.println("Signal North - " + pState.getSignal(pLevel, pPos, Direction.NORTH));
//        pLevel.setBlock(pPos,pState.cycle(LIT),2);
//        for(Direction direction : Direction.values()) {
//            System.out.println("Updating neighbor blocks");
//            pLevel.updateNeighborsAt(pPos.relative(direction), this);
//            pLevel.updateNeighborsAt(pPos, this);
//        }

        //playground

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SteamEngineEntity) {
                pLevel.removeBlockEntity(pPos);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pBlockEntity instanceof SteamEngineEntity sEE) {
                sEE.tick(pLevel1, pPos, pState1);
            }
        };


//        return createTickerHelper(pBlockEntityType, TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SteamEngineEntity(pPos, pState);
    }
}
