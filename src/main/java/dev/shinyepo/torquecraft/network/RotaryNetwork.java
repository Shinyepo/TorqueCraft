package dev.shinyepo.torquecraft.network;

import dev.shinyepo.torquecraft.capabilities.handlers.rotary.RotaryHandler;
import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryClient;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
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
    private final List<RotaryNetworkDevice<?>> devices = new ArrayList<>();
    private final Map<BlockPos, RotarySource> sources = new Object2ObjectOpenHashMap<>();
    private final Map<BlockPos, RotaryTransmitter> transmitters = new Object2ObjectOpenHashMap<>();
    private final Map<BlockPos, RotaryClient> clients = new Object2ObjectOpenHashMap<>();

    private final RotaryHandler rotaryHandler = new RotaryHandler(0, 0);

    public RotaryNetwork(UUID id) {
        network_id = id;
    }

    public UUID getNetworkId() {
        return this.network_id;
    }

    private void increaseMax(SourceConfig config) {
        if (this.sources.size() > 1) {
            this.rotaryHandler.setMaxAngular(this.rotaryHandler.getMaxAngular() + config.getAngular());
            this.rotaryHandler.setMaxTorque(this.rotaryHandler.getMaxTorque() + config.getTorque());
        } else {
            this.rotaryHandler.setMaxAngular(config.getAngular());
            this.rotaryHandler.setMaxTorque(config.getTorque());
        }
    }

    private void reduceMax(SourceConfig config) {
        if (sources.size() > 1) {
            this.rotaryHandler.setMaxAngular(this.rotaryHandler.getMaxAngular() - config.getAngular());
            this.rotaryHandler.setMaxTorque(this.rotaryHandler.getMaxTorque() - config.getTorque());
        } else {
            this.rotaryHandler.setMaxAngular(0);
            this.rotaryHandler.setMaxTorque(0);
        }
    }

    public boolean validDevice(IRotaryNetworkDevice device) {
        return !devices.contains(device);
    }

    public List<RotaryNetworkDevice<?>> getDevices() {
        return devices;
    }

    public Map<BlockPos, RotarySource> getSources() {
        return sources;
    }

    public Map<BlockPos, RotaryClient> getClients() {
        return clients;
    }

    public void emitPower(float angular, float torque) {
        if (angular != this.rotaryHandler.getAngular() || torque != this.rotaryHandler.getTorque()) {
            this.rotaryHandler.setAngular(angular);
            this.rotaryHandler.setTorque(torque);
            this.updateNetwork();
        }
    }

    private void updateNetwork() {
        if(!transmitters.isEmpty()) {
            transmitters.forEach((pos, transmitter) -> transmitter.setRotaryPower(this.rotaryHandler.getAngular(), this.rotaryHandler.getTorque(), this.rotaryHandler.getTemp()));
        }
        if (!clients.isEmpty()) {
            clients.forEach((pos, client) -> client.setRotaryPower(this.rotaryHandler.getAngular(), this.rotaryHandler.getTorque(), this.rotaryHandler.getTemp()));
        }
    }

    public void clear() {
        this.transmitters.clear();
        this.sources.clear();
        this.clients.clear();
        this.devices.clear();
    }

    public <T extends RotaryNetworkDevice<?>> void registerDevice(T device) {
        if (!validDevice(device)) return;
        BlockPos pos = device.getBlockPos();
        if (device instanceof RotaryClient client) {
            clients.put(pos, client);
        }
        if (device instanceof RotaryTransmitter transmitter) {
            transmitters.put(pos, transmitter);
        }
        if (device instanceof RotarySource source) {
            sources.put(pos, source);
            increaseMax(source.sourceConfig);
        }
        devices.add(device);
        updateNetwork();
    }

    public <T extends RotaryNetworkDevice<?>> void unregisterDevice(T device) {
        if (validDevice(device)) return;
        BlockPos pos = device.getBlockPos();
        if (device instanceof RotaryClient client) {
            clients.remove(pos, client);
        }
        if (device instanceof RotaryTransmitter transmitter) {
            transmitters.remove(pos, transmitter);
        }
        if (device instanceof RotarySource source) {
            sources.remove(pos, source);
            reduceMax(source.sourceConfig);
        }
        devices.remove(device);
        updateNetwork();
    }
}
