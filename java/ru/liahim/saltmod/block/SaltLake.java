package ru.liahim.saltmod.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.AchievSalt;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltLake extends Block {

	@SideOnly(Side.CLIENT)
	private IIcon TOP;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE;

	public SaltLake(String name, CreativeTabs tab) {
		super(Material.rock);
		this.setTickRandomly(true);
		this.setBlockName(name);
		this.setCreativeTab(tab);
		this.setHardness(2F);
		this.setResistance(10F);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? this.TOP : (side == 0 ? ModBlocks.saltOre.getBlockTextureFromSide(side) :
				((side == 2 && (meta == 1 || meta == 3 || meta == 5 || meta == 7 || meta == 9 || meta == 11 || meta == 13 || meta == 15)) ||
						(side == 5 && (meta == 2 || meta == 3 || meta == 6 || meta == 7 || meta == 10 || meta == 11 || meta == 14 || meta == 15)) ||
						(side == 3 && ((meta >= 4 && meta <= 7) || (meta >= 12 && meta <= 15))) ||
						(side == 4 && (meta >= 8 && meta <= 15)) ? this.SIDE : this.blockIcon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1) {
		this.blockIcon = par1.registerIcon("saltmod:SaltLake_Side");
		this.TOP = par1.registerIcon("saltmod:SaltLake_Top");
		this.SIDE = par1.registerIcon("saltmod:SaltLake_Flow");
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		if (!world.isRemote) {
			if (entity instanceof EntityLivingBase && EntityList.getEntityString(entity) != null &&
					((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList.getEntityString(entity).toLowerCase().contains("lava")) ||
							EntityList.getEntityString(entity).toLowerCase().contains("witch"))) {
				world.scheduleBlockUpdate(x, y, z, this, 0);
			}
			if (entity instanceof EntityPlayer) {
				((EntityPlayer) entity).addStat(AchievSalt.saltLake, 1);
			}
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {


			int d1 = 0;
			double d0 = 0.0625D;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1 + d0, z + 1);
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
									world.getBlock(x1, y, z1) == ModBlocks.saltBrickStair || world.getBlock(x1, y, z1) == ModBlocks.saltSlab ||
									world.getBlock(x1, y, z1) == ModBlocks.saltSlabDouble) {
								world.scheduleBlockUpdate(x1, y, z1, this, 10);
							}
						}
					}
				}
			}

			if (world.getBlock(x, y + 1, z).getMaterial() == Material.snow) {
				world.setBlockToAir(x, y + 1, z);
			} else if (world.getBlock(x, y + 1, z).getMaterial() == Material.craftedSnow ||
					world.getBlock(x, y + 1, z).getMaterial() == Material.ice) {
				world.setBlock(x, y + 1, z, Blocks.water);
			}
		}
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par2) {
		return ModItems.salt;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1 + random.nextInt(3);
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		if (fortune > 0) {
			int j = random.nextInt(fortune + 1);
			if (j > 2) {
				return 2;
			}
			return quantityDropped(random) + j;
		} else {
			return quantityDropped(random);
		}
	}

	@Override
	public int getExpDrop(IBlockAccess par1, int par2, int par3) {
		return 1;
	}

	@Override
	public MapColor getMapColor(int meta) {
		return MapColor.quartzColor;
	}
}