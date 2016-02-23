package ru.liahim.saltmod.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import ru.liahim.saltmod.common.ModBlocks;

public class ItemSaltSlab extends ItemSlab {
	
	public ItemSaltSlab(Block block){
		super(block, ModBlocks.saltSlab, ModBlocks.saltSlabDouble, false);
		this.setHasSubtypes(true);
	}
}