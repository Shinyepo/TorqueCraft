package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.menu.furnace.AlloyFurnaceContainer;
import dev.shinyepo.torquecraft.menu.grinder.GrinderContainer;
import dev.shinyepo.torquecraft.menu.mechanicalfan.MechanicalFanContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, TorqueCraft.MODID);

    public static final Supplier<MenuType<GrinderContainer>> GRINDER_CONTAINER = MENU_TYPES.register("grinder",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new GrinderContainer(windowId, inv.player, data.readBlockPos(), FluidStack.OPTIONAL_STREAM_CODEC.decode(data))));


    public static final Supplier<MenuType<AlloyFurnaceContainer>> ALLOY_FURNACE_CONTAINER = MENU_TYPES.register("alloy_furnace",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new AlloyFurnaceContainer(windowId, inv.player, data.readBlockPos())));

    public static final Supplier<MenuType<MechanicalFanContainer>> MECHANICAL_FAN_CONTAINER = MENU_TYPES.register("mechanical_fan",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MechanicalFanContainer(windowId, inv.player, data.readBlockPos())));

}
