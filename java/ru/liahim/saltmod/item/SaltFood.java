package ru.liahim.saltmod.item;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.liahim.saltmod.common.ModItems;

public class SaltFood extends ItemFood {
	
	private Item container;
	private PotionEffect[] effects;
	
	public SaltFood(String name, int amount, float saturation, Item container, PotionEffect... potionEffect) {
		super(amount, saturation, false);
		this.setUnlocalizedName(name);
		this.container = container;
		this.effects = potionEffect;
	}
	
	public SaltFood(String name, int amount, float saturation, PotionEffect... potionEffect) {
		super(amount, saturation, false);
		this.setUnlocalizedName(name);
		this.container = null;
		this.effects = potionEffect;
	}

	public SaltFood(String name, int amount, float saturation) {
		super(amount, saturation, false);
		this.setUnlocalizedName(name);
		this.container = null;
		this.effects = null;
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag) {

		super.addInformation(is, player, list, flag);

		if (this.effects != null)
    	{
			for (int i = 0; i < effects.length; i ++)
			{	 
				String mess = "";
        	
				if (effects[i] != null && effects[i].getPotionID() > 0)
				{
					mess += (Potion.potionTypes[effects[i].getPotionID()].isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.GRAY);
					mess += StatCollector.translateToLocal(effects[i].getEffectName()).trim();

					if (effects[i].getAmplifier() == 1){mess += " II";}
					else if (effects[i].getAmplifier() == 2){mess += " III";}
					else if (effects[i].getAmplifier() == 3){mess += " IV";}
					else if (effects[i].getAmplifier() == 4){mess += " V";}

        			if (effects[i].getDuration() > 20)
        				mess += " (" + Potion.getDurationString(effects[i]) + ")";
	    
        			mess += EnumChatFormatting.RESET;

        			list.add(mess);
        		}
        	}
    	}
	}
	
    public EnumAction getItemUseAction(ItemStack item)
    {
    	if (getUnlocalizedName().equals(ModItems.fermentedSaltWort.getUnlocalizedName()))
    	{
    		return EnumAction.drink;
    	}
    		
        return EnumAction.eat;
    }

	@Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        super.onEaten(stack, world, player);
        
        if (this.effects != null)
    	{
        	for (int i = 0; i < effects.length; i ++)
        	{
        		if (!world.isRemote && effects[i] != null && effects[i].getPotionID() > 0)
        			player.addPotionEffect(new PotionEffect(this.effects[i]));
        	}
    	}
        
        if (!world.isRemote && getUnlocalizedName().equals(ModItems.saltEgg.getUnlocalizedName()))
        {
        	world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Items.dye, 1, 15)));
        }
        
        return this.container != null ? new ItemStack(this.container) : stack;
    }
}