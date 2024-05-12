package dev.shinyepo.torquecraft;

import dev.shinyepo.torquecraft.block.entities.renderers.ShaftRenderer;
import dev.shinyepo.torquecraft.block.entities.renderers.SteamEngineRenderer;
import dev.shinyepo.torquecraft.events.HoverEvent;
import dev.shinyepo.torquecraft.model.baker.helpers.PipeModelLoader;
import dev.shinyepo.torquecraft.menu.GrinderScreen;
import dev.shinyepo.torquecraft.registries.*;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluidTypes;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import dev.shinyepo.torquecraft.registries.networking.TorquePackets;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(TorqueCraft.MODID)
public class    TorqueCraft
{
    public static final String MODID = "torquecraft";

    public TorqueCraft(IEventBus modEventBus)
    {
        TorqueBlocks.BLOCKS.register(modEventBus);
        TorqueBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TorqueItems.ITEMS.register(modEventBus);
        TorqueFluidTypes.FLUID_TYPES.register(modEventBus);
        TorqueFluids.FLUIDS.register(modEventBus);
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
        public static void onClientSetup(FMLClientSetupEvent event) {
            NeoForge.EVENT_BUS.register(new HoverEvent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.SOURCE_LUBRICANT.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.FLOWING_LUBRICANT.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.SOURCE_JET_FUEL.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.FLOWING_JET_FUEL.get(), RenderType.translucent());
        }

        @SubscribeEvent
        public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register(new ResourceLocation(TorqueCraft.MODID, "pipe_loader"), new PipeModelLoader());
        }
        @SubscribeEvent
        public static void menuSetup(RegisterMenuScreensEvent e) {
            e.register(TorqueMenus.GRINDER_CONTAINER.get(), GrinderScreen::new);
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(TorqueBlockEntities.SHAFT_ENTITY.get(), ShaftRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), SteamEngineRenderer::new);
        }
    }
}
