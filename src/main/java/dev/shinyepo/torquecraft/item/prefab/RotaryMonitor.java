package dev.shinyepo.torquecraft.item.prefab;

import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ShaftEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotaryMonitor extends Item {
    public RotaryMonitor(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        //Install monitor
        BlockEntity entity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        if (entity instanceof ShaftEntity shaft && pContext.getPlayer().isShiftKeyDown()) {
            Direction facing = pContext.getLevel().getBlockState(pContext.getClickedPos()).getValue(BlockStateProperties.HORIZONTAL_FACING);
            Direction clickedFace = pContext.getClickedFace();
            if (clickedFace != facing && clickedFace != facing.getOpposite()) {
                boolean res = shaft.installMonitor(clickedFace);
                if (res) {
                    pContext.getItemInHand().consume(1, null);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }


}
