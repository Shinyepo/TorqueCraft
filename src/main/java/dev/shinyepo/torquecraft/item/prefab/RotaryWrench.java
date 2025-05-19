package dev.shinyepo.torquecraft.item.prefab;

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
        BlockEntity entity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());

        if (pContext.getLevel().isClientSide()) {
            if (entity instanceof AnimatedEntity animated) {
                animated.setProgress(0F);
            }
            return InteractionResult.SUCCESS;
        } else {
            //Rotate machine
            if (entity instanceof IWrenchInteraction device && pContext.getPlayer().isShiftKeyDown()) {
                BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos());
                Direction newFace = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();

                var networkDevice = (RotaryNetworkDevice<?>) entity;

                RotaryNetworkRegistry.getInstance().unregisterDevice(networkDevice.getNetworkId(), networkDevice);

                device.resetSides();
                device.configureSides(newFace);
                networkDevice.invalidateCapabilities();
                pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), state.setValue(BlockStateProperties.HORIZONTAL_FACING, newFace));

                networkDevice.updateNetwork(RotaryNetworkRegistry.getInstance().registerDevice(networkDevice));
                return InteractionResult.SUCCESS_NO_ITEM_USED;
            }

            //Change machine mode
            if (entity instanceof IModeMachine modeMachine) {
                modeMachine.cycleMode();
                return InteractionResult.SUCCESS_NO_ITEM_USED;
            }
        }
        return InteractionResult.PASS;
    }
}
