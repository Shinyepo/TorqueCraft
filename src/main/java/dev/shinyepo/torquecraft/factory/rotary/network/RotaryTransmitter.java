package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.IWrenchInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotaryTransmitter extends RotaryNetworkDevice<TransmitterConfig> implements IWrenchInteraction {
    private final TransmitterConfig transmitterConfig;


    public RotaryTransmitter(BlockEntityType<?> type, BlockPos pos, BlockState blockState, TransmitterConfig config) {
        super(type, pos, blockState, config);
        this.transmitterConfig = config;
        configureSides(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    public void configureSides(Direction facing) {
        switch (transmitterConfig) {
            case SHAFT -> {
                configureSides(facing, SideType.OUTPUT);
                configureSides(facing.getOpposite(), SideType.INPUT);
            }
            case THREE_WAY -> {
                configureSides(facing, SideType.OUTPUT);
                configureSides(facing.getClockWise(), SideType.INPUT);
                configureSides(facing.getOpposite(), SideType.INPUT);
            }
            case BEVEL_GEARS -> {
                configureSides(facing.getOpposite(), SideType.INPUT);
                configureSides(Direction.UP, SideType.OUTPUT);
            }
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.rotaryHandler.get().getAngular() > transmitterConfig.getAngular() || this.rotaryHandler.get().getTorque() > transmitterConfig.getTorque()) {
            //Init meltdown
        }
    }
}
