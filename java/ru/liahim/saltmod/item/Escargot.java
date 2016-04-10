package ru.liahim.saltmod.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class Escargot extends ItemFood {

	public Escargot(String name, int i, float j, CreativeTabs tab, String texture) {
		super(i, j, false);
		this.setUnlocalizedName(name);
		this.setCreativeTab(tab);
		this.setAlwaysEdible();
		this.setPotionEffect(9, 15, 0, 0.3F);
		this.setTextureName("saltmod:" + texture);
	}
}