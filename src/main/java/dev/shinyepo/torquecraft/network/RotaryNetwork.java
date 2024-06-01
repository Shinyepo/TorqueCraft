package dev.shinyepo.torquecraft.network;

import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.GearboxEntity;
import dev.shinyepo.torquecraft.block.entities.rotary.transmitters.ThreeWayEntity;
import dev.shinyepo.torquecraft.capabilities.handlers.rotary.RotaryHandler;
import dev.shinyepo.torquecraft.config.RotaryMode;
import dev.shinyepo.torquecraft.config.SourceConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.constants.TorqueAttributes;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryClient;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.factory.rotary.network.RotarySource;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RotaryNetwork {
    private final UUID network_id;
    private final Map<BlockPos, RotaryNetworkDevice<?>> devices = new Object2ObjectOpenHashMap<>();
    private final Map<BlockPos, RotarySource> sources = new Object2ObjectOpenHashMap<>();
    private final Map<BlockPos, RotaryTransmitter> transmitters = new Object2ObjectOpenHashMap<>();
    private final Map<BlockPos, RotaryClient> clients = new Object2ObjectOpenHashMap<>();

    private final Map<BlockPos, RotarySource> transmittingSources = new Object2ObjectOpenHashMap<>();

    private final RotaryHandler rotaryHandler = new RotaryHandler(0, 0);

    public RotaryNetwork(UUID id) {
        network_id = id;
    }

    public UUID getNetworkId() {
        return this.network_id;
    }

    private void increaseMax(SourceConfig config) {
        if (this.sources.size() > 1) {
            if (this.rotaryHandler.getAngular() < config.getAngular()) {
                this.rotaryHandler.setMaxAngular(config.getAngular());
                this.rotaryHandler.setMaxTorque(config.getTorque());
            } else if (this.rotaryHandler.getAngular() == config.getAngular()) {
                this.rotaryHandler.setMaxTorque(this.rotaryHandler.getMaxTorque() + config.getTorque());
            }
        } else {
            this.rotaryHandler.setMaxAngular(config.getAngular());
            this.rotaryHandler.setMaxTorque(config.getTorque());
        }
    }

    private void reduceMax(SourceConfig config) {
        if (sources.size() > 1) {
            this.rotaryHandler.setMaxTorque(this.rotaryHandler.getMaxTorque() - config.getTorque());
        } else {
            this.rotaryHandler.setMaxAngular(0);
            this.rotaryHandler.setMaxTorque(0);
        }
    }

    public void emitPower(BlockPos pos, float angular, float torque, RotarySource source) {
        if (transmittingSources.size() >= 4) {
            if (!transmittingSources.containsKey(source.getBlockPos())) {
                var device = devices.get(pos);
                if (device instanceof RotaryTransmitter transmitter) {
                    transmitter.initMeltdown(pos);
                }
                return;
            }
        }
        transmittingSources.put(source.getBlockPos(), source);
        emitPower(pos, angular, torque);
    }

    public void emitPower(BlockPos pos, float angular, float torque) {
        var device = devices.get(pos);
        switch (device) {
            case ThreeWayEntity threeWay -> mergeTransmitter(threeWay, angular, torque);
            case GearboxEntity gearbox -> adjustOutput(gearbox, angular, torque);
            default -> device.setRotaryPower(angular, torque);
        }
    }

    private void adjustOutput(GearboxEntity gearbox, float angular, float torque) {
        var value = gearbox.getRatio().getRatio();
        var mode = gearbox.getBlockState().getValue(TorqueAttributes.ROTARY_MODE);
        float newAngular;
        float newTorque;
        if (mode == RotaryMode.ANGULAR) {
            newAngular = angular * value;
            newTorque = torque / value;
        } else {
            newAngular = angular / value;
            newTorque = torque * value;
        }
        gearbox.setRotaryPower(newAngular, newTorque);
    }

    private void mergeTransmitter(ThreeWayEntity transmitter, float angular, float torque) {
        SideType[] sides = transmitter.getSidesConfig();

        List<Direction> inputs = new ArrayList<>();
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] == SideType.INPUT)
                inputs.add(Direction.values()[i]);
        }
        if (inputs.size() > 1) {
            float mergedAngular = 0;
            float mergedTorque = 0;
            for (Direction dir : inputs) {
                BlockPos pos = transmitter.getBlockPos().relative(dir);
                var dev = devices.get(pos);
                if (dev instanceof RotaryTransmitter || (dev instanceof RotarySource && transmittingSources.containsKey(pos))) {
                    var handler = dev.getRotaryHandler(dir.getOpposite());
                    if (handler != null) {
                        if (handler.getAngular() > mergedAngular) {
                            mergedAngular = handler.getAngular();
                            mergedTorque = handler.getTorque();
                        } else if (handler.getAngular() == mergedAngular && handler.getTorque() > 0) {
                            mergedTorque += handler.getTorque();
                        }
                    }
                }
            }

            transmitter.setRotaryPower(mergedAngular, mergedTorque);
        } else {
            transmitter.setRotaryPower(angular, torque);
        }
    }

    public boolean validDevice(IRotaryNetworkDevice device) {
        return !devices.containsKey(device.getBlockPos());
    }

    public Map<BlockPos, RotaryNetworkDevice<?>> getDevices() {
        return devices;
    }

    public Map<BlockPos, RotarySource> getSources() {
        return sources;
    }

    public Map<BlockPos, RotaryClient> getClients() {
        return clients;
    }

    public boolean validateTransmittingSources(RotarySource source) {
        var found = transmittingSources.get(source.getBlockPos());
        if (found == null) {
            var transmitter = transmitters.get(source.getBlockPos().relative(source.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)));
            if (transmitter != null) {
                transmitter.stopMeltdown(transmitter.getBlockPos());
            }
            return false;
        }
        transmittingSources.remove(source.getBlockPos(), source);
        recheckMeltdown();
        return false;
    }

    private void recheckMeltdown() {
        transmitters.forEach((pos, transmitter) -> transmitter.stopMeltdown(pos));
    }

    private void updateNetwork() {
        if (!transmitters.isEmpty()) {
            transmitters.forEach((pos, transmitter) -> transmitter.setRotaryPower(0, 0));
        }
        if (!clients.isEmpty()) {
            clients.forEach((pos, client) -> client.setRotaryPower(0, 0));
        }
    }

    public void clear() {
        this.transmitters.clear();
        this.sources.clear();
        this.clients.clear();
        this.transmittingSources.clear();
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
            increaseMax(source.getConfig());
        }
        devices.put(pos, device);
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
            transmittingSources.remove(pos, source);
            reduceMax(source.getConfig());
        }
        devices.remove(pos, device);
        updateNetwork();
    }
}
