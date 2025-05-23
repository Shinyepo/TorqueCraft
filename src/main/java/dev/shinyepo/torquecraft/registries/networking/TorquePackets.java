package dev.shinyepo.torquecraft.registries.networking;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.networking.packets.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class TorquePackets {
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(TorqueCraft.MODID)
                .versioned("1.0")
                .optional();

        //TO CLIENT
        registrar.playToClient(SyncFluidS2C.TYPE, SyncFluidS2C.STREAM_CODEC,SyncFluidS2C::handler);
        registrar.playToClient(SyncRotaryPowerS2C.TYPE, SyncRotaryPowerS2C.STREAM_CODEC, SyncRotaryPowerS2C::handler);
        registrar.playToClient(SyncMeltdownS2C.TYPE, SyncMeltdownS2C.STREAM_CODEC, SyncMeltdownS2C::handler);
        registrar.playToClient(SyncGearboxRotaryPowerS2C.TYPE, SyncGearboxRotaryPowerS2C.STREAM_CODEC, SyncGearboxRotaryPowerS2C::handler);
        //TO SERVER
        registrar.playToServer(ChangeModeC2S.TYPE, ChangeModeC2S.STREAM_CODEC, ChangeModeC2S::handler);
    }
}
