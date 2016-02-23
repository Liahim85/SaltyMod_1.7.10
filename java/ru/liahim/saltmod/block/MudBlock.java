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
import ru.liahim.saltmod.common.ModItems;

public class MudBlock extends BlockFalling {
	
	private static int tick = 0;
	
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
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        float f = 0.125F;
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)((float)(y + 1) - f), (double)(z + 1));
    }
	
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
		entity.motionX *= 0.4D;
		entity.motionZ *= 0.4D;
    }
	
    public Item getItemDropped(int meta, Random rand, int fortune)
    {
        return ModItems.mineralMud;
    }

    public int quantityDropped(Random rand)
    {
        return 4;
    }
    
    public MapColor getMapColor(int meta)
    {
        return MapColor.grayColor;
    }
}