package ru.liahim.saltmod.entity.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderRainmakerDust extends Render {

	@Override
	public void doRender(Entity entity, double x, double y, double z, float width, float height) {}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {return null;}
}