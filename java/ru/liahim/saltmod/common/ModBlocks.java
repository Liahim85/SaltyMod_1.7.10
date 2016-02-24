package ru.liahim.saltmod.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.creativetab.CreativeTabs;
import ru.liahim.saltmod.block.GrassTop;
import ru.liahim.saltmod.block.MudBlock;
import ru.liahim.saltmod.block.SaltBlock;
import ru.liahim.saltmod.block.SaltBrickStair;
import ru.liahim.saltmod.block.SaltCrystal;
import ru.liahim.saltmod.block.SaltDirt;
import ru.liahim.saltmod.block.SaltDirtLite;
import ru.liahim.saltmod.block.SaltGrass;
import ru.liahim.saltmod.block.SaltLake;
import ru.liahim.saltmod.block.SaltLamp;
import ru.liahim.saltmod.block.SaltOre;
import ru.liahim.saltmod.block.SaltSlab;
import ru.liahim.saltmod.block.SaltWort;
import ru.liahim.saltmod.extractor.Extractor;
import ru.liahim.saltmod.item.ItemSaltBlock;
import ru.liahim.saltmod.item.ItemSaltDirt;
import ru.liahim.saltmod.item.ItemSaltSlab;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	static CreativeTabs tab = CommonProxy.saltTab;

	public static Block saltOre = new SaltOre("saltOre", tab);
	public static Block saltLake = new SaltLake("saltLake", tab);
	public static Block saltBlock = new SaltBlock(tab);
	public static BlockStairs saltBrickStair = new SaltBrickStair("saltBrickStair", tab);
	public static BlockSlab saltSlab = new SaltSlab(false, "saltSlab", tab);
	public static BlockSlab saltSlabDouble = new SaltSlab(true, "saltSlabDouble", null);
	public static Block saltLamp = new SaltLamp("saltLamp", tab);
	public static Block saltDirt = new SaltDirt(tab);
	public static Block saltDirtLite = new SaltDirtLite("saltDirtLite", tab);
	public static Block saltGrass = new SaltGrass("saltGrass", tab);
	public static Block grassTop = new GrassTop("grassTop", null);
	public static Block mudBlock = new MudBlock("mudBlock", tab);
	public static Block extractor = new Extractor(false, false, "extractor", tab);
	public static Block extractorLit = new Extractor(true, false, "extractor", null);
	public static Block extractorSteam = new Extractor(true, true, "extractor", null);
	public static Block saltCrystal = new SaltCrystal("saltCrystal", tab);
	public static Block saltWort = new SaltWort("saltWort", null);
	
	public static final void init()
	{
		GameRegistry.registerBlock(saltOre, "saltOre");
		GameRegistry.registerBlock(saltLake, "saltLake");
		GameRegistry.registerBlock(saltBlock, ItemSaltBlock.class, "saltBlock");
		GameRegistry.registerBlock(saltBrickStair, "saltBrickStair");
		GameRegistry.registerBlock(saltSlab, ItemSaltSlab.class, "saltSlab");
		GameRegistry.registerBlock(saltSlabDouble, ItemSaltSlab.class, "saltSlabDouble");
		GameRegistry.registerBlock(saltLamp, "saltLamp");
		GameRegistry.registerBlock(saltDirt, ItemSaltDirt.class, "saltDirt");
		GameRegistry.registerBlock(saltDirtLite, "saltDirtLite");
		GameRegistry.registerBlock(saltGrass, "saltGrass");
		GameRegistry.registerBlock(grassTop, "grassTop");
		GameRegistry.registerBlock(mudBlock, "mudBlock");
		GameRegistry.registerBlock(extractor, "extractor");
		GameRegistry.registerBlock(extractorLit, "extractorLit").setLightLevel(0.9F);
		GameRegistry.registerBlock(extractorSteam, "extractorSteam").setLightLevel(0.9F);
		GameRegistry.registerBlock(saltCrystal, "saltCrystal");
		GameRegistry.registerBlock(saltWort, "saltWort");
	}
}
