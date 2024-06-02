package dev.shinyepo.torquecraft.item.prefab;

import dev.shinyepo.torquecraft.config.RotaryMode;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.IModeMachine;
import dev.shinyepo.torquecraft.factory.IWrenchInteraction;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.factory.rotary.render.AnimatedEntity;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotaryWrench extends Item {
    public RotaryWrench(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide()) {
            var entity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
            if (entity instanceof AnimatedEntity animated)
                animated.setProgress(1.5F);
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }


        //Rotate machine
        BlockEntity entity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        if (entity instanceof IWrenchInteraction device && pContext.getPlayer().isShiftKeyDown()) {
            BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos());
            Direction newFace = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();

            var networkDevice = (RotaryNetworkDevice<?>) entity;

            RotaryNetworkRegistry.getInstance().unregisterDevice(networkDevice.getNetworkId(), networkDevice);

            device.resetSides();
            device.configureSides(newFace);
            pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), state.setValue(BlockStateProperties.HORIZONTAL_FACING, newFace));

            networkDevice.updateNetwork(RotaryNetworkRegistry.getInstance().registerDevice(networkDevice));
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }

        //Change machine mode
        if (entity instanceof IModeMachine) {
            BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos());
            RotaryMode oldValue = state.getValue(TorqueAttributes.MODE);
            pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), state.setValue(TorqueAttributes.MODE, oldValue.getNext()));

            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        return InteractionResult.PASS;
    }
}
