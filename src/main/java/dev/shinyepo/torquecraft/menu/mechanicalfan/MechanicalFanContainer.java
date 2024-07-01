package dev.shinyepo.torquecraft.menu.mechanicalfan;

import dev.shinyepo.torquecraft.block.entities.rotary.MechanicalFanEntity;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.IRotaryHandler;
import dev.shinyepo.torquecraft.menu.ContainerBase;
import dev.shinyepo.torquecraft.registries.TorqueMenus;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MechanicalFanContainer extends ContainerBase {
    private final MechanicalFanEntity mfEntity;

    public MechanicalFanContainer(int windowId, Player player, BlockPos pos) {
        super(TorqueMenus.MECHANICAL_FAN_CONTAINER.get(), windowId, pos, 1, 1, TorqueBlocks.MECHANICAL_FAN.get());
        this.mfEntity = (MechanicalFanEntity) player.level().getBlockEntity(pos);
        layoutPlayerInventorySlots(player.getInventory(), 8, 84);
    }

    protected IRotaryHandler getRotaryHandler() {
        return mfEntity.getRotaryHandler(null);
    }

    protected BlockPos getBlockPos() {
        return mfEntity.getBlockPos();
    }

    protected void changeMode(int mode) {
        mfEntity.changeMode(mode);
    }

    protected MechanicalFanEntity.FanMode getMode() {
        return mfEntity.getMode();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem())
            return slot.getItem();
        return null;
    }
}
