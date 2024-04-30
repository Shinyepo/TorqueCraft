package dev.shinyepo.torquecraft;

import dev.shinyepo.torquecraft.menu.GrinderScreen;
import dev.shinyepo.torquecraft.recipes.TorqueRecipes;
import dev.shinyepo.torquecraft.registries.*;
import dev.shinyepo.torquecraft.registries.networking.TorquePackets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(TorqueCraft.MODID)
public class    TorqueCraft
{
    public static final String MODID = "torquecraft";

    public TorqueCraft(IEventBus modEventBus)
    {
        TorqueBlocks.BLOCKS.register(modEventBus);
        TorqueBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TorqueItems.ITEMS.register(modEventBus);
        TorqueCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        TorqueMenus.MENU_TYPES.register(modEventBus);
        TorqueRecipes.Types.RECIPE_WRITING.register(modEventBus);
        TorqueRecipes.Serializers.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(TorqueCapabilities::registerCapabilities);
        modEventBus.addListener(TorquePackets::registerPayloadHandler);
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void menuSetup(RegisterMenuScreensEvent e) {
            e.register(TorqueMenus.GRINDER_CONTAINER.get(), GrinderScreen::new);
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
//            event.registerBlockEntityRenderer(TorqueBlockEntities.GRINDER_ENTITY.get(), GrinderEntityRenderer::new);
        }
    }
}
