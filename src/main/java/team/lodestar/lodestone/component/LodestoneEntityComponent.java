package team.lodestar.lodestone.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;

public class LodestoneEntityComponent implements AutoSyncedComponent {

    public FireEffectInstance fireEffectInstance;

    private final Entity entity;

    public LodestoneEntityComponent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        FireEffectHandler.deserializeNBT(this, tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        FireEffectHandler.serializeNBT(this, tag);
    }
}
