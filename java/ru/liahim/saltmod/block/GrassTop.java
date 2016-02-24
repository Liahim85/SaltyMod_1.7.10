package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GrassTop extends Block {

	public GrassTop(String name, CreativeTabs tab) {
		super(Material.grass);
		this.setBlockName(name);
		this.setCreativeTab(tab);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return side > 1 ? Blocks.grass.getIconSideOverlay() : this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("minecraft:grass_top");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
	{
		if (side > 0) return super.shouldSideBeRendered(access, x, y, z, side);
		else return false;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		double d0 = 0.5D;
		double d1 = 1.0D;
		return ColorizerGrass.getGrassColor(d0, d1);
	}
    
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int p_149741_1_)
	{
		return this.getBlockColor();
	}
	
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		int l = 0;
		int i = 0;
		int j = 0;

		for (int k1 = -1; k1 <= 1; ++k1)
		{
			for (int l1 = -1; l1 <= 1; ++l1)
			{
				int i2 = world.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor(x + l1, y, z + k1);
				l += (i2 & 16711680) >> 16;
				i += (i2 & 65280) >> 8;
				j += i2 & 255;
			}
		}

		return (l / 9 & 255) << 16 | (i / 9 & 255) << 8 | j / 9 & 255;
	}
    
	public Item getItemDropped(int meta, Random random, int fortune)
	{
		return null;
	}
	
	protected boolean canSilkHarvest()
	{
		return false;
	}
}
