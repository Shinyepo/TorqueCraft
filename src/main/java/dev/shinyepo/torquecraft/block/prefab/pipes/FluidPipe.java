package dev.shinyepo.torquecraft.block.prefab.pipes;

import dev.shinyepo.torquecraft.block.entities.pipes.FluidPipeEntity;
import dev.shinyepo.torquecraft.model.baker.helpers.PipeConnection;
import dev.shinyepo.torquecraft.factory.pipe.PipeBlock;
import dev.shinyepo.torquecraft.utils.PipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class FluidPipe extends PipeBlock implements SimpleWaterloggedBlock, EntityBlock {
    public FluidPipe() {
        super(Properties.of()
                .strength(1.0f)
                .sound(SoundType.METAL)
                .noOcclusion()
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPipeEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        } else {
            return (lvl, pos, st, be) -> {
                if (be instanceof FluidPipeEntity pipe) {
                    pipe.tickServer();
                }
            };
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof FluidPipeEntity pipe) {
            pipe.markDirty();
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.CONSUME;

        Direction clickedDir = calcDirection(hit.getLocation(), pos);
        if (clickedDir != null) {
            BlockPos rel = pos.relative(clickedDir);
            BlockState sideBlock = level.getBlockState(rel);
            if (sideBlock.getBlock() instanceof PipeBlock) return InteractionResult.PASS;
            EnumProperty<PipeConnection> prop = PipeUtil.getProp(clickedDir);
            PipeConnection currCon = state.getValue(prop);
            PipeConnection val = PipeConnection.BLOCK;

            if (currCon == PipeConnection.BLOCK) {
                val = PipeConnection.INPUT;
            } else if (currCon == PipeConnection.INPUT) {
                val = PipeConnection.OUTPUT;
            }
            level.setBlock(pos, state.setValue(prop, val),3);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    private Direction calcDirection(Vec3 loc, BlockPos pos) {
        Vec3 rel = loc.subtract(pos.getX(), pos.getY(), pos.getZ());
        double x = rel.x;
        double y = rel.y;
        double z = rel.z;

        if (x > .3 && y < .3) {
            return Direction.DOWN;
        } else if (x > .3 && y > .7) {
            return Direction.UP;
        } else if ((y > .3 && y < .7) && x < .3) {
            return Direction.WEST;
        } else if ((y > .3 && y < .7) && x > .7) {
            return Direction.EAST;
        } else if ((y > .3 && y < .7) && z < .3) {
            return Direction.NORTH;
        } else if ((y > .3 && y < .7) && z > .7) {
            return Direction.SOUTH;
        }
        return null;
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof FluidPipeEntity pipe) {
            pipe.markDirty();
        }
        BlockState blockState = calculateState(level, pos, state);
        if (state != blockState) {
            level.setBlockAndUpdate(pos, blockState);
        }
    }
}
