package dev.shinyepo.torquecraft.network;

import dev.shinyepo.torquecraft.capabilities.handlers.RotaryHandler;
import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryClient;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RotaryNetwork {
    private final UUID network_id;
    private final List<IRotaryNetworkDevice> devices = new ArrayList<>();
    private final Map<BlockPos, RotarySource> sources = new Object2ObjectOpenHashMap<>();
    private final Map<BlockPos, RotaryTransmitter> transmitters = new Object2ObjectOpenHashMap<>();
    private Map<BlockPos, RotaryClient> clients = new Object2ObjectOpenHashMap<>();

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

    public void registerClient(RotaryClient rotaryClient) {
        if (!validDevice(rotaryClient)) return;
        clients.put(rotaryClient.getBlockPos(), rotaryClient);
        updateNetwork();
        devices.add(rotaryClient);
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

    public void removeClient(RotaryClient rotaryClient) {
        this.clients.remove(rotaryClient.getBlockPos());
        this.updateNetwork();
        this.devices.remove(rotaryClient);
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
