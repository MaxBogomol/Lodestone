package team.lodestar.lodestone.attachment;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.UnknownNullability;
import team.lodestar.lodestone.capabilityold.LodestonePlayerDataCapability;
import team.lodestar.lodestone.networkold.interaction.UpdateLeftClickPacket;
import team.lodestar.lodestone.networkold.interaction.UpdateRightClickPacket;
import team.lodestar.lodestone.registry.common.LodestoneAttachmentTypes;

public class PlayerAttachment implements INBTSerializable<CompoundTag> {

    public boolean hasJoinedBefore;
    public boolean rightClickHeld;
    public int rightClickTime;
    public boolean leftClickHeld;
    public int leftClickTime;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("firstTimeJoin", hasJoinedBefore);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        hasJoinedBefore = tag.getBoolean("firstTimeJoin");
    }

    public static void playerTick(PlayerTickEvent event) {
        var playerData = event.getEntity().getData(LodestoneAttachmentTypes.PLAYER_DATA);
        playerData.rightClickTime = playerData.rightClickHeld ? playerData.rightClickTime + 1 : 0;
        playerData.leftClickTime = playerData.leftClickHeld ? playerData.leftClickTime + 1 : 0;
    }


    public static void playerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            var playerData = serverPlayer.getData(LodestoneAttachmentTypes.PLAYER_DATA);
            playerData.hasJoinedBefore = true;

            //TODO?
            syncSelf(serverPlayer);
        }
    }

    public static void playerClone(PlayerEvent.Clone event) {
        event.getOriginal().revive();

        var oldPlayerData = event.getOriginal().getData(LodestoneAttachmentTypes.PLAYER_DATA);
        var newPlayerData = event.getEntity().getData(LodestoneAttachmentTypes.PLAYER_DATA);

        newPlayerData.deserializeNBT(oldPlayerData.serializeNBT());
    }

    public static class ClientOnly {
        public static void clientTick(ClientTickEvent event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;

            var playerData = player.getData(LodestoneAttachmentTypes.PLAYER_DATA);
            boolean left = minecraft.options.keyAttack.isDown();
            boolean right = minecraft.options.keyUse.isDown();
            if (left != playerData.leftClickHeld) {
                playerData.leftClickHeld = left;
                LodestonePacketRegistry.LODESTONE_CHANNEL.send(PacketDistributor.SERVER.noArg(), new UpdateLeftClickPacket(playerData.leftClickHeld));
            }
            if (right != playerData.rightClickHeld) {
                playerData.rightClickHeld = right;
                LodestonePacketRegistry.LODESTONE_CHANNEL.send(PacketDistributor.SERVER.noArg(), new UpdateRightClickPacket(playerData.rightClickHeld));
            }
        }
    }
}
