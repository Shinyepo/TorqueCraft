package dev.shinyepo.torquecraft.network;

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

    public RotaryNetwork(UUID id) {
        network_id = id;
    }

    public UUID getNetworkId() {
        return this.network_id;
    }

    public void registerSource(RotarySource source) {
        sources.put(source.getBlockPos(), source);
        devices.add(source);
    }

    public boolean validDevice(IRotaryNetworkDevice device) {
        return !devices.contains(device);
    }
    public void registerTransmitter(RotaryTransmitter transmitter) {
        transmitters.put(transmitter.getBlockPos(), transmitter);
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
        devices.remove(source);
    }


    public void removeTransmitter(RotaryTransmitter rotaryTransmitter) {
        transmitters.remove(rotaryTransmitter.getBlockPos());
        devices.remove(rotaryTransmitter);
    }
}
