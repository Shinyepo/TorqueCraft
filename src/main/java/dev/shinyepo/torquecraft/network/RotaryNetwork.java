package dev.shinyepo.torquecraft.network;

import dev.shinyepo.torquecraft.capabilities.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.factory.rotary.RotarySource;
import dev.shinyepo.torquecraft.factory.rotary.RotaryTransmitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RotaryNetwork {
    private final UUID network_id;
    private List<IRotaryNetworkDevice> devices = new ArrayList<>();
    private Map<BlockPos, RotarySource> sources = new Object2ObjectOpenHashMap<>();
    private Map<BlockPos, RotaryTransmitter> transmitters = new Object2ObjectOpenHashMap<>();

    private final RotaryHandler rotaryHandler = new RotaryHandler(0, 0);

    public RotaryNetwork(UUID id) {
        network_id = id;
    }

    public UUID getNetworkId() {
        return this.network_id;
    }

    public void registerSource(RotarySource source) {
        if (!validDevice(source)) return;
        sources.put(source.getBlockPos(), source);

        var engine = SourceConfig.STEAM_ENGINE;
        this.rotaryHandler.setMaxAngular(engine.getAngular());
        this.rotaryHandler.setMaxTorque(engine.getTorque());
        this.updateNetwork();

        devices.add(source);
    }

    public boolean validDevice(IRotaryNetworkDevice device) {
        return !devices.contains(device);
    }

    public void registerTransmitter(RotaryTransmitter transmitter) {
        if (!validDevice(transmitter)) return;
        transmitters.put(transmitter.getBlockPos(), transmitter);
        updateNetwork();
        devices.add(transmitter);
    }

    public List<IRotaryNetworkDevice> getDevices() {
        return devices;
    }

    public Map<BlockPos, RotarySource> getSources() {
        return this.sources;
    }

    public void removeSource(RotarySource source) {
        sources.remove(source.getBlockPos());
        this.rotaryHandler.setMaxAngular(0);
        this.rotaryHandler.setMaxTorque(0);
        this.updateNetwork();
        devices.remove(source);
    }

    public void emitPower(float angular, float torque) {
            this.rotaryHandler.setAngular(angular);
            this.rotaryHandler.setTorque(torque);
            this.updateNetwork();
    }

    private void updateNetwork() {
        if(!transmitters.isEmpty()) {
            transmitters.forEach((pos, transmitter) -> {
                transmitter.setRotaryPower(this.rotaryHandler.getAngular(), this.rotaryHandler.getTorque());
            });
        }
    }


    public void removeTransmitter(RotaryTransmitter rotaryTransmitter) {
        transmitters.remove(rotaryTransmitter.getBlockPos());
        this.updateNetwork();
        devices.remove(rotaryTransmitter);
    }

    public void clear() {
        this.transmitters.clear();
        this.sources.clear();
        this.devices.clear();
    }
}
