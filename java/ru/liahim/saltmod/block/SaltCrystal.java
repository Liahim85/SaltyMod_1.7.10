package ru.liahim.saltmod.block;

import java.util.ArrayList;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.liahim.saltmod.init.AchievSalt;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModItems;
import ru.liahim.saltmod.init.SaltConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltCrystal extends BlockBush {

	protected boolean silkdrop = false;

	public SaltCrystal(String name, CreativeTabs tab) {
		this.setBlockName(name);
		this.setCreativeTab(tab);
		this.setBlockTextureName("saltmod:SaltCrystal");
	}

	@SideOnly(Side.CLIENT)
	private IIcon SMALL;
	@SideOnly(Side.CLIENT)
	private IIcon MEDIUM;

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta < 0 || meta > 2) {
			meta = 0;
		}
		return meta == 2 ? this.SMALL : meta == 1 ? this.MEDIUM : this.blockIcon;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		float g = 0.125F;
		float v = 0.375F;
		if (meta == 1) {
			v = 0.625F;
		}
		if (meta == 2) {
			v = 0.875F;
		}
		this.setBlockBounds(0.0F + g, 0.0F, 0.0F + g, 1.0F - g, 1.0F - v, 1.0F - g);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1) {
		this.blockIcon = par1.registerIcon(this.getTextureName());
		this.MEDIUM = par1.registerIcon(this.getTextureName() + "_M");
		this.SMALL = par1.registerIcon(this.getTextureName() + "_S");
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		int silk = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, player.getCurrentEquippedItem());
		if (!world.isRemote) {
			if (player.getCurrentEquippedItem() != null && silk > 0) {
				silkdrop = true;
			}

			if (world.getBlock(x, y - 1, z) == ModBlocks.saltOre) {
				crystalFind(world, x, y, z, player);
			}

			if (world.getBlockMetadata(x, y, z) == 0 && (world.getBlock(x, y - 1, z) == ModBlocks.saltBlock || world.getBlock(x, y - 1, z) == ModBlocks.saltSlabDouble)) {
				player.addStat(AchievSalt.saltFarm, 1);
			}
		}
	}

	protected void crystalFind(World world, int x, int y, int z, EntityPlayer player) {
		int I = 0;
		if (Loader.isModLoaded("TwilightForest")) {
			I = SaltConfig.TFDim;
		}
		if (world.provider.dimensionId == 0) {
			if (y < 40) player.addStat(AchievSalt.saltCrystal, 1);
		} else if (world.provider.dimensionId == I && SaltConfig.TFOreGen) {
			if (y < 20) player.addStat(AchievSalt.saltCrystal, 1);
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		if (metadata == 0) {

			if (silkdrop) {
				drop.add(new ItemStack(this));
			} else {
				drop.add(new ItemStack(ModItems.saltPinch));
			}
		}

		silkdrop = false;

		return drop;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (!world.isRemote) {
			if (entity instanceof EntityLivingBase && EntityList.getEntityString(entity) != null &&
					((EntityList.getEntityString(entity).toLowerCase().contains("slime") && !EntityList.getEntityString(entity).toLowerCase().contains("lava")) ||
							EntityList.getEntityString(entity).toLowerCase().contains("witch"))) {
				entity.attackEntityFrom(DamageSource.cactus, 30.0F);

				world.playSoundEffect(x + 0.5D, y + 1.0D, z + 0.5D, "dig.glass", 1.0F, 2.0F);
				world.func_147480_a(x, y, z, true);
			} else if (entity instanceof EntityPlayer && world.getBlock(x, y - 1, z) == ModBlocks.saltOre) {
				crystalFind(world, x, y, z, (EntityPlayer) entity);
			}
		}
	}

	@Override
	public MapColor getMapColor(int meta) {
		return MapColor.airColor;
	}
}