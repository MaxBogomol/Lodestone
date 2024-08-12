package team.lodestar.lodestone.systems.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


/**
 * A simple block entity which holds a single ItemStack
 */
public abstract class ItemHolderBlockEntity extends LodestoneBlockEntity {
    public LodestoneBlockEntityInventory inventory;

    public ItemHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        inventory.interact(this, player.level(), player, hand, s -> true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onBreak(@Nullable Player player) {
        inventory.dumpItems(level, worldPosition);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        compound.put("Inventory", inventory.serializeNBT());
        super.saveAdditional(compound, provider);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        super.loadAdditional(compound, provider);
    }
}