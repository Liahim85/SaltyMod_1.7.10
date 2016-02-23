package ru.liahim.saltmod;

import ru.liahim.saltmod.common.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid=SaltMod.MODID, name=SaltMod.NAME, version=SaltMod.VERSION)

public class SaltMod {
	
    public static final String MODID = "SaltMod";
    public static final String NAME = "Salty Mod";
    public static final String VERSION = "1.7.d";
	
	@Instance(MODID)
    public static SaltMod instance;

	@SidedProxy(clientSide="ru.liahim.saltmod.client.ClientProxy", serverSide="ru.liahim.saltmod.common.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
	{
		this.proxy.preInit(event);
	}
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	this.proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	this.proxy.postInit(event);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event){}
}