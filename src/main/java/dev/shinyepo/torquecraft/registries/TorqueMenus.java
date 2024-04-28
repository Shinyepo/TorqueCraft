package dev.shinyepo.torquecraft.registries;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.menu.GrinderContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TorqueMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, TorqueCraft.MODID);

    public static final Supplier<MenuType<GrinderContainer>> GRINDER_CONTAINER = MENU_TYPES.register("grinder",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new GrinderContainer(windowId, inv.player, data.readBlockPos())));

}
