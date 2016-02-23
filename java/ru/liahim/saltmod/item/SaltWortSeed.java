package ru.liahim.saltmod.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.liahim.saltmod.common.ModBlocks;

public class SaltWortSeed extends ItemFood {
	
	public SaltWortSeed(String name, CreativeTabs tab) {
		super(1, (float) 0.4, false);
		this.setUnlocalizedName(name);
		this.setCreativeTab(tab);
		this.setTextureName("saltmod:SaltWortSeed");
		this.setPotionEffect(10, 2, 1, 0.8F);
	}
	
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag) {
		
		PotionEffect ptn_efct = new PotionEffect(Potion.regeneration.id, 40, 1);

	    String mess = "";

	    mess += (Potion.potionTypes[ptn_efct.getPotionID()].isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.GRAY);
	    mess += StatCollector.translateToLocal(ptn_efct.getEffectName()).trim();

	    if (ptn_efct.getAmplifier() == 1){mess += " II";}
	    else if (ptn_efct.getAmplifier() == 2){mess += " III";}
	    else if (ptn_efct.getAmplifier() == 3){mess += " IV";}
	    else if (ptn_efct.getAmplifier() == 4){mess += " V";}

	    if (ptn_efct.getDuration() > 20)
	        mess += " (" + Potion.getDurationString(ptn_efct) + ")";
	    
	    mess += EnumChatFormatting.RESET;

	    list.add(mess);
	}
	
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (ModBlocks.saltWort.canBlockStay(world, x, y + 1, z) && side == 1 && world.isAirBlock(x, y + 1, z))
		{
			world.setBlock(x, y + 1, z, ModBlocks.saltWort);
			world.playSoundEffect((double)x + 0.5D, (double)y + 1.0D, (double)z + 0.5D, ModBlocks.saltWort.stepSound.getBreakSound(), 1.0F, 0.8F);
			--item.stackSize;
			return true;
		}
		else
		{
			return false;	
		}
	}
}