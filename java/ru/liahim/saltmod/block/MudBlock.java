package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.ModItems;

public class MudBlock extends BlockFalling {

	public MudBlock(String name, CreativeTabs tab) {
		super(Material.ground);
		this.setBlockName(name);
		this.setStepSound(soundTypeGravel);
		this.setCreativeTab(tab);
		this.setHardness(0.5F);
		this.setResistance(1F);
		this.setHarvestLevel("shovel", 0);
		this.setBlockTextureName("saltmod:MudBlock");
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		float f = 0.125F;
		return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1 - f, z + 1);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		entity.motionX *= 0.4D;
		entity.motionZ *= 0.4D;
	}
	
	@Override
	public Item getItemDropped(int meta, Random rand, int fortune)
	{
		return ModItems.mineralMud;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 4;
	}
    
	@Override
	public MapColor getMapColor(int meta)
	{
		return MapColor.grayColor;
	}
}
