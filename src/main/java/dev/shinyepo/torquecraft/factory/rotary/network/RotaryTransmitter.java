package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RotaryTransmitter extends RotaryNetworkDevice<TransmitterConfig> {
    private final TransmitterConfig transmitterConfig;

    public RotaryTransmitter(BlockEntityType<?> type, BlockPos pos, BlockState blockState, TransmitterConfig config) {
        super(type, pos, blockState, config);
        this.transmitterConfig = config;
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
