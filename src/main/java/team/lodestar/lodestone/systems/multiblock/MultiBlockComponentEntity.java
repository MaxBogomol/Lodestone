package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

/**
 * A basic Multiblock component block entity. Defers some important actions to the core of the multiblock.
 */
public class MultiBlockComponentEntity extends LodestoneBlockEntity {

    public BlockPos corePos;

    public MultiBlockComponentEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MultiBlockComponentEntity(BlockPos pos, BlockState state) {
        super(LodestoneBlockEntityRegistry.MULTIBLOCK_COMPONENT.get(), pos, state);
    }



    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        if (corePos != null) {
            BlockHelper.saveBlockPos(tag, corePos, "core_position_");
        }
        super.saveAdditional(tag, provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        corePos = BlockHelper.loadBlockPos(tag, "core_position_");
        super.loadAdditional(tag, provider);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        if (corePos != null && level.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
            return core.onUse(player, hand);
        }
        return super.onUse(player, hand);
    }

    @Override
    public void onBreak(@Nullable Player player) {
        if (corePos != null && level.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
            core.onBreak(player);
        }
        super.onBreak(player);
    }
}