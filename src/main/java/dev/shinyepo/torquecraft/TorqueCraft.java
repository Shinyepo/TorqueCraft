package dev.shinyepo.torquecraft;

import com.mojang.logging.LogUtils;
import dev.shinyepo.torquecraft.block.entities.renderers.*;
import dev.shinyepo.torquecraft.constants.TorqueStandaloneModels;
import dev.shinyepo.torquecraft.events.HoverEvent;
import dev.shinyepo.torquecraft.menu.furnace.AlloyFurnaceScreen;
import dev.shinyepo.torquecraft.menu.grinder.GrinderScreen;
import dev.shinyepo.torquecraft.menu.mechanicalfan.MechanicalFanScreen;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import dev.shinyepo.torquecraft.network.fluid.PressureFluidNetworkRegistry;
import dev.shinyepo.torquecraft.particle.sprinkler.SprinklerWaterProvider;
import dev.shinyepo.torquecraft.registries.TorqueCapabilities;
import dev.shinyepo.torquecraft.registries.TorqueCreativeTabs;
import dev.shinyepo.torquecraft.registries.TorqueMenus;
import dev.shinyepo.torquecraft.registries.block.TorqueBlockEntities;
import dev.shinyepo.torquecraft.registries.block.TorqueBlocks;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluidTypes;
import dev.shinyepo.torquecraft.registries.fluid.TorqueFluids;
import dev.shinyepo.torquecraft.registries.item.TorqueItems;
import dev.shinyepo.torquecraft.registries.networking.TorquePackets;
import dev.shinyepo.torquecraft.registries.particle.TorqueParticles;
import dev.shinyepo.torquecraft.registries.recipe.TorqueRecipes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelBaker;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Mod(TorqueCraft.MODID)
public class TorqueCraft {
    public static final String MODID = "torquecraft";
    public static final Logger logger = LogUtils.getLogger();

    public TorqueCraft(IEventBus modEventBus) {

        NeoForge.EVENT_BUS.addListener(this::serverStopped);

        modEventBus.addListener(this::commonSetup);

        TorqueBlocks.BLOCKS.register(modEventBus);
        TorqueBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TorqueItems.ITEMS.register(modEventBus);
        TorqueFluidTypes.FLUID_TYPES.register(modEventBus);
        TorqueFluids.FLUIDS.register(modEventBus);
        TorqueCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        TorqueMenus.MENU_TYPES.register(modEventBus);
        TorqueRecipes.Types.RECIPE_TYPES.register(modEventBus);
        TorqueRecipes.Serializers.RECIPE_SERIALIZERS.register(modEventBus);
        TorqueParticles.PARTICLE_TYPES.register(modEventBus);

        modEventBus.addListener(TorqueCapabilities::registerCapabilities);
        modEventBus.addListener(TorquePackets::registerPayloadHandler);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        RotaryNetworkRegistry.init();
        PressureFluidNetworkRegistry.init();
    }

    private void serverStopped(ServerStoppedEvent event) {
        RotaryNetworkRegistry.reset();
        PressureFluidNetworkRegistry.reset();
    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            NeoForge.EVENT_BUS.register(new HoverEvent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.SOURCE_LUBRICANT.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.FLOWING_LUBRICANT.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.SOURCE_JET_FUEL.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(TorqueFluids.FLOWING_JET_FUEL.get(), RenderType.translucent());
        }

        @SubscribeEvent
        public static void registerGeometryLoaders(ModelEvent.RegisterLoaders event) {
//            event.register(fromNamespaceAndPath(TorqueCraft.MODID, "pipe_loader"), new PipeModelLoader());
        }

        @SubscribeEvent
        public static void menuSetup(RegisterMenuScreensEvent e) {
            e.register(TorqueMenus.GRINDER_CONTAINER.get(), GrinderScreen::new);
            e.register(TorqueMenus.ALLOY_FURNACE_CONTAINER.get(), AlloyFurnaceScreen::new);
            e.register(TorqueMenus.MECHANICAL_FAN_CONTAINER.get(), MechanicalFanScreen::new);
        }

        @SubscribeEvent
        public static void registerBakedModels(ModelEvent.RegisterStandalone event) {
            event.register(TorqueStandaloneModels.FAN_BLADE, StandaloneModelBaker.blockStateModel());
            event.register(TorqueStandaloneModels.SHAFT_ROD, StandaloneModelBaker.blockStateModel());
            event.register(TorqueStandaloneModels.HSLA_SHAFT_ROD, StandaloneModelBaker.blockStateModel());
            event.register(TorqueStandaloneModels.HSLA_SHORT_SHAFT_ROD, StandaloneModelBaker.blockStateModel());
            event.register(TorqueStandaloneModels.SHORT_SHAFT_ROD, StandaloneModelBaker.blockStateModel());
            event.register(TorqueStandaloneModels.GRINDER_SHAFT, StandaloneModelBaker.blockStateModel());
            event.register(TorqueStandaloneModels.ROTARY_MONITOR, StandaloneModelBaker.blockStateModel());
        }

        @SubscribeEvent
        public static void initializeClient(RegisterClientExtensionsEvent e) {
            e.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return TorqueFluidTypes.WATER_STILL_RL;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return TorqueFluidTypes.WATER_FLOWING_RL;
                }

                @Override
                public @Nullable ResourceLocation getOverlayTexture() {
                    return TorqueFluidTypes.JET_FUEL_OVERLAY_RL;
                }

                @Override
                public int getTintColor() {
                    return 0xA1ADD8E6;
                }
            }, TorqueFluidTypes.JET_FUEL_TYPE.get());

            e.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return TorqueFluidTypes.WATER_STILL_RL;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return TorqueFluidTypes.WATER_FLOWING_RL;
                }

                @Override
                public @Nullable ResourceLocation getOverlayTexture() {
                    return TorqueFluidTypes.LUBRICANT_OVERLAY_RL;
                }

                @Override
                public int getTintColor() {
                    return 0xA1FFFF84;
                }
            }, TorqueFluidTypes.LUBRICANT_TYPE.get());
        }

        @SubscribeEvent
        public static void layersRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
//            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(TorqueCraft.MODID, "block/partial/shaft_rod"),"shaft"), ShaftRenderer::createSingleBodyLayer);
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(TorqueBlockEntities.SHAFT_ENTITY.get(), ShaftRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.STEAM_ENGINE_ENTITY.get(), SteamEngineRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.MECHANICAL_FAN_ENTITY.get(), MechanicalFanRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.GRINDER_ENTITY.get(), GrinderRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.THREE_WAY_ENTITY.get(), ThreeWayRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.BEVEL_GEARS_ENTITY.get(), BevelGearsRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.GEARBOX_ENTITY.get(), GearboxRenderer::new);
            event.registerBlockEntityRenderer(TorqueBlockEntities.VACUUM_ENTITY.get(), VacuumRenderer::new);
        }

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(TorqueParticles.SPRINKLER_WATER.get(), SprinklerWaterProvider::new);
        }
    }
}
