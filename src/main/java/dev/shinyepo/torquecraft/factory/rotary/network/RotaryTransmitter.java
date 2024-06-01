package dev.shinyepo.torquecraft.factory.rotary.network;

import dev.shinyepo.torquecraft.config.TransmitterConfig;
import dev.shinyepo.torquecraft.config.side.SideType;
import dev.shinyepo.torquecraft.factory.IWrenchInteraction;
import dev.shinyepo.torquecraft.network.RotaryNetworkRegistry;
import dev.shinyepo.torquecraft.networking.TorqueMessages;
import dev.shinyepo.torquecraft.networking.packets.SyncMeltdownS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class RotaryTransmitter extends RotaryNetworkDevice<TransmitterConfig> implements IWrenchInteraction {
    private final TransmitterConfig transmitterConfig;
    private boolean meltdown = false;


    public RotaryTransmitter(BlockEntityType<?> type, BlockPos pos, BlockState blockState, TransmitterConfig config) {
        super(type, pos, blockState, config);
        this.transmitterConfig = config;
        configureSides(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    public void configureSides(Direction facing) {
        switch (transmitterConfig) {
            case SHAFT -> {
                configureSides(facing, SideType.OUTPUT);
                configureSides(facing.getOpposite(), SideType.INPUT);
            }
            case THREE_WAY -> {
                configureSides(facing, SideType.OUTPUT);
                configureSides(facing.getClockWise(), SideType.INPUT);
                configureSides(facing.getOpposite(), SideType.INPUT);
            }
            case BEVEL_GEARS -> {
                configureSides(facing.getOpposite(), SideType.INPUT);
                configureSides(Direction.UP, SideType.OUTPUT);
            }
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.rotaryHandler.get().getAngular() > transmitterConfig.getAngular() || this.rotaryHandler.get().getTorque() > transmitterConfig.getTorque()) {
            //Init meltdown
        }

        if (meltdown) {
            this.rotaryHandler.get().setAngular(0);
            this.rotaryHandler.get().setTorque(0);
        }
        RotaryNetworkRegistry.getInstance().getNetwork(this.getNetworkId()).emitPower(this.getBlockPos().relative(this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)), this.rotaryHandler.get().getAngular(), this.rotaryHandler.get().getTorque());

    }


    @Override
    public void renderTick() {
        super.renderTick();
        if (meltdown) {
            displayMeltdown();
        }
    }

    //Server
    public void initMeltdown(BlockPos pos) {
        if (!meltdown) {
            meltdown = true;
            TorqueMessages.sendToAllPlayers(new SyncMeltdownS2C(pos, true));
        }
    }

    public void stopMeltdown(BlockPos pos) {
        if (meltdown) {
            meltdown = false;
            TorqueMessages.sendToAllPlayers(new SyncMeltdownS2C(pos, false));
        }
    }

    //Client
    public void clientMeltdown(boolean state) {
        this.meltdown = state;
    }

    private void displayMeltdown() {
        if (level == null) return;
        if (level.getGameTime() % 5 != 0) return;

        ParticleOptions particle = ParticleTypes.SMOKE;
        Vec3 offset1 = new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
        float speed = 0.1F;
        level.playLocalSound(this.worldPosition, SoundEvents.ANVIL_BREAK, SoundSource.BLOCKS, 1F, 1F, true);
        level.addParticle(particle, offset1.x, offset1.y, offset1.z, 0.01F, speed, 0);
        level.addParticle(particle, offset1.x, offset1.y, offset1.z, 0, speed, 0.01F);
        level.addParticle(particle, offset1.x, offset1.y, offset1.z, 0.04F, speed, 0);
        level.addParticle(particle, offset1.x, offset1.y, offset1.z, 0, speed, 0.04F);
    }
}
