package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import ru.liahim.saltmod.common.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltOre extends Block {
	
	@SideOnly(Side.CLIENT)
	private IIcon SIDE;

	public SaltOre(String name, CreativeTabs tab) {
		super(Material.rock);
		this.setBlockName(name);
		this.setCreativeTab(tab);
		this.setHardness(2F);
		this.setResistance(10F);
		this.setHarvestLevel("pickaxe", 2);
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	return side == 1 && meta > 0 ? Blocks.stone.getBlockTextureFromSide(side) : (side == 2 && (meta == 1 || meta == 3 || meta == 5 || meta == 7 || meta == 9 || meta == 11 || meta == 13 || meta == 15)) ||
    	(side == 5 && (meta == 2 || meta == 3 || meta == 6 || meta == 7 || meta == 10 || meta == 11 || meta == 14 || meta == 15)) ||
    	(side == 3 && ((meta >= 4 && meta <= 7) || (meta >= 12 && meta <= 15))) ||
    	(side == 4 && (meta >= 8 && meta <= 15)) ? this.SIDE : this.blockIcon;
    }
	
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1)
    {
    	this.blockIcon = par1.registerIcon("saltmod:SaltOre");
        this.SIDE = par1.registerIcon("saltmod:SaltOre_Side");
    }
	
	  @Override
	    public Item getItemDropped(int par1, Random random, int par2)
	    {
	        return ModItems.salt;
	    }
	  
	  @Override
	  	public int quantityDropped(Random random) {
		    	
		    return 1 + random.nextInt(3);
	  }
		
	  @Override
		public int quantityDroppedWithBonus(int fortune, Random random) {
		  
		        if (fortune > 0)
		        {
		            int j = random.nextInt(fortune + 1);
		            
		            if (j > 2)
		            {
		            	return j = 2;
		            }

		            return quantityDropped(random) + j;
		        }
		        
		        else
		        {
		            return quantityDropped(random);
		        }
	    	
	    }
	  
	@Override
	public int getExpDrop(IBlockAccess par1, int par2, int par3)
	{
	return 1;
	}
}
