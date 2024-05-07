package dev.shinyepo.torquecraft.factory;

import dev.shinyepo.torquecraft.handlers.AdaptedItemHandler;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncFluidS2C;
import dev.shinyepo.torquecraft.utils.TorqueFluidTank;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class MachineFactory extends BlockEntity {
    public static final String ITEMS_INPUT_TAG = "Input";
    public static final String ITEMS_OUTPUT_TAG = "Output";

    private TorqueFluidTank fluidTank;

    public static List<TagKey<Item>> validInputs = List.of();
    private ItemStackHandler inputItems;
    private ItemStackHandler outputItems;

    private Lazy<CombinedInvWrapper> itemHandler;
    private Lazy<IItemHandler> inputItemHandler;
    private Lazy<IItemHandler> outputItemHandler;

    private static List<TagKey<Item>> validFluidSlotInputs = List.of();

    private Lazy<ItemStackHandler> tankDrainItems;

    public int progress = 0;
    public int maxProgress = 64;

    public MachineFactory(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
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

    public void setValidInputs(List<TagKey<Item>> tags) {
        validInputs = tags;
    }

    public void setValidFluidSlotInputs(List<TagKey<Item>> tags) {
        validFluidSlotInputs = tags;
    }

    public Lazy<CombinedInvWrapper> createItemHandler (int inputSlots, int outputSlots) {
        inputItems = createInputItemHandler(inputSlots);
        outputItems = createOutputItemHandler(outputSlots);
        inputItemHandler = createInputItemHandler();
        outputItemHandler = createOutputItemHandler();
        return itemHandler = Lazy.of(() -> new CombinedInvWrapper(inputItems,outputItems));
    }

    public Lazy<ItemStackHandler> createDrainHandler (int drainSlot) {
        return tankDrainItems = createDrainTankHandler(drainSlot);
    }

    public Lazy<IItemHandler> createInputItemHandler(){
        return Lazy.of(() -> new AdaptedItemHandler(inputItems) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        });
    }

    public Lazy<IItemHandler> createOutputItemHandler(){
        return Lazy.of(() -> new AdaptedItemHandler(outputItems) {
            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }
        });
    }

    @Nonnull
    public ItemStackHandler createInputItemHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (!validInputs.isEmpty()) {
                    return stack.getTags().anyMatch((tag) -> validInputs.contains(tag));
                }
                return false;
            }
        };
    }

    @Nonnull
    public ItemStackHandler createOutputItemHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }


            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 1) {
                if (!validFluidSlotInputs.isEmpty()) {
                    return stack.getTags().anyMatch(tag -> validFluidSlotInputs.contains(tag));
                }
                }
                return false;
            }
        };
    }

    @Nonnull
    public TorqueFluidTank createFluidTank(int capacity) {
        return fluidTank = new TorqueFluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    TorqueMessages.sendToAllPlayers(new SyncFluidS2C(worldPosition, this.fluid));
                }
            }
        };
    }

    public Lazy<ItemStackHandler> createDrainTankHandler(int slots) {
        return Lazy.of(() -> new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (!validFluidSlotInputs.isEmpty()) {
                    return stack.getTags().anyMatch(tag -> validFluidSlotInputs.contains(tag));
                }
                return false;
            }
        });
    }

    public TorqueFluidTank getFluidTank() { return fluidTank; }
    //GUI
    public ItemStackHandler getInputItems() {
        return inputItems;
    }
    //GUI
    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    public Lazy<CombinedInvWrapper> getItemHandler() {
        return itemHandler;
    }

    public Lazy<IItemHandler> getInputItemHandler() {
        return inputItemHandler;
    }

    public Lazy<IItemHandler> getOutputItemHandler() { return outputItemHandler; }


    public ItemStackHandler getTankDrainItems() {
        return tankDrainItems.get();
    }
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (fluidTank != null) {
            fluidTank.writeToNBT(provider,tag);
        }
        tag.put(ITEMS_INPUT_TAG, inputItems.serializeNBT(provider));
        tag.put(ITEMS_OUTPUT_TAG, outputItems.serializeNBT(provider));
        tag.putInt("grinder.progress", progress);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag,provider);
        if (fluidTank != null) {
            fluidTank.readFromNBT(provider,tag);
        }
        if (tag.contains(ITEMS_INPUT_TAG)) {
            inputItems.deserializeNBT(provider, tag.getCompound(ITEMS_INPUT_TAG));
        }
        if (tag.contains(ITEMS_OUTPUT_TAG)) {
            outputItems.deserializeNBT(provider, tag.getCompound(ITEMS_OUTPUT_TAG));
        }
        tag.getInt("grinder.progress");
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
        SimpleContainer inventory = new SimpleContainer(itemHandler.get().getSlots());
        for (int i = 0; i < itemHandler.get().getSlots(); i++) {
            inventory.setItem(i, itemHandler.get().getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}
