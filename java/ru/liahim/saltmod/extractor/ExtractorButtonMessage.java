package ru.liahim.saltmod.extractor;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ExtractorButtonMessage implements IMessage {

	int x, y, z;

    public ExtractorButtonMessage() { }

    public ExtractorButtonMessage(int x, int y, int z)
    {
		this.x = x;
		this.y = y;
		this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    	buf.writeInt(x);
    	buf.writeInt(y);
    	buf.writeInt(z);
    }
    
    public static class Handler implements IMessageHandler<ExtractorButtonMessage, IMessage> {
        
    	@Override
    	public IMessage onMessage(ExtractorButtonMessage message, MessageContext ctx)
    	{
    		World world = ctx.getServerHandler().playerEntity.worldObj;
    		
    		TileEntity te = world.getTileEntity(message.x, message.y, message.z);

    		if (te instanceof TileEntityExtractor)
    		{
    			((TileEntityExtractor) te).tank.setFluid(null);
    		}
    		
    		return null;
    	}
    }
}