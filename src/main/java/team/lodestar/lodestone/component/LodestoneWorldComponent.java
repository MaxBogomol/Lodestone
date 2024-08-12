package team.lodestar.lodestone.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

import java.util.ArrayList;

public class LodestoneWorldComponent implements AutoSyncedComponent, CommonTickingComponent {
    public Level level;
    public final ArrayList<WorldEventInstance> activeWorldEvents = new ArrayList<>();
    public final ArrayList<WorldEventInstance> inboundWorldEvents = new ArrayList<>();

    public LodestoneWorldComponent(Level level){
        this.level = level;
    }


    @Override
    public void tick() {
        WorldEventHandler.worldTick(level);
        if(level.isClientSide()){
            WorldEventHandler.tick(level);
        }
    }

    @Override
    public void readFromNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        WorldEventHandler.deserializeNBT(this, nbt);
    }

    @Override
    public void writeToNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        WorldEventHandler.serializeNBT(this, nbt);
    }
}
