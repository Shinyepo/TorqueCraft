package dev.shinyepo.torquecraft.block.prefab.rotary;

import com.mojang.serialization.MapCodec;
import dev.shinyepo.torquecraft.block.entities.rotary.SteamEngineEntity;
import dev.shinyepo.torquecraft.capabilities.TorqueCustomCapabilities;
import dev.shinyepo.torquecraft.capabilities.handlers.IRotaryHandler;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.rotary.IRotaryIO;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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
    public static final BooleanProperty OPERATIONAL = TorqueAttributes.OPERATIONAL;
    private static final VoxelShape SHAPE = Block.box(0,0,0,16,11,16);

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
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (entity instanceof RotarySource rotaryEntity) {
            IRotaryHandler handler = pLevel.getCapability(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK,pPos, pState, rotaryEntity, pHit.getDirection());
            if (handler != null) {
                if (pLevel.isClientSide()) {
                    pPlayer.displayClientMessage(Component.literal("ANGULAR: " + handler.getAngular() + ", TORQUE: " + handler.getTorque() + ", POWER: "+ handler.getPower()),false);
                } else {
                    pPlayer.displayClientMessage(Component.literal("ANGULAR: " + handler.getAngular() + ", TORQUE: " + handler.getTorque() + ", POWER: "+ handler.getPower()),true);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof RotarySource source) {
                pLevel.removeBlockEntity(pPos);
                source.removeSource();

            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return (pLevel1, pPos, pState1, pBlockEntity) -> {
                if (pBlockEntity instanceof IRotaryIO rotary) {
                    rotary.renderTick();
                }
            };
        };
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pBlockEntity instanceof SteamEngineEntity sEE) {
                sEE.tick(pLevel1, pPos, pState1);
            }
        };
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pPlacer == Minecraft.getInstance().player) {
            if (pLevel.getBlockEntity(pPos) instanceof RotarySource rotary) {
                rotary.setProgress(0F);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SteamEngineEntity(pPos, pState);
    }
}
