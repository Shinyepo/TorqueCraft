package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ShaftEntity extends RotaryTransmitter {
    private boolean hasMonitor = false;
    private Direction monitorFacing;
    public ShaftEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.SHAFT_ENTITY.get(), pPos, pBlockState, TransmitterConfig.SHAFT);
    }

    public boolean installMonitor(Direction monitorFacing) {
        if (!hasMonitor) {
            hasMonitor = true;
            this.monitorFacing = monitorFacing;
            setChanged();
            return true;
        }
        return false;
    }

    public boolean hasMonitor() {
        return hasMonitor;
    }

    public Direction getMonitorFacing() {
        return monitorFacing;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putBoolean(TorqueNBT.MONITOR, hasMonitor);
        if (monitorFacing != null) {
            tag.putString(TorqueNBT.MONITOR_FACING, monitorFacing.toString());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        hasMonitor = tag.getBoolean(TorqueNBT.MONITOR).get();
        if (tag.contains(TorqueNBT.MONITOR_FACING)) {
            monitorFacing = Direction.byName(tag.getString(TorqueNBT.MONITOR_FACING).get());
        }
    }

    public void dropMonitor() {
        Container inventory = new SimpleContainer(1);
        inventory.setItem(0, new ItemStack(TorqueItems.ROTARY_MONITOR.get()));
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}
