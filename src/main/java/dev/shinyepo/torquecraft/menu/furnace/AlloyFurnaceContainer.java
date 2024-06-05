package dev.shinyepo.torquecraft.menu.furnace;

import dev.shinyepo.torquecraft.block.entities.AlloyFurnaceEntity;
import dev.shinyepo.torquecraft.registries.TorqueMenus;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AlloyFurnaceContainer extends AbstractContainerMenu {
    private final BlockPos pos;
    private final int SLOT_COUNT;
    public final ContainerData data;

    public AlloyFurnaceContainer(int windowId, Player player, BlockPos pos) {
        this(windowId, player, pos, new SimpleContainerData(4));
    }

    public AlloyFurnaceContainer(int windowId, Player player, BlockPos pos, ContainerData data) {
        super(TorqueMenus.ALLOY_FURNACE_CONTAINER.get(), windowId);
        this.pos = pos;
        this.SLOT_COUNT = AlloyFurnaceEntity.SLOT_COUNT;
        this.data = data;

        if (player.level().getBlockEntity(pos) instanceof AlloyFurnaceEntity alloyFurnace) {
            addSlot(new SlotItemHandler(alloyFurnace.getFuelHandler(), 0, 8, 63));
            addItemSlotRange(alloyFurnace.getAddon(), 1, 3, 35, 18);
            addItemSlotRange(alloyFurnace.getInput(), 3, 3, 62, 18);
            addItemSlotRange(alloyFurnace.getOutput(), 1, 3, 143, 18);

            addDataSlots(data);
            layoutPlayerInventorySlots(player.getInventory(), 8, 84);
        }
    }

    private void addItemSlotRange(IItemHandler handler, int row, int column, int x, int y) {
        int index = 0;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                addSlot(new SlotItemHandler(handler, index, x + (y * j), y + (y * i)));
                index++;
            }
        }
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
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
            if (pIndex < SLOT_COUNT + 1) {
                if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!this.moveItemStackTo(stack, 0, 13, false)) {
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
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), pos), pPlayer, TorqueBlocks.ALLOY_FURNACE.get());
    }
}
