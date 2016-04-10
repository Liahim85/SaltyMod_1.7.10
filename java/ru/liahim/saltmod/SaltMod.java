package ru.liahim.saltmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.SaltConfig;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=SaltMod.MODID, name=SaltMod.NAME, version=SaltMod.VERSION)

public class SaltMod {

	public static final String MODID = "SaltMod";
	public static final String NAME = "Salty Mod";
	public static final String VERSION = "1.8.a";
	public static final Logger logger = LogManager.getLogger(NAME);

    public static SaltConfig config;

	@Mod.Instance(MODID)
	public static SaltMod instance;

	@SidedProxy(clientSide = "ru.liahim.saltmod.common.ClientProxy", serverSide = "ru.liahim.saltmod.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Starting SaltMod PreInitialization");
        config = new SaltConfig(event.getSuggestedConfigurationFile());
        config.preInit();
        ModItems.init();
        ModBlocks.init();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
        config.init();
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
        config.postInit();
		proxy.postInit(event);
	}
}