package ru.liahim.saltmod.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.ModBlocks;

public class SaltLamp extends Block {

	public SaltLamp(String name, CreativeTabs tab) {
		super(Material.rock);
		this.setTickRandomly(true);
		this.setBlockName(name);
		this.setCreativeTab(tab);
		this.setHardness(5F);
		this.setResistance(10F);
		this.setLightLevel(0.9F);
		this.setHarvestLevel("pickaxe", 1);
		this.setBlockTextureName("saltmod:SaltLamp");
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		if (!world.isRemote) {
			if (entity instanceof EntityLivingBase && EntityList.getEntityString(entity) != null &&
					((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList.getEntityString(entity).toLowerCase().contains("lava")) ||
							EntityList.getEntityString(entity).toLowerCase().contains("witch"))) {
				world.scheduleBlockUpdate(x, y, z, this, 0);
			}
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (!world.isRemote) {
			int d1 = 0;
			double d0 = 0.0625D;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(d0, d0, d0);
			List list = world.getEntitiesWithinAABB(Entity.class, axisalignedbb);
			Iterator iterator = list.iterator();
			Entity entity;
			while (iterator.hasNext()) {
				entity = (Entity) iterator.next();
				if (entity instanceof EntityLivingBase && EntityList.getEntityString(entity) != null &&
						((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList.getEntityString(entity).toLowerCase().contains("lava")) ||
								EntityList.getEntityString(entity).toLowerCase().contains("witch"))) {
					entity.attackEntityFrom(DamageSource.cactus, 1.0F);
					d1 = 3;
				}
				if (d1 > 0) {
					d1 = d1 - 1;
					for (int x1 = x - 1; x1 < x + 2; x1++) {
						for (int z1 = z - 1; z1 < z + 2; z1++) {
							if (world.getBlock(x1, y, z1) == ModBlocks.saltBlock || world.getBlock(x1, y, z1) == ModBlocks.saltLamp ||
									world.getBlock(x1, y, z1) == ModBlocks.saltLake || world.getBlock(x1, y, z1) == ModBlocks.saltDirt ||
									world.getBlock(x1, y, z1) == ModBlocks.saltBrickStair) {
								world.scheduleBlockUpdate(x1, y, z1, this, 10);
							}
						}
					}
				}
			}
			for (int x2 = x - 1; x2 < x + 2; x2++) {
				for (int y2 = y - 1; y2 < y + 2; y2++) {
					for (int z2 = z - 1; z2 < z + 2; z2++) {
						if ((world.getBlock(x2, y2, z2) == Blocks.ice || world.getBlock(x2, y2, z2) == Blocks.snow ||
								(world.getBlock(x2, y2, z2) == Blocks.snow_layer && y2 != y - 1)) &&
								((x2 - 1 == x && (world.getBlock(x2 - 1, y2, z2) == this || world.getBlock(x2 - 1, y2, z2).getMaterial() == Material.water)) ||
										(x2 + 1 == x && (world.getBlock(x2 + 1, y2, z2) == this || world.getBlock(x2 + 1, y2, z2).getMaterial() == Material.water)) ||
										(y2 - 1 == y && (world.getBlock(x2, y2 - 1, z2) == this || world.getBlock(x2, y2 - 1, z2).getMaterial() == Material.water)) ||
										(y2 + 1 == y && (world.getBlock(x2, y2 + 1, z2) == this || world.getBlock(x2, y2 + 1, z2).getMaterial() == Material.water)) ||
										(z2 - 1 == z && (world.getBlock(x2, y2, z2 - 1) == this || world.getBlock(x2, y2, z2 - 1).getMaterial() == Material.water)) ||
										(z2 + 1 == z && (world.getBlock(x2, y2, z2 + 1) == this || world.getBlock(x2, y2, z2 + 1).getMaterial() == Material.water))))
						{
							world.scheduleBlockUpdate(x, y, z, this, 5);
							if (rand.nextInt(20) == 0) {
								if (world.getBlock(x2, y2, z2) == Blocks.ice || world.getBlock(x2, y2, z2) == Blocks.snow) {
									world.setBlock(x2, y2, z2, Blocks.water);
								}
								if (world.getBlock(x2, y2, z2) == Blocks.snow_layer) {
									world.setBlockToAir(x2, y2, z2);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public MapColor getMapColor(int meta) {
		return MapColor.quartzColor;
	}
}