package dev.shinyepo.torquecraft.network;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryNetworkDevice;
import dev.shinyepo.torquecraft.factory.rotary.network.RotaryTransmitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RotaryNetworkRegistry {
    private static final RotaryNetworkRegistry INSTANCE = new RotaryNetworkRegistry();
    private static Boolean INITIALIZED = false;
    private Map<UUID, RotaryNetwork> networks = new Object2ObjectOpenHashMap<>();

    public static void init() {
        if (!INITIALIZED) {
            INITIALIZED = true;
            NeoForge.EVENT_BUS.register(INSTANCE);
            TorqueCraft.logger.info("Registering Network instance.");
        }
    }

    public static RotaryNetworkRegistry getInstance() {
        return INSTANCE;
    }


    public static void reset() {
        getInstance().networks.clear();
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent.Post event) {
        cleanEmptyNetworks();
    }

    private void cleanEmptyNetworks() {
        networks.forEach((id, network) -> {
            if (network.getDevices().isEmpty()) {
                removeNetwork(id);
                TorqueCraft.logger.info("Empty network was removed.");
            }
        });
    }

    public RotaryNetwork getNetwork(UUID id) {
        return getInstance().networks.get(id);
    }

    public void removeNetwork(UUID id) {
        var network = getInstance().networks.get(id);
        if (network != null) {
            network.clear();
            getInstance().networks.remove(id);
        }
        TorqueCraft.logger.info("Removed network. New network size: {}", networks.size());
    }

    public <T extends RotaryNetworkDevice<?>> UUID registerNetwork(UUID id, T device) {
        RotaryNetwork network = new RotaryNetwork(id);
        network.registerDevice(device);
        getInstance().networks.put(id, network);
        TorqueCraft.logger.info("Registering network. New network size: {}", networks.size());
        return id;
    }

    public RotaryNetwork registerNetwork(RotaryNetwork network) {
        getInstance().networks.put(network.getNetworkId(), network);
        return network;
    }

    public <T extends RotaryNetworkDevice<?>> UUID registerDevice(T device) {
        RotaryNetwork network = getInstance().fetchNetwork(device);
        if (network != null) {
            network.registerDevice(device);
            TorqueCraft.logger.info("New network devices: {}", network.getDevices().toString());

            TorqueCraft.logger.info("Device merged with existing network");
            return network.getNetworkId();
        } else {
            TorqueCraft.logger.info("Created new network for device");
            return getInstance().registerNetwork(UUID.randomUUID(), device);
        }
    }

    public RotaryNetwork fetchNetwork(RotaryNetworkDevice<?> dev1) {
        List<RotaryNetwork> foundNetworks = new ArrayList<>();
        SideType[] sides = dev1.getSidesConfig();
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] != SideType.NONE) {
                BlockPos dev1Pos = dev1.getBlockPos();
                var blockEntity = dev1.getLevel().getBlockEntity(dev1Pos.relative(Direction.values()[i]));
                if (blockEntity instanceof RotaryNetworkDevice<?> dev2) {
                    SideType[] sides2 = dev2.getSidesConfig();
                    if (sides2[Direction.values()[i].getOpposite().ordinal()].getOpposite() == sides[i].ordinal()) {
                        RotaryNetwork network = getInstance().getNetwork(dev2.getNetworkId());
                        if (network != null)
                            foundNetworks.add(network);
                    }
                }
            }
        }
        if (foundNetworks.size() > 1) {
            return getInstance().mergeNetworks(foundNetworks);
        }
        return foundNetworks.isEmpty() ? null : foundNetworks.getFirst();
    }

    private RotaryNetwork mergeNetworks(List<RotaryNetwork> networks) {
        List<RotaryNetworkDevice<?>> devices1 = networks.get(0).getDevices();
        List<RotaryNetworkDevice<?>> devices2 = networks.get(1).getDevices();
        RotaryNetwork newNetwork = new RotaryNetwork(UUID.randomUUID());
        bulkAddDevices(newNetwork, devices1);
        bulkAddDevices(newNetwork, devices2);
        getInstance().removeNetwork(networks.get(0).getNetworkId());
        getInstance().removeNetwork(networks.get(1).getNetworkId());
        TorqueCraft.logger.info("Merged 2 networks");
        TorqueCraft.logger.info("Devices from first network: {}", devices1);
        TorqueCraft.logger.info("Devices from second network: {}", devices2);
        return getInstance().registerNetwork(newNetwork);
    }

    private void bulkAddDevices(RotaryNetwork network, List<RotaryNetworkDevice<?>> devices) {
        for (RotaryNetworkDevice<?> device : devices) {
            network.registerDevice(device);
            device.updateNetwork(network.getNetworkId());
        }
    }

    public <T extends RotaryNetworkDevice<?>> void unregisterDevice(UUID networkId, T device) {
        RotaryNetwork network = getInstance().getNetwork(networkId);
        if (network == null) return;
        if (device instanceof RotaryTransmitter transmitter) {
            List<RotaryNetworkDevice<?>> devices = new ArrayList<>();
            SideType[] sides = transmitter.getSidesConfig();
            for (int i = 0; i < sides.length; i++) {
                if (sides[i] != SideType.NONE) {
                    BlockPos dev1Pos = transmitter.getBlockPos();
                    var blockEntity = transmitter.getLevel().getBlockEntity(dev1Pos.relative(Direction.values()[i]));
                    if (blockEntity instanceof RotaryNetworkDevice<?> dev2) {
                        SideType[] sides2 = dev2.getSidesConfig();
                        if (sides2[Direction.values()[i].getOpposite().ordinal()].getOpposite() == sides[i].ordinal()) {
                            devices.add(dev2);
                        }
                    }
                }
            }
            if (devices.size() > 1) {
                for (RotaryNetworkDevice<?> rotaryNetworkDevice : devices) {
                    getInstance().splitNetwork(rotaryNetworkDevice, device.getBlockPos());
                }
                getInstance().removeNetwork(networkId);
            } else {
                network.unregisterDevice(transmitter);
            }
        } else {
            network.unregisterDevice(device);
        }
    }

    private void splitNetwork(RotaryNetworkDevice<?> device, BlockPos unregisteredPos) {
        RotaryNetwork newNetwork = new RotaryNetwork(UUID.randomUUID());

        getInstance().findDevices(newNetwork, device, unregisteredPos);
        getInstance().registerNetwork(newNetwork);
        TorqueCraft.logger.info("Split networks devices: " + newNetwork.getDevices().toString());
    }


    private void findDevices(RotaryNetwork network, BlockEntity device, BlockPos unregisteredPos) {
        if (device instanceof RotaryTransmitter transmitter) {
            if (device.getBlockPos() == unregisteredPos) return;
            if (!network.validDevice(transmitter)) return;
            network.registerDevice(transmitter);
            transmitter.updateNetwork(network.getNetworkId());
            SideType[] sides = transmitter.getSidesConfig();
            for (int i = 0; i < sides.length; i++) {
                if (sides[i] != SideType.NONE) {
                    BlockEntity relativeEntity = device.getLevel().getBlockEntity(device.getBlockPos().relative(Direction.values()[i]));
                    if (relativeEntity instanceof RotaryNetworkDevice<?>)
                        getInstance().findDevices(network, relativeEntity, unregisteredPos);
                }
            }
        } else {
            if (!network.validDevice((IRotaryNetworkDevice) device)) return;
            network.registerDevice((RotaryNetworkDevice<?>) device);
            ((RotaryNetworkDevice<?>) device).updateNetwork(network.getNetworkId());
        }
    }
}
