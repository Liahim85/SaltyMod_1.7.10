package ru.liahim.saltmod.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.common.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltSlab extends BlockSlab {
	
	protected boolean crystal = true;
	public static final String[] type = {"Block", "Brick", "Pillar"};

	@SideOnly(Side.CLIENT)
    private IIcon SALTBLOCK;
	@SideOnly(Side.CLIENT)
    private IIcon SALTBLOCKSIDE;
	@SideOnly(Side.CLIENT)
    private IIcon BRICKTOP;
	@SideOnly(Side.CLIENT)
    private IIcon BRICKBOTTOM;
	@SideOnly(Side.CLIENT)
    private IIcon LINES;
	@SideOnly(Side.CLIENT)
    private IIcon LINESTOP;
	
	public SaltSlab(boolean isDouble, String name, CreativeTabs tab) {
		super(isDouble, Material.rock);
		this.setTickRandomly(true);
		this.setBlockName(name);
		this.setCreativeTab(tab);
        this.setHardness(5F);
		this.setResistance(10F);
		this.setHarvestLevel("pickaxe", 1);
		useNeighborBrightness = true;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta < 0 || (meta > 2 && meta < 8) || meta > 10){meta = 0;}
		
		if (meta == 0 || meta == 8){
			if (side == 0 || side == 1) {return this.SALTBLOCK;}
			else {return this.SALTBLOCKSIDE;}}
		else if (meta == 2 || meta == 10){
			if (side == 0 || side == 1) {return this.LINESTOP;}
			else {return this.LINES;}}
		else {
			if (side == 0) {return this.BRICKBOTTOM;}
			else if (side == 1) {return this.BRICKTOP;}
			else {return this.blockIcon;}}
	}
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1)
    {
    	this.blockIcon = par1.registerIcon("saltmod:SaltBrick");
    	this.BRICKTOP = par1.registerIcon("saltmod:SaltBrick_Top");
    	this.BRICKBOTTOM = par1.registerIcon("saltmod:SaltBrick_Bottom");
    	this.SALTBLOCK = par1.registerIcon("saltmod:SaltBlock");
    	this.SALTBLOCKSIDE = par1.registerIcon("saltmod:SaltSlab");
    	this.LINES = par1.registerIcon("saltmod:SaltBlock_Lines");
    	this.LINESTOP = par1.registerIcon("saltmod:SaltBlock_Lines_Top");
    }
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
	{
		if (item != Item.getItemFromBlock(ModBlocks.saltSlabDouble))
		{
			list.add(new ItemStack(item, 1, 0));
			list.add(new ItemStack(item, 1, 1));
			list.add(new ItemStack(item, 1, 2));
		}
	}
	
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(ModBlocks.saltSlab);
    }
    
    @Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity)
    {
    	if (!world.isRemote) {
		if (entity instanceof EntityLivingBase && EntityList.getEntityString(entity) != null &&
		  ((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList.getEntityString(entity).toLowerCase().contains("lava")) ||
			EntityList.getEntityString(entity).toLowerCase().contains("witch")))
		{world.scheduleBlockUpdate(x, y, z, this, 0);}
    	}
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
		if (!world.isRemote) {
		if (entity instanceof EntityLivingBase && EntityList.getEntityString(entity) != null &&
		  ((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList.getEntityString(entity).toLowerCase().contains("lava")) ||
			EntityList.getEntityString(entity).toLowerCase().contains("witch")))
		{world.scheduleBlockUpdate(x, y, z, this, 0);}
		}
    }
	
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if (!world.isRemote) {
		
/*Эффект слизням и ведьмам*/
		
		int d1 = 0;
		double d0 = 0.0625D;
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1)).expand(d0, d0, d0);
		List list = world.getEntitiesWithinAABB(Entity.class, axisalignedbb);
		Iterator iterator = list.iterator();
		Entity entity;

		while(iterator.hasNext()) {
		    entity = (Entity)iterator.next();

		if (entity instanceof EntityLivingBase && EntityList.getEntityString(entity) != null &&
		  ((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList.getEntityString(entity).toLowerCase().contains("lava")) ||
			EntityList.getEntityString(entity).toLowerCase().contains("witch")))
		{entity.attackEntityFrom(DamageSource.cactus, 1.0F); d1 = 3;}
		if (d1 > 0) {d1 = d1 - 1;
			for (int x1 = x - 1; x1 < x + 2; x1++) {
		    for (int z1 = z - 1; z1 < z + 2; z1++) {
		    	if (world.getBlock(x1, y, z1) == ModBlocks.saltBlock || world.getBlock(x1, y, z1) == ModBlocks.saltLamp ||
    			    world.getBlock(x1, y, z1) == ModBlocks.saltLake || world.getBlock(x1, y, z1) == ModBlocks.saltDirt ||
		    		world.getBlock(x1, y, z1) == ModBlocks.saltBrickStair || world.getBlock(x1, y, z1) == ModBlocks.saltSlab ||
		    		world.getBlock(x1, y, z1) == ModBlocks.saltSlabDouble)
		    	{world.scheduleBlockUpdate(x1, y, z1, this, 10);}
		    }
		    }
		}
		}
		
