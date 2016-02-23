package ru.liahim.saltmod.client;

import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.extractor.ExtractorRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    public static int saltGrassRenderType;
    public static int extractorRenderType;
    
    public static void setCustomRenderers()
    {
    	saltGrassRenderType = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new SaltGrassRenderer());
    	
    	extractorRenderType = RenderingRegistry.getNextAvailableRenderId();
    	RenderingRegistry.registerBlockHandler(new ExtractorRenderer());
    }
}