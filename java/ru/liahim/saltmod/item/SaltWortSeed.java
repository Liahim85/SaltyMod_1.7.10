package ru.liahim.saltmod.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.liahim.saltmod.common.CommonProxy;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.network.SaltWortMessage;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class SaltWortSeed extends ItemFood {
	
	public SaltWortSeed(String name, CreativeTabs tab) {
		super(1, (float) 0.4, false);
		this.setUnlocalizedName(name);
		this.setCreativeTab(tab);
		this.setTextureName("saltmod:SaltWortSeed");
		this.setPotionEffect(10, 2, 1, 0.8F);
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag) {

		PotionEffect ptn_efct = new PotionEffect(Potion.regeneration.id, 40, 1);

		String mess = "";

		mess += (Potion.potionTypes[ptn_efct.getPotionID()].isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.GRAY);
		mess += StatCollector.translateToLocal(ptn_efct.getEffectName()).trim();

		if (ptn_efct.getAmplifier() == 1) {
			mess += " II";
		} else if (ptn_efct.getAmplifier() == 2) {
			mess += " III";
		} else if (ptn_efct.getAmplifier() == 3) {
			mess += " IV";
		} else if (ptn_efct.getAmplifier() == 4) {
			mess += " V";
		}

		if (ptn_efct.getDuration() > 20)
			mess += " (" + Potion.getDurationString(ptn_efct) + ")";

		mess += EnumChatFormatting.RESET;

		list.add(mess);
	}

	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (ModBlocks.saltWort.canBlockStay(world, x, y + 1, z) && side == 1 && world.isAirBlock(x, y + 1, z)) {
			world.setBlock(x, y + 1, z, ModBlocks.saltWort);
			world.playSoundEffect(x + 0.5D, y + 1.0D, z + 0.5D, ModBlocks.saltWort.stepSound.getBreakSound(), 1.0F, 0.8F);
			--item.stackSize;
			return true;
		} else if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityFlowerPot) {
			TileEntityFlowerPot te = (TileEntityFlowerPot) world.getTileEntity(x, y, z);
			if (te.getFlowerPotItem() == null)
			{
				if (!world.isRemote)
				{
					int i = world.rand.nextInt(2);
					te.func_145964_a(Item.getItemFromBlock(ModBlocks.saltWort), i);
					CommonProxy.network.sendToAllAround(new SaltWortMessage(x, y, z, i), new TargetPoint(world.provider.dimensionId, x, y, z, 256));
					te.markDirty();
					world.markBlockForUpdate(x, y, z);
				}
				--item.stackSize;
				return true;
			} else {return false;}
		} else {
			return false;
		}
	}
}