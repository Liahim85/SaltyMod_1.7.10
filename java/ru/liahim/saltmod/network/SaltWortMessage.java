package ru.liahim.saltmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityFlowerPot;
import ru.liahim.saltmod.init.ModBlocks;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SaltWortMessage implements IMessage {

	int x, y, z, i;
	
	public SaltWortMessage() {}

	public SaltWortMessage(int x, int y, int z, int i) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.i = i;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		i = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(i);
	}
	
	public static class Handler implements IMessageHandler<SaltWortMessage, IMessage> {

		@Override
		public IMessage onMessage(SaltWortMessage message, MessageContext ctx) {
			act(message.x, message.y, message.z, message.i);
			return null;
		}
	}
	
    @SideOnly(Side.CLIENT)
    private static void act(int x, int y, int z, int i) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
		if (world.getTileEntity(x, y, z) != null &&
			world.getTileEntity(x, y, z) instanceof TileEntityFlowerPot) {
			TileEntityFlowerPot te = (TileEntityFlowerPot) world.getTileEntity(x, y, z);
			te.func_145964_a(Item.getItemFromBlock(ModBlocks.saltWort), i);
			world.markBlockForUpdate(x, y, z);
		}
    }
}