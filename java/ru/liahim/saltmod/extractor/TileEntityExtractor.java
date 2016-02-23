package ru.liahim.saltmod.extractor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import ru.liahim.saltmod.api.ExtractRegistry;
import ru.liahim.saltmod.common.CommonProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityExtractor extends TileEntity implements ISidedInventory, IFluidHandler
{
    private static final int[] slotsBottom = new int[] {0, 1};
    private static final int[] slotsSides = new int[] {1};
    private ItemStack[] invSlots = new ItemStack[2];
    public int burningTime;
    public int currentItemBurnTime;
    public int extractTime;
    public int liquidID;
    public int liquidLevel;
    private String inventoryName;
    
    //red stone
    private int liquidChange;
    private int redSS;
    
    //steam
    private int steamLevel;
    private int steamTime;
    public int pressure;

    //tank
    private int maxCap = FluidContainerRegistry.BUCKET_VOLUME * CommonProxy.extractorVolume;
	public FluidTank tank = new FluidTank(maxCap);

/** UPDATE */	

	public void updateEntity()
    {
    	super.updateEntity();
        boolean burn = this.burningTime > 0;
        boolean teUpdate = false;
        boolean clear = !this.worldObj.isSideSolid(xCoord, yCoord + 1, zCoord, ForgeDirection.DOWN);

        if (this.liquidLevel > 0 && liquidChange == 0)
        {
        	liquidChange = this.liquidLevel;
        	teUpdate = true;
        	if (this.canExtract()) 
            {Extractor.updateExtractorBlockState(this.burningTime > 0, true, this.worldObj, this.xCoord, this.yCoord, this.zCoord);}
        }

        if (this.liquidLevel == 0 && liquidChange > 0)
        {
        	liquidChange = 0;
        	extractTime = 0;
        	teUpdate = true;
        	Extractor.updateExtractorBlockState(this.burningTime > 0, false, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
        
        if (this.burningTime > 0)
        {
            --this.burningTime;
        }
        
        if (liquidChange != this.liquidLevel && redSS != this.getFluidAmountScaled(15))
        {
        	liquidChange = this.liquidLevel;
        	redSS = this.getFluidAmountScaled(15);
        	teUpdate = true;
        }

        if (!this.worldObj.isRemote)
        {
        	this.liquidID = this.tank.getFluid() != null ? this.tank.getFluid().fluidID : 0;
        	this.liquidLevel = this.tank.getFluid() != null ? this.tank.getFluidAmount() : 0;

            if (this.burningTime != 0 || this.invSlots[1] != null && !this.isFluidTankEmpty())
            {            	
                if (this.burningTime == 0 && this.canExtract())
                {
                    this.currentItemBurnTime = this.burningTime = TileEntityFurnace.getItemBurnTime(this.invSlots[1]);

                    if (this.burningTime > 0)
                    {
                    	teUpdate = true;

                        if (this.invSlots[1] != null)
                        {
                            --this.invSlots[1].stackSize;

                            if (this.invSlots[1].stackSize == 0)
                            {
                                this.invSlots[1] = invSlots[1].getItem().getContainerItem(invSlots[1]);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canExtract())
                {
                	if (clear)
                	{
                		int vol = ExtractRegistry.instance().getExtractFluidVolum(this.tank.getFluid().getFluid());
                		++this.extractTime;

                		if (this.extractTime == vol)
                		{
                			this.extractTime = 0;
                			this.extract();
                			teUpdate = true;                			
                		}

                		this.tank.drain(1, true);
                	}
                	
                	else {pressure();}
                }
                
                else
                {
                    this.extractTime = 0;
                }
            }

            if (burn != this.burningTime > 0)
            {
            	teUpdate = true;
                Extractor.updateExtractorBlockState(this.burningTime > 0, this.canExtract(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
            
            if ((this.steamLevel != 0 && clear) || (this.liquidLevel == 0 && !clear) || !this.isBurning())
            {this.pressure = 0; this.steamLevel = 0; this.steamTime = 0;}
        }

        if (teUpdate)
        {
            this.markDirty();
        }
    }
	
    public boolean isBurning()
    {
        return this.burningTime > 0;
    }
	
    public void pressure()
    {
    	int vol = ExtractRegistry.instance().getExtractFluidVolum(this.tank.getFluid().getFluid());
    	this.pressure = this.steamLevel/((32 - getFluidAmountScaled(32) + 1) * 4);
    	++this.steamTime;

    	if (this.steamTime % (pressure + 1) == 0)
    	{
    		++this.extractTime;
    		this.steamTime = 0;

    		if (this.extractTime == vol)
    		{
    			this.extractTime = 0;
    			this.extract();
    			this.markDirty();
    		}
    		
    		this.tank.drain(1, true);
    	}
    	
    	++this.steamLevel;
    	
    	if (pressure >= 16)
    	{
    		this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
    		this.worldObj.createExplosion((Entity)null, (double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D, 2.5F, true);
    	}
    }

	public boolean canExtract()
    {
    	if (this.isFluidTankEmpty()) {return false;}
    	
    	else
    	{
    		ItemStack itemstack = ExtractRegistry.instance().getExtractItemStack(this.tank.getFluid().getFluid());
    		if (itemstack == null) return false;
    		if (this.invSlots[0] == null) return true;
    		if (!this.invSlots[0].isItemEqual(itemstack)) return false;
    		int result = invSlots[0].stackSize + itemstack.stackSize;
    		return result <= getInventoryStackLimit() && result <= this.invSlots[0].getMaxStackSize();
    	}
    }
    
    public void extract()
    {
        if (this.canExtract())
        {
            ItemStack itemstack = ExtractRegistry.instance().getExtractItemStack(this.tank.getFluid().getFluid());

            if (this.invSlots[0] == null)
            {
                this.invSlots[0] = itemstack.copy();
            }
            
            else if (this.invSlots[0].isItemEqual(itemstack))
            {
                this.invSlots[0].stackSize += itemstack.stackSize;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getExtractProgressScaled(int scale)
    {
    	int vol = ExtractRegistry.instance().getExtractFluidVolum(FluidRegistry.getFluid(this.liquidID));
    	if (vol == 0) {vol = 1000;}
        return this.extractTime * scale / vol;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.burningTime * scale / this.currentItemBurnTime;
    }
    
	@SideOnly(Side.CLIENT)
	public int getFluidAmountScaledClient(int scale)
	{
		return MathHelper.ceiling_float_int((float)this.liquidLevel * scale / this.maxCap);
	}
    
/** INVENTORY */
    
    public ItemStack getStackInSlot(int slot)
    {
        return this.invSlots[slot];
    }

    public ItemStack decrStackSize(int slot, int amt)
    {
        if (this.invSlots[slot] != null)
        {
            ItemStack itemstack;

            if (this.invSlots[slot].stackSize <= amt)
            {
                itemstack = this.invSlots[slot];
                this.invSlots[slot] = null;
                return itemstack;
            }
            
            else
            {
                itemstack = this.invSlots[slot].splitStack(amt);

                if (this.invSlots[slot].stackSize == 0)
                {
                    this.invSlots[slot] = null;
                }

                return itemstack;
            }
        }
        
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (this.invSlots[slot] != null)
        {
            ItemStack itemstack = this.invSlots[slot];
            this.invSlots[slot] = null;
            return itemstack;
        }
        
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        this.invSlots[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getSizeInventory()
    {
        return this.invSlots.length;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == 0 ? false : (slot == 1 ? TileEntityFurnace.isItemFuel(stack) : false);
    }

    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == 0 ? slotsBottom : (side != 1 ? slotsSides : new int[0]);
    }

    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return this.isItemValidForSlot(slot, stack);
    }

    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return side != 0 || slot != 1 || stack.getItem() == Items.bucket;
    }
    
/** NAMES */
    
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.inventoryName : "container.extractor";
    }

    public boolean hasCustomInventoryName()
    {
        return this.inventoryName != null && this.inventoryName.length() > 0;
    }

    public void func_145951_a(String string)
    {
        this.inventoryName = string;
    }
    
/** NBT */

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagList inv_tags = nbt.getTagList("Items", 10);
        this.invSlots = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < inv_tags.tagCount(); ++i)
        {
            NBTTagCompound tag = inv_tags.getCompoundTagAt(i);
            byte b0 = tag.getByte("Slot");

            if (b0 >= 0 && b0 < this.invSlots.length)
            {
                this.invSlots[b0] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        this.burningTime = nbt.getShort("BurnTime");
        this.extractTime = nbt.getShort("CookTime");
        this.currentItemBurnTime = nbt.getShort("ItemBurnTime");
        this.steamLevel = nbt.getShort("SteamLevel");
        readTankFromNBT(nbt);

        if (nbt.hasKey("CustomName", 8))
        {
            this.inventoryName = nbt.getString("CustomName");
        }
    }
    
	protected void readTankFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("Tank"))
		{
			this.tank.readFromNBT(nbt.getCompoundTag("Tank"));
		}
	}

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setShort("BurnTime", (short)this.burningTime);
        nbt.setShort("CookTime", (short)this.extractTime);
        nbt.setShort("ItemBurnTime", (short)this.currentItemBurnTime);
        nbt.setShort("SteamLevel", (short)this.steamLevel);
        
        NBTTagList taglist = new NBTTagList();

        for (int i = 0; i < this.invSlots.length; ++i)
        {
            if (this.invSlots[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte)i);
                this.invSlots[i].writeToNBT(tag);
                taglist.appendTag(tag);
            }
        }

        nbt.setTag("Items", taglist);
        writeTankToNBT(nbt);

        if (this.hasCustomInventoryName())
        {
        	nbt.setString("CustomName", this.inventoryName);
        }
    }
    
	protected void writeTankToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.tank.writeToNBT(tag);
		nbt.setTag("Tank", tag);
	}
	
/** FLUID */

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		int f = this.tank.fill(resource, doFill);
		return f;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(this.tank.getFluid())) 
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		FluidStack d = this.tank.drain(maxDrain, doDrain);
		return d;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] {this.tank.getInfo()};
	}
	
	public int getFluidAmountScaled(int scale) 
	{
		return MathHelper.ceiling_float_int((float)this.getFluidAmount() * scale / this.maxCap);
	}

	public boolean isFluidTankEmpty()
	{
		return getFluidAmount() == 0;
	}

	public int getFluidAmount()
	{
		return this.tank.getFluidAmount();
	}
}