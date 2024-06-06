package dev.shinyepo.torquecraft.menu.furnace;

import dev.shinyepo.torquecraft.block.entities.AlloyFurnaceEntity;
import dev.shinyepo.torquecraft.menu.ContainerBase;
import dev.shinyepo.torquecraft.registries.TorqueMenus;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AlloyFurnaceContainer extends ContainerBase {
    public final ContainerData data;

    public AlloyFurnaceContainer(int windowId, Player player, BlockPos pos) {
        this(windowId, player, pos, new SimpleContainerData(4));
    }

    public AlloyFurnaceContainer(int windowId, Player player, BlockPos pos, ContainerData data) {
        super(TorqueMenus.ALLOY_FURNACE_CONTAINER.get(), windowId, pos, AlloyFurnaceEntity.SLOT_COUNT, 13, TorqueBlocks.ALLOY_FURNACE.get());
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
}
