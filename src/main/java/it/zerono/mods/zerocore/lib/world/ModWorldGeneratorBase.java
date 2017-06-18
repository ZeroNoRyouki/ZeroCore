package it.zerono.mods.zerocore.lib.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public abstract class ModWorldGeneratorBase implements IWorldGenerator {

    @Override
    public final void generate(final Random random, final int chunkX, final int chunkZ, final World world,
                               final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {

        if ((null == this._whiteList) || this._useBlackList == this._whiteList.shouldGenerateIn(world))
            return;

        this.generateChunk(random, chunkX << 4, chunkZ << 4, world, chunkGenerator, chunkProvider);
    }

    public void setBehavior(final boolean useBlackListLogic) {
        this._useBlackList = useBlackListLogic;
    }

    protected abstract void generateChunk(final Random random, final int firstBlockX, final int firstBlockZ,
                                          final World world, final IChunkGenerator chunkGenerator,
                                          final IChunkProvider chunkProvider);

    protected ModWorldGeneratorBase(final IWorldGenWhiteList whiteList, final boolean useBlackListLogic) {

        this._whiteList = whiteList;
        this._useBlackList = useBlackListLogic;
    }

    protected final IWorldGenWhiteList _whiteList;
    protected boolean _useBlackList;
}
