package ru.liahim.saltmod.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.common.ModBlocks;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;

public class SaltOreGenerator implements IWorldGenerator {
	
	int I = 0;
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,	IChunkProvider chunkProvider) {
	
	if (Loader.isModLoaded("TwilightForest")){I = CommonProxy.TFDim;}
		
	if (world.provider.dimensionId == 0 || (world.provider.dimensionId == I && CommonProxy.TFOreGen))
	{generateOverworld(world, random, chunkX*16, chunkZ*16);}
	
	}

	public void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
   	
	int H = 96;
        	
	if (world.provider.dimensionId == 0){H = 96;}
	else if (world.provider.dimensionId == I){H = 64;}
        	
	for (int i = 0; i < CommonProxy.saltOreFrequency; i++) {
		int randPosX = chunkX + rand.nextInt(16);
		int randPosY = rand.nextInt(H);
		int randPosZ = chunkZ + rand.nextInt(16);
     
		(new WorldGenMinable(ModBlocks.saltOre, CommonProxy.saltOreSize)).generate(world, rand, randPosX, randPosY, randPosZ);
	 	}
	}
}