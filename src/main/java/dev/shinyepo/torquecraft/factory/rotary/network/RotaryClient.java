package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotaryClient extends RotaryNetworkDevice<ClientConfig> {
    private final ClientConfig clientConfig;

    public RotaryClient(BlockEntityType<?> type, BlockPos pos, BlockState blockState, ClientConfig config) {
        super(type, pos, blockState, config);
        this.clientConfig = config;
        configureSides(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite(),SideType.INPUT);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level == null || this.level.isClientSide()) return;
        updateNetwork(RotaryNetworkRegistry.getInstance().registerClient(this));
    }

    public void removeClient() {
        if (this.level != null && !this.level.isClientSide) {
            RotaryNetworkRegistry.getInstance().removeClient(this.getNetworkId(), this);
        }
    }
}
