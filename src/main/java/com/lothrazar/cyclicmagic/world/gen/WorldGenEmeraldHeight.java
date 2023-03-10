package com.lothrazar.cyclicmagic.world.gen;
import java.util.Random;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenEmeraldHeight implements IWorldGenerator {
  private WorldGenerator genEmerald;
  public WorldGenEmeraldHeight() {
    //http://minecraft.gamepedia.com/Ore#Availability
    // http://minecraft.gamepedia.com/Customized#Ore_settings
    genEmerald = new WorldGenMinable(Blocks.EMERALD_ORE.getDefaultState(), 1, BlockMatcher.forBlock(Blocks.STONE));
  }
  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    int spawnTries = 70;
    if (Const.Dimension.overworld == world.provider.getDimension()) {
      this.run(genEmerald, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, spawnTries, 0, Const.WORLDHEIGHT - 1);
    }
  }
  private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
    if (minHeight < 0 || maxHeight > Const.WORLDHEIGHT || minHeight > maxHeight)
      throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
    int heightDiff = maxHeight - minHeight;
    BlockPos pos;
    Biome biome;
    for (int i = 0; i < chancesToSpawn; i++) {
      int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
      int y = minHeight + rand.nextInt(heightDiff);
      int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);
      pos = new BlockPos(x, y, z);
      biome = world.getBiome(pos);
      if (biome == Biomes.EXTREME_HILLS || biome == Biomes.EXTREME_HILLS_EDGE || biome == Biomes.EXTREME_HILLS_WITH_TREES) {
        generator.generate(world, rand, pos);
      }
    }
  }
}
