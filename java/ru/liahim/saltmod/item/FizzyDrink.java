package ru.liahim.saltmod.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FizzyDrink extends Item {
	
	public FizzyDrink(String name, CreativeTabs tab, String textureName) {
		this.setMaxStackSize(1);
		this.setUnlocalizedName(name);
		this.setCreativeTab(tab);
		this.setTextureName("saltmod:" + textureName);
		
	}
	
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag)
	{
		list.add(I18n.format(getUnlocalizedName() + ".tooltip"));	
	}

	public ItemStack onEaten(ItemStack item, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
            --item.stackSize;
        }
        
        if (!world.isRemote)
        {
        	player.curePotionEffects(new ItemStack(Items.milk_bucket));
        	player.extinguish();
        }
        return item.stackSize <= 0 ? new ItemStack(Items.glass_bottle) : item;
    }
	
	public int getMaxItemUseDuration(ItemStack item)
    {
        return 32;
    }
	
    public EnumAction getItemUseAction(ItemStack item)
    {
        return EnumAction.drink;
    }
    
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
    	player.setItemInUse(item, this.getMaxItemUseDuration(item));
        return item;
    }
	
}
