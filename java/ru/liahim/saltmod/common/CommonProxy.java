package ru.liahim.saltmod.common;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import ru.liahim.saltmod.SaltMod;
import ru.liahim.saltmod.api.ExtractRegistry;
import ru.liahim.saltmod.client.ClientProxy;
import ru.liahim.saltmod.extractor.ExtractorButtonMessage;
import ru.liahim.saltmod.extractor.GuiExtractorHandler;
import ru.liahim.saltmod.extractor.TileEntityExtractor;
import ru.liahim.saltmod.item.SaltFood;
import ru.liahim.saltmod.world.SaltCrystalGenerator;
import ru.liahim.saltmod.world.SaltLakeGenerator;
import ru.liahim.saltmod.world.SaltOreGenerator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CommonProxy {
	
	public static CreativeTabs saltTab = new SaltTab("saltTab");

	public static SaltOreGenerator saltOreGenerator = new SaltOreGenerator();
	public static SaltCrystalGenerator saltCrystalGenerator = new SaltCrystalGenerator();
	public static SaltLakeGenerator saltLakeGenerator = new SaltLakeGenerator();
	
	public static int saltOreFrequency;
	public static int saltOreSize;
	public static int saltLakeGroupRarity;
	public static int saltLakeQuantity;
	public static int saltLakeDistance;
	public static int saltLakeRadius;
	public static int saltCrystalGrowSpeed;
	public static int saltWortGrowSpeed;
	public static boolean mudArmorWaterDam;
	public static int mudRegenSpeed;
	public static int extractorVolume;
	public static int TFDim;
	public static boolean TFOreGen;

//TF
	public static Item saltVenisonCooked = new SaltFood("saltVenisonCooked", 9, 9.0F).setCreativeTab(saltTab).setTextureName("saltmod:TF_SaltVenisonCooked");
	public static Item saltMeefSteak = new SaltFood("saltMeefSteak", 7, 7.0F).setCreativeTab(saltTab).setTextureName("saltmod:TF_SaltMeefSteak");
	public static Item saltMeefStroganoff = new SaltFood("saltMeefStroganoff", 9, 1.0F, Items.bowl).setMaxStackSize(1).setCreativeTab(saltTab).setTextureName("saltmod:TF_SaltMeefStroganoff");
	public static Item saltHydraChop = new SaltFood("saltHydraChop", 19, 20.0F).setPotionEffect(10, 5, 0, 1.0F).setCreativeTab(saltTab).setTextureName("saltmod:TF_SaltHydraChop");
	public static Item pickledMushgloom = new SaltFood("pickledMushgloom", 4, 4.8F, Items.glass_bottle, new PotionEffect(Potion.nightVision.id, 1200, 0), new PotionEffect(Potion.moveSlowdown.id, 100, 0)).setAlwaysEdible().setMaxStackSize(1).setCreativeTab(saltTab).setTextureName("saltmod:TF_PickledMushgloom");
	public static Item saltWortVenison = new SaltFood("saltWortVenison", 10, 9.2F, Items.bowl, new PotionEffect(Potion.regeneration.id, 100, 0)).setMaxStackSize(1).setCreativeTab(saltTab).setTextureName("saltmod:TF_SaltWortVenison");
	public static Item saltWortMeefSteak = new SaltFood("saltWortMeefSteak", 8, 7.2F, Items.bowl, new PotionEffect(Potion.regeneration.id, 100, 0)).setMaxStackSize(1).setCreativeTab(saltTab).setTextureName("saltmod:TF_SaltWortMeefSteak");

	public static ArmorMaterial mudMaterial = EnumHelper.addArmorMaterial("mudMaterial", 4, new int[] {1, 1, 1, 1}, 15);

//Milk
	@SideOnly(Side.CLIENT)
	public static IIcon milkIcon;
	public static Fluid milk;
	
	public static SimpleNetworkWrapper network;
	
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		saltOreFrequency = config.getInt("SaltOreFrequency", "World", 4, 1, 10, "Salt ore frequency");
		saltOreSize = config.getInt("SaltOreSize", "World", 5, 1, 10, "Salt ore size");	
		saltLakeGroupRarity = config.getInt("SaltLakeGroupRarity", "World", 500, 1, 1000, "Rarity of the salt lake groups");
		saltLakeQuantity = config.getInt("SaltLakeQuantity", "World", 5, 1, 10, "The maximum quantity of the salt lakes in the salt lake groups");
		saltLakeDistance = config.getInt("SaltLakeDistance", "World", 30, 10, 50, "The maximum distance between the salt lakes in the salt lake groups");
		saltLakeRadius = config.getInt("SaltLakeRadius", "World", 20, 5, 50, "The maximum radius of the salt lake");
		saltCrystalGrowSpeed = config.getInt("SaltCrystalGrowRate", "Farm", 14, 1, 20, "The salt crystals growth rate (1 - fastly, 20 - slowly)");
		saltWortGrowSpeed = config.getInt("SaltWortGrowRate", "Farm", 7, 1, 20, "The saltwort growth rate (1 - fastly, 20 - slowly)");
		extractorVolume = config.getInt("SaltExtractorVolume", "Extractor", 1, 1, 3, "The number of buckets in the salt extractor");
		mudArmorWaterDam = config.getBoolean("MudArmorWaterDamage", "Armor", true, "Mud Armor water damage");
		mudRegenSpeed = config.getInt("MudRegenSpeed", "Armor", 100, 10, 100, "Speed of Mud Armor & Block regeneration effect (10 - fastly, 100 - slowly)");
		TFOreGen = config.getBoolean("TFOreGen", "TwilightForest", true, "Salt ore generation in the Twilight Forest dimention");
		config.save();
		
		SaltModEvent sEvent = new SaltModEvent();
		FMLCommonHandler.instance().bus().register(sEvent);
		MinecraftForge.EVENT_BUS.register(sEvent);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(SaltMod.instance, new GuiExtractorHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel("ExtractorChannel");
		network.registerMessage(ExtractorButtonMessage.Handler.class, ExtractorButtonMessage.class, 0, Side.SERVER);
	}
	
	public void init(FMLInitializationEvent event)
	{
		ModItems.init();
		ModBlocks.init();
		AchievSalt.init();

		Configuration configTF = new Configuration(new File("./config", "TwilightForest.cfg"));
		configTF.load();
		TFDim = configTF.get("dimension", "dimensionID", 7).getInt();

		ClientProxy.setCustomRenderers();

		GameRegistry.registerTileEntity(TileEntityExtractor.class, "tileEntityExtractor");

		GameRegistry.registerWorldGenerator(saltOreGenerator, 0);
		GameRegistry.registerWorldGenerator(saltCrystalGenerator, 10);
		GameRegistry.registerWorldGenerator(saltLakeGenerator, 15);

//Recipe
		ExtractRegistry.instance().addExtracting(FluidRegistry.WATER, ModItems.saltPinch, 1000, 0.0F);

		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPinch, 9), new ItemStack(ModItems.salt));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock, 1, 5));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock, 1, 6));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock, 1, 7));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock, 1, 8));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBlock, 1, 9));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltLamp));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.salt, 9), new ItemStack(ModBlocks.saltBrickStair));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPinch, 40), new ItemStack(ModBlocks.saltSlab, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPinch, 40), new ItemStack(ModBlocks.saltSlab, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPinch, 40), new ItemStack(ModBlocks.saltSlab, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPinch), new ItemStack(ModBlocks.saltCrystal));
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.saltDirt), new ItemStack(ModItems.salt), new ItemStack(Blocks.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.saltDirt), new ItemStack(ModBlocks.saltDirtLite), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 1, 2), new ItemStack(ModItems.saltWortSeed));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fizzyDrink), new ItemStack(ModItems.soda), new ItemStack(Items.potionitem));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.potionitem), new ItemStack(Items.glass_bottle), new ItemStack(Items.snowball));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.mineralMud), new ItemStack(ModItems.soda), new ItemStack(ModItems.salt), new ItemStack(Items.coal), new ItemStack(Items.clay_ball));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.mineralMud), new ItemStack(ModItems.soda), new ItemStack(ModItems.salt), new ItemStack(Items.coal, 1, 1), new ItemStack(Items.clay_ball));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.mineralMud, 4), new ItemStack(ModBlocks.mudBlock));

		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltBeefCooked), new ItemStack(ModItems.saltPinch), new ItemStack(Items.cooked_beef));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPorkchopCooked), new ItemStack(ModItems.saltPinch), new ItemStack(Items.cooked_porkchop));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPotatoBaked), new ItemStack(ModItems.saltPinch), new ItemStack(Items.baked_potato));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltChickenCooked), new ItemStack(ModItems.saltPinch), new ItemStack(Items.cooked_chicken));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishCod), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch), new ItemStack(Items.fish));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishCodCooked), new ItemStack(ModItems.saltPinch), new ItemStack(Items.cooked_fished));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishSalmon), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch), new ItemStack(Items.fish, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishSalmonCooked), new ItemStack(ModItems.saltPinch), new ItemStack(Items.cooked_fished, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishClownfish), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.saltPinch), new ItemStack(Items.fish, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltMushroomStew), new ItemStack(ModItems.saltPinch), new ItemStack(Items.mushroom_stew));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltMushroomStew), new ItemStack(ModItems.saltPinch), new ItemStack(Items.bowl), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltBread), new ItemStack(ModItems.saltPinch), new ItemStack(Items.bread));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltEgg), new ItemStack(ModItems.saltPinch), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pumpkinPorridge), new ItemStack(Items.bowl), new ItemStack(Blocks.pumpkin));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.vegetableStew), new ItemStack(Items.bowl), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Blocks.brown_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.vegetableStew), new ItemStack(Items.bowl), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Blocks.red_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltVegetableStew), new ItemStack(ModItems.saltPinch), new ItemStack(Items.bowl), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Blocks.brown_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltVegetableStew), new ItemStack(ModItems.saltPinch), new ItemStack(Items.bowl), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Blocks.red_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltVegetableStew), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.vegetableStew));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.potatoMushroom), new ItemStack(Items.bowl), new ItemStack(Items.potato), new ItemStack(Items.potato), new ItemStack(Blocks.brown_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.potatoMushroom), new ItemStack(Items.bowl), new ItemStack(Items.potato), new ItemStack(Items.potato), new ItemStack(Blocks.red_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPotatoMushroom), new ItemStack(Items.bowl), new ItemStack(ModItems.saltPinch), new ItemStack(Items.potato), new ItemStack(Items.potato), new ItemStack(Blocks.brown_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPotatoMushroom), new ItemStack(Items.bowl), new ItemStack(ModItems.saltPinch), new ItemStack(Items.potato), new ItemStack(Items.potato), new ItemStack(Blocks.red_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPotatoMushroom), new ItemStack(ModItems.potatoMushroom), new ItemStack(ModItems.saltPinch));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltPotatoMushroom), new ItemStack(ModItems.potatoMushroom), new ItemStack(ModItems.saltPinch));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fishSoup), new ItemStack(Items.bowl), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Items.fish));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishSoup), new ItemStack(Items.bowl), new ItemStack(ModItems.saltPinch), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Items.fish));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishSoup), new ItemStack(ModItems.fishSoup), new ItemStack(ModItems.saltPinch));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fishSalmonSoup), new ItemStack(Items.bowl), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Items.fish, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishSalmonSoup), new ItemStack(Items.bowl), new ItemStack(ModItems.saltPinch), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Items.fish, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltFishSalmonSoup), new ItemStack(ModItems.fishSalmonSoup), new ItemStack(ModItems.saltPinch));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltWortBeef), new ItemStack(Items.bowl), new ItemStack(Items.cooked_beef), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltWortPorkchop), new ItemStack(Items.bowl), new ItemStack(Items.cooked_porkchop), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.dandelionSalad), new ItemStack(Items.bowl), new ItemStack(Items.wheat_seeds), new ItemStack(Blocks.yellow_flower), new ItemStack(Blocks.red_flower, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltDandelionSalad), new ItemStack(ModItems.saltPinch), new ItemStack(Items.bowl), new ItemStack(Items.wheat_seeds), new ItemStack(Blocks.yellow_flower), new ItemStack(Blocks.red_flower, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltDandelionSalad), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.dandelionSalad));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.wheatSprouts), new ItemStack(Items.bowl), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltWheatSprouts), new ItemStack(ModItems.saltPinch), new ItemStack(Items.bowl), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltWheatSprouts), new ItemStack(ModItems.saltPinch), new ItemStack(ModItems.wheatSprouts));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fruitSalad), new ItemStack(Items.bowl), new ItemStack(Items.apple), new ItemStack(Items.carrot), new ItemStack(Items.melon));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.gratedCarrot), new ItemStack(Items.bowl), new ItemStack(Items.carrot), new ItemStack(Items.carrot), new ItemStack(Items.sugar));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.carrotPie), new ItemStack(Items.carrot), new ItemStack(Items.carrot), new ItemStack(Items.sugar), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.applePie), new ItemStack(Items.apple), new ItemStack(Items.apple), new ItemStack(Items.sugar), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.potatoPie), new ItemStack(ModItems.saltPinch), new ItemStack(Items.potato), new ItemStack(Items.potato), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.onionPie), new ItemStack(ModItems.saltPinch), new ItemStack(Blocks.red_flower, 1, 2), new ItemStack(Blocks.red_flower, 1, 2), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fishPie), new ItemStack(ModItems.saltPinch), new ItemStack(Items.wheat), new ItemStack(Items.fish), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fishSalmonPie), new ItemStack(ModItems.saltPinch), new ItemStack(Items.wheat), new ItemStack(Items.fish, 1, 1), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.mushroomPie), new ItemStack(ModItems.saltPinch), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.mushroomPie), new ItemStack(ModItems.saltPinch), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.brown_mushroom), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.mushroomPie), new ItemStack(ModItems.saltPinch), new ItemStack(Blocks.red_mushroom), new ItemStack(Blocks.red_mushroom), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pickledMushroom), new ItemStack(ModItems.saltPinch), new ItemStack(Items.potionitem), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.brown_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pickledMushroom), new ItemStack(ModItems.saltPinch), new ItemStack(Items.potionitem), new ItemStack(Blocks.red_mushroom), new ItemStack(Blocks.red_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pickledMushroom), new ItemStack(ModItems.saltPinch), new ItemStack(Items.potionitem), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pickledFern), new ItemStack(ModItems.saltPinch), new ItemStack(Items.potionitem), new ItemStack(Blocks.tallgrass, 1, 2), new ItemStack(Blocks.tallgrass, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltWortPie), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(Items.wheat), new ItemStack(Items.egg));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saltWortSalad), new ItemStack(Items.bowl), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.fermentedSaltWort), new ItemStack(Items.glass_bottle), new ItemStack(Items.ghast_tear), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.muffin), new ItemStack(ModItems.soda), new ItemStack(Items.egg), new ItemStack(Items.wheat), new ItemStack(Items.dye, 1, 3));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.milk_bucket), new ItemStack(ModItems.powderedMilk), new ItemStack(Items.water_bucket), new ItemStack(Items.bucket));

		GameRegistry.addRecipe(new ItemStack(ModItems.salt), "xxx", "xxx", "xxx", 'x', ModItems.saltPinch);
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltBlock), "xxx", "xxx", "xxx", 'x', ModItems.salt);
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltLamp), "x", "y", 'x', new ItemStack(ModBlocks.saltBlock, 1, 0), 'y', new ItemStack(Blocks.torch));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltBlock, 4, 5), "xx", "xx", 'x', new ItemStack(ModBlocks.saltBlock, 1, 0));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltBlock, 2, 2), "x", "x", 'x', new ItemStack(ModBlocks.saltBlock, 1, 0));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltBlock, 1, 1), "x", "x", 'x', new ItemStack(ModBlocks.saltSlab, 1, 0));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltBlock, 1, 8), "x", "x", 'x', new ItemStack(ModBlocks.saltSlab, 1, 1));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltBlock, 1, 9), "x", "x", 'x', new ItemStack(ModBlocks.saltSlab, 1, 2));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltBrickStair, 6), "  x", " xx", "xxx", 'x', new ItemStack(ModBlocks.saltBlock, 1, 5));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltSlab, 6, 0), "xxx", 'x', new ItemStack(ModBlocks.saltBlock, 1, 0));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltSlab, 6, 1), "xxx", 'x', new ItemStack(ModBlocks.saltBlock, 1, 5));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.saltSlab, 6, 2), "xxx", 'x', new ItemStack(ModBlocks.saltBlock, 1, 2));
		GameRegistry.addRecipe(new ItemStack(ModItems.cornedBeef), "xxx", "xyx", "xxx", 'x', ModItems.saltPinch, 'y', Items.rotten_flesh);

		GameRegistry.addRecipe(new ItemStack(ModBlocks.mudBlock), "xx", "xx", 'x', ModItems.mineralMud);
		GameRegistry.addRecipe(new ItemStack(ModItems.mudHelmet), "xxx", "x x", 'x', ModItems.mineralMud);
		GameRegistry.addRecipe(new ItemStack(ModItems.mudChestplate), "x x", "xxx", "xxx", 'x', ModItems.mineralMud);
		GameRegistry.addRecipe(new ItemStack(ModItems.mudLeggings), "xxx", "x x", "x x", 'x', ModItems.mineralMud);
		GameRegistry.addRecipe(new ItemStack(ModItems.mudBoots), "x x", "x x", 'x', ModItems.mineralMud);

		GameRegistry.addRecipe(new ItemStack(ModBlocks.extractor), "xyx", "x x", "xxx", 'x', Blocks.cobblestone, 'y', Items.cauldron);

		GameRegistry.addSmelting(ModBlocks.saltOre, new ItemStack(ModItems.salt, 1), 0.7F);
		GameRegistry.addSmelting(ModBlocks.saltLake, new ItemStack(ModItems.salt, 1), 0.7F);
		GameRegistry.addSmelting(new ItemStack(ModBlocks.saltBlock, 1, 0), new ItemStack(ModBlocks.saltBlock, 1, 6), 0.0F);
		GameRegistry.addSmelting(new ItemStack(ModBlocks.saltBlock, 1, 5), new ItemStack(ModBlocks.saltBlock, 1, 7), 0.0F);
		GameRegistry.addSmelting(ModItems.saltWortSeed, new ItemStack(ModItems.soda, 1), 0.0F);

