package it.zerono.mods.zerocore.lib.data.fixer;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

import javax.annotation.Nonnull;
import java.util.Map;

public class GameObjectWalker implements IDataWalker {

    public GameObjectWalker() {
        this._walkers = Maps.newHashMap();
    }

    public void addObjectWalker(@Nonnull final ResourceLocation objectId, @Nonnull final IGameObjectDataWalker objectWalker) {
        this._walkers.put(objectId.toString(), objectWalker);
    }

    //region IDataWalker

    @Override
    public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int version) {

        if (null != compound && compound.hasKey("id")) {

            final IGameObjectDataWalker walker = this._walkers.get(compound.getString("id"));

            if (null != walker) {
                compound = walker.walkObject(fixer, compound, version);
            }
        }

        return compound;
    }

    //region internals

    private final Map<String, IGameObjectDataWalker> _walkers;
}
