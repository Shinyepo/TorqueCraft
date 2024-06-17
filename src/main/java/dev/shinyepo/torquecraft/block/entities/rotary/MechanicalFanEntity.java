package dev.shinyepo.torquecraft.block.entities.rotary;

import com.google.common.collect.Lists;
import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryClient;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MechanicalFanEntity extends RotaryClient {
    private AABB workingBoundary;
    private final List<BlockPos> workingArea = Lists.newArrayList();
    private final List<BlockPos> validFarmlands = Lists.newArrayList();
    private static final ClientConfig config = ClientConfig.MECHANICAL_FAN;


    public MechanicalFanEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.MECHANICAL_FAN_ENTITY.get(), pPos, pBlockState, config);

        this.workingBoundary = getWorkingBoundary(pPos, pBlockState);
        BlockPos.betweenClosedStream(workingBoundary).forEach(x -> workingArea.add(x.immutable()));
    }

    private void calculateWorkingArea() {
        this.workingBoundary = getWorkingBoundary(this.getBlockPos(), this.getBlockState());
        BlockPos.betweenClosedStream(workingBoundary).forEach(x -> workingArea.add(x.immutable()));
    }

    private AABB getWorkingBoundary(BlockPos pPos, BlockState pState) {
        Direction facing = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int distance = (int) Math.ceil(4.5714 + (this.rotaryHandler.get().getAngular() * 3 / 112));
        switch (facing) {
            case NORTH -> {
                return new AABB(new Vec3(pPos.getX() - 1, pPos.getY() - 0.5, pPos.getZ() - 0.1),
                        new Vec3(pPos.getX() + 1.9, pPos.getY() + 2, pPos.getZ() - distance));
            }
            case EAST -> {
                return new AABB(new Vec3(pPos.getX() + 1, pPos.getY() - 0.5, pPos.getZ() - 1),
                        new Vec3(pPos.getX() + distance, pPos.getY() + 2, pPos.getZ() + 1.9));

            }
            case SOUTH -> {
                return new AABB(new Vec3(pPos.getX() - 1, pPos.getY() - 0.5, pPos.getZ() + 1),
                        new Vec3(pPos.getX() + 1.9, pPos.getY() + 2, pPos.getZ() + distance));
            }
            case WEST -> {
                return new AABB(new Vec3(pPos.getX(), pPos.getY() - 0.5, pPos.getZ() - 1),
                        new Vec3(pPos.getX() - distance, pPos.getY() + 2, pPos.getZ() + 1.9));
            }
            default -> {
                return null;
            }
        }
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

    public void tick(Level pLevel, BlockState pState) {
        if (this.rotaryHandler.get().getAngular() == 0) return;
        List<Entity> entities = pLevel.getEntities(null, workingBoundary);
        if (!entities.isEmpty()) {
            for (Entity en : entities) {
                if (!en.isShiftKeyDown()) {
                    Direction direction = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    en.hurtMarked = true;
                    en.setDeltaMovement(en.getDeltaMovement()
                            .add(0.15d * (direction.getStepX() * 1.5),
                                    0,
                                    0.15d * (direction.getStepZ() * 1.5)));
                }
            }
        }
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

    @Override
    public void setRotaryPower(float angular, float torque) {
        super.setRotaryPower(angular, torque);
        calculateWorkingArea();
    }
}
