package dev.shinyepo.torquecraft.block.entities.rotary.transmitters;

import dev.shinyepo.torquecraft.config.GearboxRatio;
import dev.shinyepo.torquecraft.config.RotaryMode;
import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.IModeMachine;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GearboxEntity extends RotaryTransmitter implements IModeMachine {
    private final GearboxRatio RATIO;
    private float inAngular;
    private float inTorque;
    private boolean updateGearbox = false;

    public GearboxEntity(BlockEntityType<?> blockEntityType, BlockPos pPos, BlockState pBlockState, GearboxRatio ratio) {
        super(blockEntityType, pPos, pBlockState, TransmitterConfig.GEARBOX);
        RATIO = ratio;
    }

    public int getRatio() {
        return RATIO.getRatio();
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        if (getNetwork() != null && updateGearbox) {
            getNetwork().adjustOutput(this, inAngular, inTorque);
            updateGearbox = false;
        }
    }

    @Override
    public void cycleMode() {
        BlockState state = getBlockState();
        RotaryMode oldValue = state.getValue(TorqueAttributes.MODE);

        getLevel().setBlockAndUpdate(getBlockPos(), state.setValue(TorqueAttributes.MODE, oldValue.getNext()));
        forceUpdateGearbox();
    }

    protected void forceUpdateGearbox() {
        updateGearbox = true;
    }

    public void backupRotaryValues(float angular, float torque) {
        inAngular = angular;
        inTorque = torque;
    }
}