/*Образование кристаллов*/
		
		if ((world.getBlock(x+1, y+1, z) == ModBlocks.saltBlock || world.getBlock(x+1, y+1, z) == ModBlocks.saltSlabDouble) &&
			(world.getBlock(x-1, y+1, z) == ModBlocks.saltBlock || world.getBlock(x-1, y+1, z) == ModBlocks.saltSlabDouble) &&
			(world.getBlock(x, y+1, z+1) == ModBlocks.saltBlock || world.getBlock(x, y+1, z+1) == ModBlocks.saltSlabDouble) &&
			(world.getBlock(x, y+1, z-1) == ModBlocks.saltBlock || world.getBlock(x, y+1, z-1) == ModBlocks.saltSlabDouble) &&
			(world.getBlock(x+1, y+1, z+1).getMaterial() == Material.water) &&
			(world.getBlock(x+1, y+1, z-1).getMaterial() == Material.water) &&
			(world.getBlock(x-1, y+1, z+1).getMaterial() == Material.water) &&
			(world.getBlock(x-1, y+1, z-1).getMaterial() == Material.water) &&
			(world.getFullBlockLightValue(x, y+1, z) < 15) && world.getBlock(x, y, z) == ModBlocks.saltSlabDouble)
		{
			if (rand.nextInt(CommonProxy.saltCrystalGrowSpeed) == 0 && crystal)
			{
				if(world.getBlock(x, y+1, z) == Blocks.air)
				{
				world.setBlock(x, y+1, z, ModBlocks.saltCrystal, 2, 3);
				}
				
				else if (world.getBlock(x, y+1, z) == ModBlocks.saltCrystal && world.getBlockMetadata(x, y+1, z) == 2)
				{
				world.setBlock(x, y+1, z, ModBlocks.saltCrystal, 1, 3);
				}
				
				else if (world.getBlock(x, y+1, z) == ModBlocks.saltCrystal && world.getBlockMetadata(x, y+1, z) == 1)
				{
				world.setBlock(x, y+1, z, ModBlocks.saltCrystal);
				}
			}
			crystal = true;
		}
	
/*Таяние льда*/
		
		int yUP;
		int yDOWN;		
		boolean DOWN;
		
		if (world.getBlock(x, y, z) == ModBlocks.saltSlabDouble){yUP = 2; yDOWN = 1; DOWN = true;}
		else if (world.doesBlockHaveSolidTopSurface(world, x, y, z)){yUP = 2; yDOWN = 0; DOWN = false;}
		else {yUP = 1; yDOWN = 1; DOWN = true;}
		
		for (int x2 = x - 1; x2 < x + 2; x2++) {
		for (int y2 = y - yDOWN; y2 < y + yUP; y2++) {
		for (int z2 = z - 1; z2 < z + 2; z2++) {
			if ((world.getBlock(x2, y2, z2) == Blocks.ice || world.getBlock(x2, y2, z2) == Blocks.snow ||
				(world.getBlock(x2, y2, z2) == Blocks.snow_layer && y2 != y-1 && DOWN)) &&
				((x2-1 == x && (world.getBlock(x2-1, y2, z2) == this || world.getBlock(x2-1, y2, z2).getMaterial() == Material.water)) ||
				 (x2+1 == x && (world.getBlock(x2+1, y2, z2) == this || world.getBlock(x2+1, y2, z2).getMaterial() == Material.water)) ||
				 (y2-1 == y && (world.getBlock(x2, y2-1, z2) == this || world.getBlock(x2, y2-1, z2).getMaterial() == Material.water)) ||
				 (y2+1 == y && (world.getBlock(x2, y2+1, z2) == this || world.getBlock(x2, y2+1, z2).getMaterial() == Material.water)) ||
				 (z2-1 == z && (world.getBlock(x2, y2, z2-1) == this || world.getBlock(x2, y2, z2-1).getMaterial() == Material.water)) ||
				 (z2+1 == z && (world.getBlock(x2, y2, z2+1) == this || world.getBlock(x2, y2, z2+1).getMaterial() == Material.water))))
				
			{crystal = false; world.scheduleBlockUpdate(x, y, z, this, 5);
			
				if (rand.nextInt(20) == 0)
				{
					if(world.getBlock(x2, y2, z2) == Blocks.ice || world.getBlock(x2, y2, z2) == Blocks.snow)
					{world.setBlock(x2, y2, z2, Blocks.water); crystal = true;}
					if(world.getBlock(x2, y2, z2) == Blocks.snow_layer)
					{world.setBlockToAir(x2, y2, z2); crystal = true;}
				}
			}
		}
		}
		}
		}
	}
	
	protected boolean canSilkHarvest()
	{
		return false;
	}
	
	@Override
	public String func_150002_b(int par1)
	{
		if (par1 < 0 || par1 >= type.length)
        {
			par1 = 0;
        }

        return super.getUnlocalizedName() + "." + type[par1];
	}
	
    public MapColor getMapColor(int meta)
    {
        return MapColor.quartzColor;
    }
}