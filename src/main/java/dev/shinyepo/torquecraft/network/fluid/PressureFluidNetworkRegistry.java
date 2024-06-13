package dev.shinyepo.torquecraft.network.fluid;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.pipe.network.IPressureTransmitter;
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

public class PressureFluidNetworkRegistry {
    private static final PressureFluidNetworkRegistry INSTANCE = new PressureFluidNetworkRegistry();
    private static Boolean INITIALIZED = false;
    private final Map<UUID, PressureFluidNetwork> networks = new Object2ObjectOpenHashMap<>();

    public static void init() {
        if (!INITIALIZED) {
            INITIALIZED = true;
            NeoForge.EVENT_BUS.register(INSTANCE);
            TorqueCraft.logger.info("Registering Fluid Network instance.");
        }
    }

    public static PressureFluidNetworkRegistry getInstance() {
        return INSTANCE;
    }


    public static void reset() {
        getInstance().networks.clear();
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent.Post event) {
        cleanEmptyNetworks();
        networks.forEach((id, network) -> network.emitFluid());
    }

    private void cleanEmptyNetworks() {
        networks.forEach((id, network) -> {
            if (network.getTransmitters().isEmpty()) {
                removeNetwork(id);
                TorqueCraft.logger.info("Empty fluid network was removed.");
            }
        });
    }

    public PressureFluidNetwork getNetwork(UUID id) {
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

    public PressureFluidNetwork registerNetwork(UUID id, IPressureTransmitter transmitter) {
        PressureFluidNetwork network = new PressureFluidNetwork(id);
        network.registerTransmitter(transmitter);
        getInstance().networks.put(id, network);
        TorqueCraft.logger.info("Registering network. New network size: {}", networks.size());
        return network;
    }

    public PressureFluidNetwork registerNetwork(PressureFluidNetwork network) {
        getInstance().networks.put(network.getNetworkId(), network);
        TorqueCraft.logger.info("Registering network. New network size: {}", networks.size());

        return network;
    }

    public PressureFluidNetwork registerDevice(IPressureTransmitter transmitter) {
        PressureFluidNetwork network = getInstance().fetchNetwork(transmitter);
        if (network != null) {
            network.registerTransmitter(transmitter);
            TorqueCraft.logger.info("Registering transmitter. New network size: {}", networks.size());

            return network;
        } else {
            TorqueCraft.logger.info("Created new network for device");
            return getInstance().registerNetwork(UUID.randomUUID(), transmitter);
        }
    }

    public PressureFluidNetwork fetchNetwork(IPressureTransmitter dev1) {
        List<PressureFluidNetwork> foundNetworks = new ArrayList<>();
        SideType[] sides = dev1.getSidesConfig();
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] == SideType.NONE) {
                BlockPos dev1Pos = dev1.getBlockPos();
                var blockEntity = dev1.getLevel().getBlockEntity(dev1Pos.relative(Direction.values()[i]));
                if (blockEntity instanceof IPressureTransmitter dev2) {
                    SideType[] sides2 = dev2.getSidesConfig();
                    if (sides2[Direction.values()[i].getOpposite().ordinal()] == SideType.NONE) {
                        PressureFluidNetwork network = getInstance().getNetwork(dev2.getNetworkId());
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

    private PressureFluidNetwork mergeNetworks(List<PressureFluidNetwork> networks) {
        PressureFluidNetwork newNetwork = new PressureFluidNetwork(UUID.randomUUID());
        for (PressureFluidNetwork network : networks) {
            bulkAddDevices(newNetwork, network.getTransmitters());
            getInstance().removeNetwork(network.getNetworkId());
        }
        TorqueCraft.logger.info("Merged " + networks.size() + " networks");
        return getInstance().registerNetwork(newNetwork);
    }

    private void bulkAddDevices(PressureFluidNetwork network, Map<BlockPos, IPressureTransmitter> transmitterMap) {
        transmitterMap.forEach((pos, transmitter) -> {
            network.registerTransmitter(transmitter);
            transmitter.updateNetwork(network);
        });
    }

    public void unregisterDevice(UUID networkId, IPressureTransmitter transmitter) {
        PressureFluidNetwork network = getInstance().getNetwork(networkId);
        if (network == null) return;
        List<IPressureTransmitter> devices = new ArrayList<>();
        SideType[] sides = transmitter.getSidesConfig();
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] == SideType.NONE) {
                BlockPos dev1Pos = transmitter.getBlockPos();
                var blockEntity = transmitter.getLevel().getBlockEntity(dev1Pos.relative(Direction.values()[i]));
                if (blockEntity instanceof IPressureTransmitter dev2) {
                    SideType[] sides2 = dev2.getSidesConfig();
                    if (sides2[Direction.values()[i].getOpposite().ordinal()] == SideType.NONE) {
                        devices.add(dev2);
                    }
                }
            }
        }
        if (devices.size() > 1) {
            for (IPressureTransmitter transmitter1 : devices) {
                getInstance().splitNetwork(transmitter1, transmitter.getBlockPos());
            }
            getInstance().removeNetwork(networkId);
        } else {
            network.unregisterTransmitter(transmitter);
        }
    }

    private void splitNetwork(IPressureTransmitter transmitter, BlockPos unregisteredPos) {
        PressureFluidNetwork newNetwork = new PressureFluidNetwork(UUID.randomUUID());

        getInstance().findDevices(newNetwork, transmitter, unregisteredPos);
        getInstance().registerNetwork(newNetwork);
        TorqueCraft.logger.info("Split networks devices: " + newNetwork.getTransmitters().toString());
    }


    private void findDevices(PressureFluidNetwork network, IPressureTransmitter transmitter, BlockPos unregisteredPos) {
        if (transmitter.getBlockPos() == unregisteredPos) return;
        if (!network.validDevice(transmitter)) return;
        network.registerTransmitter(transmitter);
        transmitter.updateNetwork(network);
        SideType[] sides = transmitter.getSidesConfig();
        for (int i = 0; i < sides.length; i++) {
            if (sides[i] != SideType.BLOCKED) {
                BlockEntity relativeEntity = transmitter.getLevel().getBlockEntity(transmitter.getBlockPos().relative(Direction.values()[i]));
                if (relativeEntity instanceof IPressureTransmitter relativeTransmitter)
                    getInstance().findDevices(network, relativeTransmitter, unregisteredPos);
            }
        }
    }
}
