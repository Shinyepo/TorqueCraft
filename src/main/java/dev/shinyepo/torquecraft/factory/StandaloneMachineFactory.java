package dev.shinyepo.torquecraft.factory;

import dev.shinyepo.torquecraft.capabilities.handlers.AdaptedItemHandler;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class StandaloneMachineFactory extends BlockEntity {
    public int progress = 0;
    public int maxProgress = 64;
    protected int burnTime = 0;
    protected int maxBurnTime = 0;
    private Map<String, ItemStackHandler> registeredHandlers;

    public StandaloneMachineFactory(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected void registerHandlers(Map<String, ItemStackHandler> handlers) {
        registeredHandlers = handlers;
    }

    public void setMaxProgress(int progress) {
        maxProgress = progress;
    }

    protected void resetProgress() {
        this.progress = 0;
    }

    protected boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    protected void increaseCraftingProgress() {
        this.progress++;
    }

    public Lazy<CombinedInvWrapper> createCombinedHandler(IItemHandlerModifiable... handlers) {
        return Lazy.of(() -> new CombinedInvWrapper(handlers));
    }

    //C
    public <T extends IItemHandlerModifiable> Lazy<AdaptedItemHandler> createAutoInputHandler(T handler) {
        return Lazy.of(() -> new AdaptedItemHandler(handler) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        });
    }

    public <T extends IItemHandlerModifiable> Lazy<AdaptedItemHandler> createAutoOutputHandler(T handler) {
        return Lazy.of(() -> new AdaptedItemHandler(handler) {
            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }
        });
    }

    @Nonnull
    public Lazy<ItemStackHandler> createInputItemHandler(int slots, List<TagKey<Item>> validItems) {
        return Lazy.of(() -> new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (!validItems.isEmpty()) {
                    return stack.getTags().anyMatch(validItems::contains);
                }
                return false;
            }
        });
    }

    @Nonnull
    public Lazy<ItemStackHandler> createOutputItemHandler(int slots) {
        return Lazy.of(() -> new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }


            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return false;
            }
        });
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!registeredHandlers.isEmpty()) {
            registeredHandlers.forEach((key, handler) -> tag.put(key, handler.serializeNBT(provider)));
        }
        tag.putInt(TorqueNBT.PROGRESS, progress);
        tag.putInt(TorqueNBT.BURN_TIME, burnTime);
        tag.putInt(TorqueNBT.MAX_BURN_TIME, maxBurnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.BURN_TIME)) {
            this.burnTime = tag.getInt(TorqueNBT.BURN_TIME);
        }
        if (tag.contains(TorqueNBT.MAX_BURN_TIME)) {
            this.maxBurnTime = tag.getInt(TorqueNBT.MAX_BURN_TIME);
        }
        if (tag.contains(TorqueNBT.PROGRESS)) {
            this.progress = tag.getInt(TorqueNBT.PROGRESS);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        saveAdditional(nbt, provider);
        return nbt;
    }

    public void drops() {
        if (!registeredHandlers.isEmpty()) {
            registeredHandlers.forEach((key, handler) -> {
                SimpleContainer tempInventory = new SimpleContainer(handler.getSlots());
                for (int i = 0; i < tempInventory.getContainerSize(); i++) {
                    tempInventory.setItem(i, handler.getStackInSlot(i));
                }
                Containers.dropContents(this.getLevel(), getBlockPos(), tempInventory);
            });
        }
    }
}
