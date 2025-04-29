package dev.shinyepo.torquecraft.block.entities.rotary;

import dev.shinyepo.torquecraft.capabilities.handlers.AdaptedItemHandler;
import dev.shinyepo.torquecraft.config.ClientConfig;
import dev.shinyepo.torquecraft.constants.TorqueNBT;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryClient;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VacuumEntity extends RotaryClient {
    private static final ClientConfig config = ClientConfig.VACUUM;
    private final Lazy<ItemStackHandler> itemHandler = Lazy.of(() -> new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    });
    private final Lazy<AdaptedItemHandler> adaptedItemHandler = Lazy.of(() -> new AdaptedItemHandler(itemHandler.get()) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    });
    private final Lazy<AdaptedItemHandler> jadePreviewHandler = Lazy.of(() -> new AdaptedItemHandler(itemHandler.get()) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    private AABB workingBoundary;
    private AABB succArea;
    private BlockCapabilityCache<IItemHandler, Direction> capCache;


    public VacuumEntity(BlockPos pPos, BlockState pBlockState) {
        super(TorqueBlockEntities.VACUUM_ENTITY.get(), pPos, pBlockState, config);

        //TODO: Calc workingBoundary based on rotary power provided
        this.workingBoundary = getWorkingBoundary(pPos);
        Vec3 center = pPos.getCenter();

        this.succArea = new AABB(new Vec3(center.x() - 1F, center.y() - 1.5F, center.z() - 1F),
                new Vec3(center.x() + 1F, center.y() + 1F, center.z() + 1F));
    }

    private AABB getWorkingBoundary(BlockPos pPos) {
//        int distance = (int) Math.ceil(4.5714 + (this.rotaryHandler.get().getAngular() * 3 / 112));
        Vec3 center = pPos.getCenter();

        return new AABB(new Vec3(center.x() + 4.5F, center.y() + 4.5F, center.z() + 4.5F),
                new Vec3(center.x() - 4.5F, center.y() - 4.5F, center.z() - 4.5F));
    }


    public IItemHandler getItemHandler(Direction dir) {
        if (dir == Direction.DOWN)
            return adaptedItemHandler.get();
        return jadePreviewHandler.get();
    }

    public void tick(Level pLevel, BlockState pState) {
        //TODO: Should we push items ourselves or let "pipes" pull them?
//        if (!(pLevel.getBlockState(getBlockPos().below()).getBlock() instanceof HopperBlock)) {
//            var handler = capCache.getCapability();
//            if (handler != null && pLevel.getGameTime() % 4 == 0) {
//
//                var slots = itemHandler.get().getSlots();
//                for (int i = 0; i < slots; i++) {
//                    var item = itemHandler.get().getStackInSlot(i);
//                    if (item.isEmpty()) continue;
//                    var targetSlots = handler.getSlots();
//                    for (int j = 0; j < targetSlots; j++) {
//                        var inSlot = handler.getStackInSlot(j);
//                        if (inSlot.isEmpty() || (inSlot.is(item.getItem()) && inSlot.getCount() < inSlot.getMaxStackSize())) {
//                            //TODO: Amount dependant on machine power?
//                            var amount = Math.min(4, item.getCount());
//                            handler.insertItem(j, itemHandler.get().extractItem(i, amount, false), false);
//                            break;
//                        }
//                    }
//                    if (!itemHandler.get().getStackInSlot(i).isEmpty()) break;
//                }
//            }
//        }
        if (this.rotaryHandler.get().getAngular() < config.getMinAngular() && this.rotaryHandler.get().getTorque() < config.getMinTorque())
            return;
        List<Entity> entities = pLevel.getEntities(null, workingBoundary);
        if (!entities.isEmpty()) {
            for (Entity en : entities) {
                if (en instanceof LivingEntity) continue;
                if (!en.isShiftKeyDown()) {
                    en.hurtMarked = true;
                    en.position();
                    Vec3 vacuumCenter = getBlockPos().getCenter();
                    Vec3 toVacuum = new Vec3(vacuumCenter.x, vacuumCenter.y, vacuumCenter.z).vectorTo(en.position()).reverse().multiply(0.03F, 0.03F, 0.03F);
                    en.setDeltaMovement(en.getDeltaMovement()
                            .add(toVacuum));
                }
            }
        }
        List<Entity> entitiesToPickup = pLevel.getEntities(null, succArea);
        if (!entities.isEmpty()) {
            for (Entity en : entitiesToPickup) {
                if (en instanceof LivingEntity) return;
                if (en instanceof ItemEntity item) {
                    suckItemIn(item);
                }
            }
        }
    }

    private void suckItemIn(ItemEntity item) {
        int slots = itemHandler.get().getSlots();
        for (int i = 0; i < slots; i++) {
            if (isSlotValid(i, item.getItem().getItem())) {
                var result = itemHandler.get().insertItem(i, item.getItem(), false);
                if (result.isEmpty()) {
                    item.setItem(ItemStack.EMPTY);
                    item.discard();
                    return;
                } else {
                    item.setItem(result);
                }
            }
        }
    }

    private boolean isSlotValid(int slot, Item item) {
        return itemHandler.get().getStackInSlot(slot).isEmpty() || (itemHandler.get().getStackInSlot(slot).is(item) && itemHandler.get().getStackInSlot(slot).getCount() < itemHandler.get().getStackInSlot(slot).getMaxStackSize());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level == null || level.isClientSide()) return;

        this.capCache = BlockCapabilityCache.create(
                Capabilities.ItemHandler.BLOCK,
                (ServerLevel) level,
                getBlockPos().below(),
                Direction.DOWN
        );
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        var serializedInv = itemHandler.get().serializeNBT(provider);
        tag.put(TorqueNBT.INVENTORY, serializedInv);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TorqueNBT.INVENTORY))
            itemHandler.get().deserializeNBT(provider, tag.getCompound(TorqueNBT.INVENTORY).get());
    }

    @Override
    public void setRotaryPower(float angular, float torque) {
        super.setRotaryPower(angular, torque);
        this.workingBoundary = getWorkingBoundary(getBlockPos());
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
        SimpleContainer tempInventory = new SimpleContainer(itemHandler.get().getSlots());
        for (int i = 0; i < tempInventory.getContainerSize(); i++) {
            tempInventory.setItem(i, itemHandler.get().getStackInSlot(i));
        }
        Containers.dropContents(this.getLevel(), getBlockPos(), tempInventory);
    }
}
