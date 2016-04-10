package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.AchievSalt;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltDirtLite extends Block {
	
	@SideOnly(Side.CLIENT)
	private IIcon SIDE;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE_L;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE_R;	
	@SideOnly(Side.CLIENT)
	private IIcon BOTTOM;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE_1;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE_2;
	
	public SaltDirtLite(String name, CreativeTabs tab) {
		super(Material.ground);
		this.setTickRandomly(true);
		this.setStepSound(soundTypeGravel);
		this.setBlockName(name);
		this.setCreativeTab(tab);
		this.setHardness(0.5F);
		this.setResistance(1F);
		this.setHarvestLevel("shovel", 0);
	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	return meta == 1 ? this.SIDE_1 : meta == 2 ? this.SIDE_2 : (side == 0 && meta >= 7) ? this.BOTTOM : 
    		((side == 2 && (meta == 7 || meta == 11 || meta == 14 || meta == 15)) ||
        	 (side == 5 && (meta == 8 || meta == 11 || meta == 12 || meta == 15)) ||
        	 (side == 3 && (meta == 9 || meta == 12 || meta == 13 || meta == 15)) ||
        	 (side == 4 && (meta == 10 || meta == 13 || meta == 14 || meta == 15)) ? this.SIDE :
            ((side == 2 && (meta == 3 || meta == 8 || meta == 12)) || (side == 5 && (meta == 4 || meta == 9 || meta == 13)) ||
             (side == 3 && (meta == 5 || meta == 10 || meta == 14)) || (side == 4 && (meta == 6 || meta == 7 || meta == 11)) ? this.SIDE_L :
            ((side == 2 && (meta == 6 || meta == 10 || meta == 13)) || (side == 5 && (meta == 3 || meta == 7 || meta == 14)) ||
             (side == 3 && (meta == 4 || meta == 8 || meta == 11)) || (side == 4 && (meta == 5 || meta == 9 || meta == 12)) ? this.SIDE_R : this.blockIcon)));
    }
	
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1)
    {
    	this.blockIcon = par1.registerIcon("saltmod:SaltDirtLite_0");
    	this.SIDE_1 = par1.registerIcon("saltmod:SaltDirtLite_1");
    	this.SIDE_2 = par1.registerIcon("saltmod:SaltDirtLite_2");
        this.SIDE = par1.registerIcon("saltmod:SaltDirtLite_Side");
        this.SIDE_L = par1.registerIcon("saltmod:SaltDirtLite_Side_L");
        this.SIDE_R = par1.registerIcon("saltmod:SaltDirtLite_Side_R");
        this.BOTTOM = par1.registerIcon("saltmod:SaltDirtLite_Bottom");
    }
    
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
        if (!world.isRemote) {
        	if (world.getBlock(x, y + 1, z).getMaterial() == Material.snow)
        	{
        		world.setBlockToAir(x, y + 1, z);
        	}
		
        	else if (!world.getBlock(x, y + 1, z).getMaterial().isSolid() && world.getFullBlockLightValue(x, y + 1, z) > 7)
        	{
        		int j = world.getBlockMetadata(x, y, z);
        		if (j > 2)
        		{
        			for (int x1 = x - 1; x1 < x + 2; x1++) {
        			for (int z1 = z - 1; z1 < z + 2; z1++) {
				
        				if ((world.getBlock(x1, y, z1) == Blocks.grass || world.getBlock(x1, y, z1) == ModBlocks.saltGrass) &&
        					 world.getBlock(x, y, z) == ModBlocks.saltDirtLite && world.getBlockLightValue(x, y + 1, z) > 7 && rand.nextInt(5) == 0)
						
        					{world.setBlock(x, y, z, ModBlocks.saltGrass, j, 3);}
        			}
        			}
        		}
        	}
        }
	}
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz)
    {
		boolean S = (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.saltPinch);
		ItemStack current = player.getCurrentEquippedItem();
		
			if (S)
			{
				if (world.getBlock(x, y + 1, z) == ModBlocks.saltWort) {player.addStat(AchievSalt.saltWortFarm, 1);}
			
				if (world.getBlockMetadata(x, y, z) == 0 || world.getBlockMetadata(x, y, z) > 2)
				{world.setBlock(x, y, z, this, 1, 3); if (!player.capabilities.isCreativeMode){--current.stackSize;}}
				else if (world.getBlockMetadata(x, y, z) == 1)
				{world.setBlock(x, y, z, this, 2, 3); if (!player.capabilities.isCreativeMode){--current.stackSize;}}
				else if (world.getBlockMetadata(x, y, z) == 2)
				{world.setBlock(x, y, z, ModBlocks.saltDirt); if (!player.capabilities.isCreativeMode){--current.stackSize;}}
				return true;
			}
			
        return false;
    }
}
