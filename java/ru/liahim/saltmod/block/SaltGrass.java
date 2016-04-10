package ru.liahim.saltmod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.liahim.saltmod.common.ClientProxy;
import ru.liahim.saltmod.init.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltGrass extends Block {

	@SideOnly(Side.CLIENT)
	private IIcon TOP;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE_L;
	@SideOnly(Side.CLIENT)
	private IIcon SIDE_R;
	@SideOnly(Side.CLIENT)
	private IIcon BOTTOM0;
	@SideOnly(Side.CLIENT)
	private IIcon BOTTOM1;

	public SaltGrass(String name, CreativeTabs tab) {
		super(Material.grass);
		this.setTickRandomly(true);
		this.setCreativeTab(tab);
		this.setStepSound(soundTypeGrass);
		this.setBlockName(name);
		this.setHardness(0.5F);
		this.setResistance(1F);
		this.setHarvestLevel("shovel", 0);
	}

	@Override
	public int getRenderType() {
		return ClientProxy.saltGrassRenderType;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? TOP : (side == 0 && meta > 0 ? this.BOTTOM1 : (side == 0 && meta == 0 ? this.BOTTOM0 :
				((side == 2 && (meta == 7 || meta == 11 || meta == 14 || meta == 15)) ||
						(side == 5 && (meta == 8 || meta == 11 || meta == 12 || meta == 15)) ||
						(side == 3 && (meta == 9 || meta == 12 || meta == 13 || meta == 15)) ||
						(side == 4 && (meta == 10 || meta == 13 || meta == 14 || meta == 15)) ? this.SIDE :
						((side == 2 && (meta == 3 || meta == 8 || meta == 12)) || (side == 5 && (meta == 4 || meta == 9 || meta == 13)) ||
								(side == 3 && (meta == 5 || meta == 10 || meta == 14)) || (side == 4 && (meta == 6 || meta == 7 || meta == 11)) ? this.SIDE_L :
								((side == 2 && (meta == 6 || meta == 10 || meta == 13)) || (side == 5 && (meta == 3 || meta == 7 || meta == 14)) ||
										(side == 3 && (meta == 4 || meta == 8 || meta == 11)) || (side == 4 && (meta == 5 || meta == 9 || meta == 12)) ? this.SIDE_R : this.blockIcon)))));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1) {
		this.blockIcon = par1.registerIcon("saltmod:SaltGrass");
		this.TOP = par1.registerIcon("saltmod:SaltGrass_Top");
		this.SIDE = par1.registerIcon("saltmod:SaltGrass_Side");
		this.SIDE_L = par1.registerIcon("saltmod:SaltGrass_Side_L");
		this.SIDE_R = par1.registerIcon("saltmod:SaltGrass_Side_R");
		this.BOTTOM0 = par1.registerIcon("saltmod:SaltDirtLite_0");
		this.BOTTOM1 = par1.registerIcon("saltmod:SaltDirtLite_Bottom");
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			if (world.getBlock(x, y + 1, z) == Blocks.snow_layer) {
				world.setBlockToAir(x, y + 1, z);
			}

			if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2) {
				int j = world.getBlockMetadata(x, y, z);
				world.setBlock(x, y, z, ModBlocks.saltDirtLite, j, 3);
			} else if (world.getBlockLightValue(x, y + 1, z) >= 9) {
				for (int l = 0; l < 4; ++l) {
					int i1 = x + random.nextInt(3) - 1;
					int j1 = y + random.nextInt(5) - 3;
					int k1 = z + random.nextInt(3) - 1;
					Block block = world.getBlock(i1, j1 + 1, k1);

					if (world.getBlock(i1, j1, k1) == Blocks.dirt && world.getBlockMetadata(i1, j1, k1) == 0 &&
							world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2) {
						world.setBlock(i1, j1, k1, Blocks.grass);
					}
				}
			}
		}
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return ModBlocks.saltDirtLite.getItemDropped(0, random, fortune);
	}
}