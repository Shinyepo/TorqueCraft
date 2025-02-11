package dev.shinyepo.torquecraft.block.entities.rotary;

import com.google.common.collect.Lists;
import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.IModeMachine;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryClient;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class MechanicalFanEntity extends RotaryClient implements IModeMachine {
    private AABB workingBoundary;
    private final List<BlockPos> workingArea = Lists.newArrayList();
    private final List<BlockPos> validFarmlands = Lists.newArrayList();
    private static final ClientConfig config = ClientConfig.MECHANICAL_FAN;
    private AABB cutArea;
    private FanMode mode = FanMode.PUSH;
    private BlockPos targetPos;
    private Lazy<ItemStackHandler> fanSlot = Lazy.of(() -> new ItemStackHandler(1));


    public MechanicalFanEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.MECHANICAL_FAN_ENTITY.get(), pPos, pBlockState, config);


        this.workingBoundary = getBoundary(pPos, pBlockState, 0, false);

        this.cutArea = getBoundary(pPos, pBlockState, 0.5F, false);
        BlockPos.betweenClosedStream(workingBoundary).forEach(x -> workingArea.add(x.immutable()));
    }

    private void calculateWorkingArea() {
        int distance = (int) Math.ceil(4.5714 + (this.rotaryHandler.get().getAngular() * 3 / 112));
        this.workingBoundary = getBoundary(this.getBlockPos(), this.getBlockState(), distance, true);
        BlockPos.betweenClosedStream(workingBoundary).forEach(x -> workingArea.add(x.immutable()));
    }

    private AABB getBoundary(BlockPos pPos, BlockState pState, float distance, boolean calcTargetPos) {
        Direction facing = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch (facing) {
            case NORTH -> {
                if (calcTargetPos)
                    this.targetPos = getBlockPos().north((int) distance).above(1);
                return new AABB(new Vec3(pPos.getX() - 1, pPos.getY() - 0.5, pPos.getZ() - 0.1),
                        new Vec3(pPos.getX() + 1.9, pPos.getY() + 2, pPos.getZ() - distance));
            }
            case EAST -> {
                if (calcTargetPos)
                    this.targetPos = getBlockPos().east((int) distance).above(1);
                return new AABB(new Vec3(pPos.getX() + 1, pPos.getY() - 0.5, pPos.getZ() - 1),
                        new Vec3(pPos.getX() + distance, pPos.getY() + 2, pPos.getZ() + 1.9));

            }
            case SOUTH -> {
                if (calcTargetPos)
                    this.targetPos = getBlockPos().south((int) distance).above(1);
                return new AABB(new Vec3(pPos.getX() - 1, pPos.getY() - 0.5, pPos.getZ() + 1),
                        new Vec3(pPos.getX() + 1.9, pPos.getY() + 2, pPos.getZ() + distance));
            }
            case WEST -> {
                if (calcTargetPos)
                    this.targetPos = getBlockPos().west((int) distance).above(1);
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
                    en.hurtMarked = true;
                    Vec3 fanCenter = getBlockPos().getCenter();
                    Vec3 targetForce;
                    if (mode == FanMode.PUSH)
                        targetForce = new Vec3(fanCenter.x, fanCenter.y, fanCenter.z).vectorTo(Vec3.atCenterOf(targetPos)).multiply(0.02F, 0.03F, 0.02F);
                    else
                        targetForce = new Vec3(en.position().x, en.position().y, en.position().z).vectorTo(Vec3.atCenterOf(getBlockPos())).multiply(0.04F, 0.03F, 0.04F);
                    en.setDeltaMovement(en.getDeltaMovement()
                            .add(targetForce));
                }
            }
        }
        List<LivingEntity> livingEntities = pLevel.getEntitiesOfClass(LivingEntity.class, cutArea);
        if (!livingEntities.isEmpty()) {
            for (LivingEntity en : livingEntities) {
                //TODO: Calc dmg based on RotarySpeed
                //TODO: Make endermans stay and get hurt! >:)
                en.hurt(pLevel.damageSources().generic(), 2F);
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

    @Override
    public void cycleMode() {
        mode = mode.getNext();
    }

    public void changeMode(int mode) {
        this.mode = FanMode.values()[mode];
        setChanged();
    }

    public FanMode getMode() {
        return mode;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(TorqueNBT.MODE, mode.ordinal());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.MODE))
            mode = FanMode.values()[tag.getInt(TorqueNBT.MODE)];
    }

    public IItemHandler getFanSlotHandler() {
        return fanSlot.get();
    }

    public enum FanMode {
        PUSH,
        PULL;

        private static final FanMode[] VALUES = values();

        public FanMode getNext() {
            return VALUES[(this.ordinal() + 1) % VALUES.length];
        }
    }
}
