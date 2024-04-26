package dev.shinyepo.torquecraft.block.entities;

import com.google.common.collect.Lists;
import dev.shinyepo.torquecraft.registries.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class MechanicalFanEntity extends BlockEntity {
    private final List<BlockPos> workingArea = Lists.newArrayList();
    private final List<BlockPos> validFarmlands = Lists.newArrayList();


    public MechanicalFanEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.MECHANICAL_FAN_ENTITY.get(), pPos, pBlockState);

        var boundary = getBoundaries(pPos, pBlockState);
        BlockPos.betweenClosed(boundary.get(0), boundary.get(1)).forEach(x-> {
            workingArea.add(x.immutable());
        });
    }

    private List<BlockPos> getBoundaries(BlockPos pPos, BlockState pState) {
        List<BlockPos> result = Lists.newArrayList();

        Direction facing = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch (facing) {
            case NORTH -> {
                result.add(new BlockPos(pPos.getX()-1, pPos.getY(), pPos.getZ()-1));
                result.add(new BlockPos(pPos.getX()+1, pPos.getY()+2, pPos.getZ()-9));
                return result;
            }
            case EAST -> {
                result.add(new BlockPos(pPos.getX()+1, pPos.getY(), pPos.getZ()-1));
                result.add(new BlockPos(pPos.getX()+9, pPos.getY()+2, pPos.getZ()+1));
                return result;
            }
            case SOUTH -> {
                result.add(new BlockPos(pPos.getX()-1, pPos.getY(), pPos.getZ()+1));
                result.add(new BlockPos(pPos.getX()+1, pPos.getY()+2, pPos.getZ()+9));
                return result;
            }
            case WEST -> {
                result.add(new BlockPos(pPos.getX()-1, pPos.getY(), pPos.getZ()-1));
                result.add(new BlockPos(pPos.getX()-9, pPos.getY()+2, pPos.getZ()+1));
                return result;
            }
        }
        return result;
    }

    private void getFarmlands(Level pLevel) {
        if (!workingArea.isEmpty()) {
            validFarmlands.clear();
            for (BlockPos bPos : workingArea) {
                BlockState bState = pLevel.getBlockState(bPos.below());
                Block block = bState.getBlock();
                if (block instanceof FarmBlock) {
                    validFarmlands.add(bPos.below());
                }
            }
        }
    }

    public void tick(Level pLevel) {
        if (pLevel.getGameTime() % 20 == 0) {
            getFarmlands(pLevel);
            if (!validFarmlands.isEmpty()) {
                for (BlockPos fPos : validFarmlands) {
                    BlockState cState = pLevel.getBlockState(fPos.above());
                    Block cBlock = cState.getBlock();
                    Block fBlock = pLevel.getBlockState(fPos).getBlock();

                    if (cBlock instanceof CropBlock && ((CropBlock) cBlock).isMaxAge(cState)) {
                        pLevel.destroyBlock(fPos.above(), true);
                        if (pLevel.getBlockState(fPos.above()).isAir() && fBlock instanceof FarmBlock) {
                            pLevel.setBlockAndUpdate(fPos.above(), cBlock.defaultBlockState());
                            pLevel.playSound(
                                    null,
                                    fPos.getX(),
                                    fPos.getY(),
                                    fPos.getZ(),
                                    SoundEvents.CROP_PLANTED,
                                    SoundSource.BLOCKS,
                                    1.0F,
                                    1.0F
                            );
                        }
                    }
                }
            }
        }
    }
}
