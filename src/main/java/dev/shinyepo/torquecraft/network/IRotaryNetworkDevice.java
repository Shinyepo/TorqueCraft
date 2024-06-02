package dev.shinyepo.torquecraft.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public interface IRotaryNetworkDevice {
    UUID network_id = null;

    UUID getNetworkId();

    BlockState getBlockState();

    BlockPos getBlockPos();

    void updateNetwork(RotaryNetwork network);
}
