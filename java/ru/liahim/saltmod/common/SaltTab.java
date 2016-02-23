package ru.liahim.saltmod.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltTab extends CreativeTabs {

	public SaltTab(String lable) {
		super(lable);
	}

	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(ModBlocks.saltOre);
	}	
}