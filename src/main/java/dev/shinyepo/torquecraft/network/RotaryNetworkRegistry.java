package dev.shinyepo.torquecraft.network;

import dev.shinyepo.torquecraft.TorqueCraft;
import dev.shinyepo.torquecraft.capabilities.TorqueCustomCapabilities;
import dev.shinyepo.torquecraft.capabilities.handlers.IRotaryHandler;
import dev.shinyepo.torquecraft.factory.rotary.IRotaryIO;
import dev.shinyepo.torquecraft.factory.rotary.RotarySource;
import dev.shinyepo.torquecraft.factory.rotary.RotaryTransmitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
    private List<BlockPos> POS_CACHE = List.of();

    public static void init() {
        if (!INITIALIZED) {
            INITIALIZED = true;
            NeoForge.EVENT_BUS.register(INSTANCE);
            TorqueCraft.logger.info("Registering Network instance.");
        }
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
        networks.remove(id);
        TorqueCraft.logger.info("Removed network. New network size: {}", networks.size());
    }

    public UUID registerNetwork(UUID id, RotarySource source) {
        RotaryNetwork network = new RotaryNetwork(id);
        getInstance().addSource(network, source);
        getInstance().networks.put(id, network);
        TorqueCraft.logger.info("Registering network. New network size: {}", networks.size());
        return id;
    }

    public UUID registerNetwork(UUID id, RotaryTransmitter transmitter) {
        RotaryNetwork network = new RotaryNetwork(id);
        getInstance().addTransmitter(network, transmitter);
        getInstance().networks.put(id, network);
        TorqueCraft.logger.info("Registering network. New network size: {}", networks.size());
        return id;
    }

    public RotaryNetwork registerNetwork(RotaryNetwork network) {
        getInstance().networks.put(network.getNetworkId(), network);
        return network;
    }

    private void addTransmitter(RotaryNetwork network, RotaryTransmitter transmitter) {
        network.registerTransmitter(transmitter);
    }

    private void addSource(RotaryNetwork network, RotarySource source) {
        network.registerSource(source);
    }

    public UUID registerTransmitter(RotaryTransmitter transmitter, Direction direction) {
        //TODO: Directions[]
        RotaryNetwork network = getInstance().fetchNetwork((IRotaryNetworkDevice) transmitter, new Direction[]{direction, direction.getOpposite()});
        if (network != null) {
            getInstance().addTransmitter(network, transmitter);
            TorqueCraft.logger.info("New network devices: {}", network.getDevices().toString());

            TorqueCraft.logger.info("Transmitter merged with existing network");
            return network.getNetworkId();
        } else {
            TorqueCraft.logger.info("Created new network for transmitter");
            return getInstance().registerNetwork(UUID.randomUUID(), transmitter);
        }
    }

    public UUID registerSource(RotarySource source) {
        RotaryNetwork network = getInstance().fetchNetwork((IRotaryNetworkDevice) source, new Direction[]{source.getBlockState().getValue(HorizontalDirectionalBlock.FACING)});
        if (network != null) {
            getInstance().addSource(network, source);
            TorqueCraft.logger.info("Source merged with existing network");
            return network.getNetworkId();
        } else {
            TorqueCraft.logger.info("Created new network for source");
            return registerNetwork(UUID.randomUUID(), source);
        }
    }


    public static RotaryNetworkRegistry getInstance() {
        return INSTANCE;
    }

    private static boolean possibleNetwork(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        if (block instanceof IRotaryIO) {
            BlockState state = level.getBlockState(pos);
            IRotaryHandler handler = level.getCapability(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK, pos.relative(state.getValue(HorizontalDirectionalBlock.FACING)), null);
            return handler != null;
        }
        return false;
    }

    private void fetchMachines(Level level, BlockPos pos) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            IRotaryHandler handler = level.getCapability(TorqueCustomCapabilities.ROTARY_HANDLER_BLOCK, pos.relative(dir), null);
            if (handler != null) {
                if (!POS_CACHE.contains(pos.relative(dir))) {
                    POS_CACHE.add(pos.relative(dir));
                    fetchMachines(level, pos.relative(dir));
                }
            }
        }
    }

    //TODO: Separate for transmitter
    public RotaryNetwork fetchNetwork(IRotaryNetworkDevice networkDevice, Direction[] directions) {
        List<RotaryNetwork> foundNetworks = new ArrayList<>();
        int i = 0;
        for (Direction dir : directions) {
            if (networkDevice instanceof BlockEntity entity) {
                BlockPos pos = entity.getBlockPos();
                var device = entity.getLevel().getBlockEntity(pos.relative(dir));
                if (device instanceof IRotaryNetworkDevice nextDevice) {
                    BlockState nextState = nextDevice.getBlockState();
                    if ((i == 0 ? nextState.getValue(HorizontalDirectionalBlock.FACING) : nextState.getValue(HorizontalDirectionalBlock.FACING).getOpposite()) == dir) {
                        foundNetworks.add(getInstance().getNetwork(nextDevice.getNetworkId()));
                    }
                }
            }
            i++;
        }
        //TODO: Merging transmitter is clunky
        if (foundNetworks.size() > 1) {
            return getInstance().mergeNetworks(foundNetworks);
        }
        return foundNetworks.isEmpty() ? null : foundNetworks.getFirst();
    }

    private RotaryNetwork mergeNetworks(List<RotaryNetwork> networks) {
        List<IRotaryNetworkDevice> devices1 = networks.getFirst().getDevices();
        List<IRotaryNetworkDevice> devices2 = networks.getLast().getDevices();
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

    private void bulkAddDevices(RotaryNetwork network, List<IRotaryNetworkDevice> devices) {
        for (IRotaryNetworkDevice device : devices) {
            if (device instanceof RotarySource source) {
                network.registerSource(source);
            } else if (device instanceof RotaryTransmitter transmitter) {
                network.registerTransmitter(transmitter);
            }
            device.updateNetwork(network.getNetworkId());
        }
    }

    public void removeSource(UUID networkId, RotarySource rotarySource) {
        RotaryNetwork network = getInstance().getNetwork(networkId);
        if (network == null) return;
        network.removeSource(rotarySource);
    }

    public void removeTransmitter(UUID networkId, RotaryTransmitter rotaryTransmitter) {
        RotaryNetwork network = getInstance().getNetwork(networkId);
        if (network == null) return;
        BlockPos pos = rotaryTransmitter.getBlockPos();
        Direction dir = rotaryTransmitter.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        Level level = rotaryTransmitter.getLevel();
        if (level.getBlockEntity(pos.relative(dir)) instanceof IRotaryNetworkDevice r1 && level.getBlockEntity(pos.relative(dir.getOpposite())) instanceof IRotaryNetworkDevice r2) {
            if(r1.getBlockState().getValue(HorizontalDirectionalBlock.FACING) == dir && r2.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite() == dir.getOpposite()) {
                getInstance().splitNetwork(network, level.getBlockEntity(pos.relative(dir)), level.getBlockEntity(pos.relative(dir.getOpposite())));
                return;
            }
        }
        network.removeTransmitter(rotaryTransmitter);
    }

    private void splitNetwork(RotaryNetwork network, BlockEntity device, BlockEntity device1) {
        RotaryNetwork newNetwork = new RotaryNetwork(UUID.randomUUID());
        RotaryNetwork newNetwork1 = new RotaryNetwork(UUID.randomUUID());

        findDevices(newNetwork, device);
        getInstance().registerNetwork(newNetwork);
        findDevices(newNetwork1, device1);
        getInstance().registerNetwork(newNetwork1);

        getInstance().removeNetwork(network.getNetworkId());
    }


    private void findDevices(RotaryNetwork network, BlockEntity device) {
        if (device instanceof RotarySource source) {
            if (network.validDevice(source)) {
                network.registerSource(source);
                source.updateNetwork(network.getNetworkId());
                return;
            }
        } else if (device instanceof RotaryTransmitter transmitter) {
            if (network.validDevice(transmitter)) {
                network.registerTransmitter(transmitter);
                transmitter.updateNetwork(network.getNetworkId());
                BlockState tState = transmitter.getBlockState();
                Direction dir = tState.getValue(HorizontalDirectionalBlock.FACING);
                BlockEntity relativeEntity = device.getLevel().getBlockEntity(device.getBlockPos().relative(dir));
                BlockEntity relativeEntity2 = device.getLevel().getBlockEntity(device.getBlockPos().relative(dir.getOpposite()));
                findDevices(network, relativeEntity);
                findDevices(network, relativeEntity2);
            }
        }
        return;


//
//        BlockState state = device.getBlockState();
//        List<Direction> directions = new ArrayList<>();
//        if (device instanceof RotarySource) {
//            directions.add(state.getValue(HorizontalDirectionalBlock.FACING));
//        } else if (device instanceof RotaryTransmitter) {
//            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
//            directions.add(dir);
//            directions.add(dir.getOpposite());
//        }
//
//        for (Direction dir : directions) {
//            BlockEntity relative = device.getLevel().getBlockEntity(device.getBlockPos());
//            if (relative instanceof RotarySource source) {
//                BlockState stateSource = source.getBlockState();
//                if (stateSource.getValue(HorizontalDirectionalBlock.FACING) == dir.getOpposite() && !network.validDevice(source)) {
//                    network.registerSource(source);
//                    source.updateNetwork(network.getNetworkId());
//                    return;
//                }
//            } else if (relative instanceof RotaryTransmitter transmitter && !network.validDevice(transmitter)) {
//                BlockState stateTransmitter = transmitter.getBlockState();
//                if (stateTransmitter.getValue(HorizontalDirectionalBlock.FACING) == dir.getOpposite()) {
//                    network.registerTransmitter(transmitter);
//                    transmitter.updateNetwork(network.getNetworkId());
//                    findDevices(network, relative.getLevel().getBlockEntity(relative.getBlockPos().relative(dir.getOpposite())));
//                }
//            }
//        }
    }
}
