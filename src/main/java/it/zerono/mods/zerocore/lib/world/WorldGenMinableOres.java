package it.zerono.mods.zerocore.lib.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WorldGenMinableOres extends ModWorldGeneratorBase {

    public WorldGenMinableOres(final IWorldGenWhiteList whiteList) {
        this(whiteList, false);
    }

    public WorldGenMinableOres(final IWorldGenWhiteList whiteList, final boolean useBlackListLogic) {
        super(whiteList, useBlackListLogic);
    }

    public void addOre(final Block ore, final Block blockToReplace, final int minY, final int maxY,
                       final int maxBlockCount, final int iterations) {

        this.addOre(ore.getDefaultState(), blockToReplace.getDefaultState(), minY, maxY, maxBlockCount, iterations);
    }

    public void addOre(final IBlockState ore, final IBlockState blockToReplace, final int minY, final int maxY,
                       final int maxBlockCount, final int iterations) {

        if ((null == ore) || (null == blockToReplace))
            throw new IllegalArgumentException("Illegal ore or target block");

        if ((minY < 0) || (maxY < minY) || (maxY > 256))
            throw new IllegalArgumentException("Invalid Y coordinates");

        if (maxBlockCount <= 0)
            throw new IllegalArgumentException("maxBlockCount should be greather than zero");

        if (iterations <= 0)
            throw new IllegalArgumentException("Invalid number of iterations");

        if (null == this._generators)
            this._generators = Lists.newArrayList();

        this._generators.add(new VeinGenerator(ore, blockToReplace, minY, maxY, maxBlockCount, iterations));
    }

    public void clearOres() {
        if (null != this._generators)
            this._generators.clear();
    }

    @Override
    protected void generateChunk(final Random random, final int firstBlockX, final int firstBlockZ, final World world,
                                 final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
        this.generateChunk(random, firstBlockX, firstBlockZ, world);
    }

    public void generateChunk(final Random random, final int firstBlockX, final int firstBlockZ, final World world) {

        if ((null == this._generators) || (this._generators.isEmpty()))
            return;

        for (final VeinGenerator generator: this._generators) {

            for (int iteration = 0; iteration < generator.iterations; ++iteration) {

                final int y = generator.minY + random.nextInt(generator.maxY - generator.minY);
                final BlockPos position = new BlockPos(firstBlockX, y, firstBlockZ);

                generator.generate(world, random, position);
            }
        }
    }

    private List<VeinGenerator> _generators;

    private class VeinGenerator extends WorldGenMinable {

        VeinGenerator(final IBlockState ore, final IBlockState blockToReplace, final int minY, final int maxY,
                      final int maxBlockCount, final int iterations) {

            super(ore, maxBlockCount, new Predicate<IBlockState>() {
                @Override
                public boolean apply(@Nullable IBlockState input) {
                    return null != input &&
                            input.getBlock() == this._targetBlock &&
                            input.getBlock().getMetaFromState(input) == this._targetBlock.getMetaFromState(input);
                }

                private final Block _targetBlock = blockToReplace.getBlock();
            });

            this.minY = minY;
            this.maxY = maxY;
            this.iterations = iterations;
        }

        final int minY;
        final int maxY;
        final int iterations;
    }
}
