package ru.liahim.saltmod.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AchievItem extends Item 
{
	private IIcon[] icon;

	public AchievItem(String name, CreativeTabs tab) 
	{
		super();
		this.setCreativeTab(tab);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.icon = new IIcon[3];
		for (int i = 0; i < this.icon.length; ++i)
		{
			this.icon[i] = reg.registerIcon("saltmod:AchievIcon_" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return this.icon[meta];
	}
}