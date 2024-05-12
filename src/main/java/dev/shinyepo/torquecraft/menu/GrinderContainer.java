package dev.shinyepo.torquecraft.menu;

import dev.shinyepo.torquecraft.block.entities.rotary.GrinderEntity;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.TorqueMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class GrinderContainer extends AbstractContainerMenu {
    private final BlockPos pos;
    private final int SLOT_COUNT;
    private final int SLOT_INPUT;
    private final GrinderEntity grinderEntity;
    private FluidStack fluidStack;


    public GrinderContainer(int windowId, Player player, BlockPos pos, FluidStack fluidStack) {
        super(TorqueMenus.GRINDER_CONTAINER.get(), windowId);
        this.pos = pos;
        this.SLOT_INPUT = GrinderEntity.SLOT_INPUT;
        this.SLOT_COUNT = GrinderEntity.SLOT_COUNT;
        this.grinderEntity = ((GrinderEntity) player.level().getBlockEntity(pos));
        this.fluidStack = fluidStack;


        if (player.level().getBlockEntity(pos) instanceof GrinderEntity grinder) {
            addSlot(new SlotItemHandler(grinder.getInputItems(), GrinderEntity.SLOT_INPUT, 56, 34));
            addSlot(new SlotItemHandler(grinder.getOutputItems(), 0, 110, 34));
            addSlot(new SlotItemHandler(grinder.getTankDrainItems(), 0, 133, 58));
            addDataSlot(new DataSlot()
            {
                @Override
                public int get() {
                    return grinderEntity.progress;
                }

                @Override
                public void set(int value) {
                    grinderEntity.progress = value;
                }
            });

            addDataSlot(new DataSlot()
            {
                @Override
                public int get() {
                    return grinderEntity.maxProgress;
                }

                @Override
                public void set(int value) {
                    grinderEntity.maxProgress = value;
                }
            });
            layoutPlayerInventorySlots(player.getInventory(), 8, 84);
        }
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (pIndex < SLOT_COUNT) {
                if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!this.moveItemStackTo(stack, SLOT_INPUT, SLOT_INPUT+1, false)) {
                if (pIndex < 27 + SLOT_COUNT) {
                    if (!this.moveItemStackTo(stack, 27 + SLOT_COUNT, 36 + SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < Inventory.INVENTORY_SIZE + SLOT_COUNT && !this.moveItemStackTo(stack, SLOT_COUNT, 27 + SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), pos), pPlayer, TorqueBlocks.GRINDER.get());
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
