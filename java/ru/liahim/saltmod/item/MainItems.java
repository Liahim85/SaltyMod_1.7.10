package ru.liahim.saltmod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MainItems extends Item {
	
	public MainItems(String name, CreativeTabs tab, String textureName) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(tab);
		this.setTextureName("saltmod:" + textureName);
	}
}