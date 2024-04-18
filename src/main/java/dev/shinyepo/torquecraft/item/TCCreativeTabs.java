package dev.shinyepo.torquecraft.item;

import dev.shinyepo.torquecraft.TorqueCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TCCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TorqueCraft.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GENERAL = CREATIVE_MODE_TABS.register("torquecraft", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup."+ TorqueCraft.MODID)) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> TCItems.TUNGSTEN_INGOT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(TCItems.TUNGSTEN_INGOT.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(TCItems.TUNGSTEN_BLOCK_ITEM.get());
            }).build());
}
