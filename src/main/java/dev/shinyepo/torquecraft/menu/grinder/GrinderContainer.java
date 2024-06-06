package dev.shinyepo.torquecraft.menu.grinder;

import dev.shinyepo.torquecraft.block.entities.rotary.GrinderEntity;
import dev.shinyepo.torquecraft.menu.ContainerBase;
import dev.shinyepo.torquecraft.registries.TorqueMenus;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class GrinderContainer extends ContainerBase {
    private final GrinderEntity grinderEntity;
    private FluidStack fluidStack;

    public GrinderContainer(int windowId, Player player, BlockPos pos, FluidStack fluidStack) {
        this(windowId, player, pos, fluidStack, new SimpleContainerData(2));
    }


    public GrinderContainer(int windowId, Player player, BlockPos pos, FluidStack fluidStack, ContainerData data) {
        super(TorqueMenus.GRINDER_CONTAINER.get(), windowId, pos, GrinderEntity.SLOT_COUNT, 1, TorqueBlocks.GRINDER.get());
        this.grinderEntity = ((GrinderEntity) player.level().getBlockEntity(pos));
        this.fluidStack = fluidStack;


        if (player.level().getBlockEntity(pos) instanceof GrinderEntity grinder) {
            addSlot(new SlotItemHandler(grinder.getInputItems(), GrinderEntity.SLOT_INPUT, 56, 34));
            addSlot(new SlotItemHandler(grinder.getOutputItems(), 0, 110, 34));
            addSlot(new SlotItemHandler(grinder.getTankDrainItems(), 0, 133, 58));

            addDataSlots(data);
            layoutPlayerInventorySlots(player.getInventory(), 8, 84);
        }
    }

    public GrinderEntity getBlockEntity() {
        return this.grinderEntity;
    }

    public FluidStack getFluidStack() {
        return this.fluidStack;
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }
}
