package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.IWrenchInteraction;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotaryClient extends RotaryNetworkDevice<ClientConfig> implements IWrenchInteraction {
    private final ClientConfig clientConfig;

    public RotaryClient(BlockEntityType<?> type, BlockPos pos, BlockState blockState, ClientConfig config) {
        super(type, pos, blockState, config);
        this.clientConfig = config;

        if (type != TorqueBlockEntities.CENTRIFUGE_ENTITY.get())
            configureSides(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    public void configureSides(Direction facing) {
        configureSides(facing.getOpposite(), SideType.INPUT);
    }
}
