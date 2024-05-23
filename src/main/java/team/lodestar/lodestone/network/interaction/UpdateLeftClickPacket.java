package team.lodestar.lodestone.network.interaction;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class UpdateLeftClickPacket extends LodestoneServerPacket {

    private final boolean leftClickHeld;

    public UpdateLeftClickPacket(boolean rightClick) {
        this.leftClickHeld = rightClick;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(leftClickHeld);
    }

    @Override
    public void executeServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.leftClickHeld = leftClickHeld);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateLeftClickPacket.class, UpdateLeftClickPacket::encode, UpdateLeftClickPacket::decode, UpdateLeftClickPacket::handle);
    }

    public static UpdateLeftClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateLeftClickPacket(buf.readBoolean());
    }
}