//Chest Content
		ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(ModItems.salt), 2, 5, 5));
		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ModItems.salt), 2, 5, 5));
		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ModItems.saltWortSeed), 2, 3, 3));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(ModItems.salt), 2, 5, 5));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(new ItemStack(ModItems.saltWortSeed), 2, 5, 5));
		ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(new ItemStack(ModItems.salt), 2, 5, 10));
		ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(new ItemStack(ModItems.salt), 2, 5, 10));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(ModItems.saltWortSeed), 2, 3, 3));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(ModItems.saltWortSeed), 2, 5, 5));

//OreDictionary
		OreDictionary.registerOre("oreSalt", ModBlocks.saltOre);
		OreDictionary.registerOre("blockSalt", ModBlocks.saltBlock);
		OreDictionary.registerOre("blockSaltCrystal", ModBlocks.saltCrystal);
		OreDictionary.registerOre("lumpSalt", ModItems.salt);
		OreDictionary.registerOre("dustSalt", ModItems.saltPinch);
		OreDictionary.registerOre("dustSoda", ModItems.soda);
		OreDictionary.registerOre("dustMilk", ModItems.powderedMilk);
		OreDictionary.registerOre("cropSaltwort", ModItems.saltWortSeed);
		OreDictionary.registerOre("materialMineralMud", ModItems.mineralMud);
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
//TF Items & Recipe
    	Item venisonCooked = GameRegistry.findItem("TwilightForest", "item.venisonCooked");
    	if (venisonCooked != null){
		GameRegistry.registerItem(saltVenisonCooked, "saltVenisonCooked");
		GameRegistry.registerItem(saltWortVenison, "saltWortVenison");
    		GameRegistry.addShapelessRecipe(new ItemStack(saltVenisonCooked),
		new Object[] {new ItemStack(ModItems.saltPinch), new ItemStack(venisonCooked)});
    		GameRegistry.addShapelessRecipe(new ItemStack(saltWortVenison),
    		new Object[] {new ItemStack(venisonCooked), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(Items.bowl)});}
	Item meefSteak = GameRegistry.findItem("TwilightForest", "item.meefSteak");
    	if (meefSteak != null){
    		GameRegistry.registerItem(saltMeefSteak, "saltMeefSteak");
    		GameRegistry.registerItem(saltWortMeefSteak, "saltWortMeefSteak");
    		GameRegistry.addShapelessRecipe(new ItemStack(saltMeefSteak),
		new Object[] {new ItemStack(ModItems.saltPinch), new ItemStack(meefSteak)});
    		GameRegistry.addShapelessRecipe(new ItemStack(saltWortVenison),
		new Object[] {new ItemStack(meefSteak), new ItemStack(ModItems.saltWortSeed), new ItemStack(ModItems.saltWortSeed), new ItemStack(Items.bowl)});}
	Item meefStroganoff = GameRegistry.findItem("TwilightForest", "item.meefStroganoff");
    	if (meefStroganoff != null){
    		GameRegistry.registerItem(saltMeefStroganoff, "saltMeefStroganoff");
    		GameRegistry.addShapelessRecipe(new ItemStack(saltMeefStroganoff),
		new Object[] {new ItemStack(ModItems.saltPinch), new ItemStack(meefStroganoff)});}
	Item hydraChop = GameRegistry.findItem("TwilightForest", "item.hydraChop");
    	if (hydraChop != null){
    		GameRegistry.registerItem(saltHydraChop, "saltHydraChop");
    		GameRegistry.addShapelessRecipe(new ItemStack(saltHydraChop),
		new Object[] {new ItemStack(ModItems.saltPinch), new ItemStack(hydraChop)});}
	Block mushgloom = GameRegistry.findBlock("TwilightForest", "tile.TFPlant");
    	if (mushgloom != null){
    		GameRegistry.registerItem(pickledMushgloom, "pickledMushgloom");
    		GameRegistry.addShapelessRecipe(new ItemStack(pickledMushgloom),
    	    new Object[] {new ItemStack(ModItems.saltPinch), new ItemStack(Items.potionitem), new ItemStack(mushgloom, 1, 9), new ItemStack(mushgloom, 1, 9)});}
//Milk Registry
    	if (FluidRegistry.isFluidRegistered("milk"))
    	{
    		Fluid milk = FluidRegistry.getFluid("milk");
    		ExtractRegistry.instance().addExtracting(milk, ModItems.powderedMilk, 1000, 0.0F);
    	}
    	else
    	{
    		milk = new Fluid("milk");
    		FluidRegistry.registerFluid(milk);
    		FluidContainerRegistry.registerFluidContainer(new FluidStack(milk, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Items.milk_bucket), FluidContainerRegistry.EMPTY_BUCKET);
    		ExtractRegistry.instance().addExtracting(milk, ModItems.powderedMilk, 1000, 0.0F);
    	}
    }
}
