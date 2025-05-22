package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.MachineFactory;
import dev.shinyepo.torquecraft.factory.TorqueFluidTank;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.particle.TorqueParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class SprinklerEntity extends MachineFactory {
    private static final ClientConfig config = ClientConfig.SPRINKLER;
    private final int fluidCapacity = 16000;
    private final int usage = 5;
    private final TorqueFluidTank fluidTank = createFluidTank(fluidCapacity);

    private final AABB workingBoundary;
    private final List<BlockPos> workingArea = new ArrayList<>();
    private int nextTick = 20;

    public SprinklerEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.SPRINKLER_ENTITY.get(), pPos, pBlockState, config);

        workingBoundary = new AABB(new Vec3(pPos.getX() - 4, pPos.getY() - 1, pPos.getZ() - 4),
                new Vec3(pPos.getX() + 4, pPos.getY() - 5, pPos.getZ() + 4));
        calculateWorkingArea();
    }

    private void calculateWorkingArea() {
        BlockPos.betweenClosedStream(workingBoundary).forEach(x -> {
            if (x != this.worldPosition)
                workingArea.add(x.immutable());
        });
    }

    @Override
    public void configureSides(Direction facing) {
        configureSides(Direction.UP, SideType.INPUT);
    }

    public int getUsage() {
        return usage;
    }

    public void tick(Level level, BlockPos pos) {
        if (fluidTank.getFluidInTank(0).getAmount() >= usage) {
            if (level.getGameTime() % nextTick == 0) {
                for (BlockPos land : workingArea) {
                    BlockState targetState = level.getBlockState(land);
                    if (targetState.getBlock() instanceof FarmBlock) {
                        if (targetState.getValue(BlockStateProperties.MOISTURE) < 7)
                            level.setBlock(land, targetState.setValue(BlockStateProperties.MOISTURE, 7), 2);
                    }
                    if (targetState.getBlock() instanceof FireBlock) {
                        level.removeBlock(land, false);
                    }
                    if (targetState.getBlock() instanceof CropBlock crop) {
                        var age = crop.getAge(targetState) + 1;
                        var maxAge = crop.getMaxAge();

                        if (age > maxAge) {
                            continue;
                        }

                        level.setBlock(land, crop.getStateForAge(age), 2);
                    }
                }
                nextTick = getAgeTick();
            }
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, workingBoundary);
            if (!entities.isEmpty()) {
                for (LivingEntity livingEntity : entities) {
                    if (livingEntity.isOnFire())
                        livingEntity.extinguishFire();
                }
            }
            fluidTank.drain(usage, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public void animateTick(Level pLevel, BlockPos pPos) {
        if (this.fluidTank.getFluidAmount() < usage) return;
        if (level.getGameTime() % 4 != 0) return;
        Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.35, pPos.getZ() + 0.5);

//        ParticleUtils.spawnParticlesAlongAxis(Direction.Axis.X,pLevel,pPos,2,ParticleTypes.FALLING_WATER, UniformInt.of(5,10));
        for (int i = 0; i < 15; i++) {
            pLevel.addParticle(TorqueParticles.SPRINKLER_WATER.get(), center.x(), center.y(), center.z(), getPartialSpeed(), 0, getPartialSpeed());
        }
//        pLevel.addParticle(TorqueParticles.SPRINKLER_WATER.get(), center.x(), center.y(), center.z(), 0.0F, 0, getPartialSpeed());
//        pLevel.addParticle(TorqueParticles.SPRINKLER_WATER.get(), pPos.getX(), pPos.getY(), pPos.getZ(), 0, 0, 0.1F);
//        pLevel.addParticle(ParticleTypes.SMOKE, pPos.getX(), pPos.getY(), pPos.getZ(), -0.5F, 0, -0.1F);
    }

    private float getPartialSpeed() {
        return Mth.nextFloat(level.random, -0.15F, 0.15F);
    }

    private float getXSpeed() {
        return Mth.nextFloat(level.random, -0.15F, 0.6F);
    }

    private int getAgeTick() {
        return Mth.randomBetweenInclusive(level.random, 40, 55);
    }


    public IFluidHandler getFluidTank(Direction dir) {
        if (dir == null) return fluidTank;
        if (dir == Direction.UP) {
            return fluidTank;
        }
        return null;
    }

    public FluidStack getFluidStack() {
        return this.fluidTank.getFluid();
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidTank.setFluid(fluidStack);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!fluidTank.isEmpty()) {
            fluidTank.writeToNBT(provider, tag);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Fluid")) {
            fluidTank.readFromNBT(provider, tag);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        saveAdditional(nbt, provider);
        return nbt;
    }

}
