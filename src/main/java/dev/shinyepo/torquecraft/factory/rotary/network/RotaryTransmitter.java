package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotaryTransmitter extends RotaryNetworkDevice<TransmitterConfig> {
    private final TransmitterConfig transmitterConfig;


    public RotaryTransmitter(BlockEntityType<?> type, BlockPos pos, BlockState blockState, TransmitterConfig config) {
        super(type, pos, blockState, config);
        this.transmitterConfig = config;
        configureTransmitterSides(config, blockState);
    }

    public void configureTransmitterSides(TransmitterConfig config, BlockState state) {
        switch (config) {
            case SHAFT -> {
                configureSides(state.getValue(BlockStateProperties.HORIZONTAL_FACING), SideType.OUTPUT);
                configureSides(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite(), SideType.INPUT);
            }
            case JUNCTION -> {
                configureSides(state.getValue(BlockStateProperties.HORIZONTAL_FACING), SideType.OUTPUT);
                configureSides(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise(), SideType.OUTPUT);
                configureSides(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite(), SideType.INPUT);
            }
            case BEVEL -> {
                configureSides(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite(), SideType.INPUT);
                configureSides(Direction.UP, SideType.OUTPUT);
            }
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.rotaryHandler.get().getAngular() > transmitterConfig.getAngular() || this.rotaryHandler.get().getTorque() > transmitterConfig.getTorque()) {
            //Init meltdown
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level == null || this.level.isClientSide()) return;
        Direction direction = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        updateNetwork(RotaryNetworkRegistry.getInstance().registerTransmitter(this, direction));
    }

    public void removeTransmitter() {
        if (this.level != null && !this.level.isClientSide) {
            RotaryNetworkRegistry.getInstance().removeTransmitter(this.getNetworkId(), this);
        }
    }
}
