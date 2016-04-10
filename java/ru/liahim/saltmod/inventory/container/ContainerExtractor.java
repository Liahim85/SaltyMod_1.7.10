package ru.liahim.saltmod.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import ru.liahim.saltmod.tileentity.TileEntityExtractor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerExtractor extends Container {
    private TileEntityExtractor te;
    private int lastExtractTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerExtractor(InventoryPlayer inv, TileEntityExtractor te) {
        this.te = te;
        this.addSlotToContainer(new SlotExtractor(inv.player, te, 0, 116, 33));
        this.addSlotToContainer(new SlotExtractorFuel(te, 1, 44, 53));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting) {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.te.extractTime);
        iCrafting.sendProgressBarUpdate(this, 1, this.te.burningTime);
        iCrafting.sendProgressBarUpdate(this, 2, this.te.currentItemBurnTime);
        iCrafting.sendProgressBarUpdate(this, 3, this.te.liquidID);
        iCrafting.sendProgressBarUpdate(this, 4, this.te.liquidLevel);
        iCrafting.sendProgressBarUpdate(this, 5, this.te.pressure);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting iCrafting = (ICrafting) crafter;

            if (this.lastExtractTime != this.te.extractTime) {
                iCrafting.sendProgressBarUpdate(this, 0, this.te.extractTime);
            }

            if (this.lastBurnTime != this.te.burningTime) {
                iCrafting.sendProgressBarUpdate(this, 1, this.te.burningTime);
            }

            if (this.lastItemBurnTime != this.te.currentItemBurnTime) {
                iCrafting.sendProgressBarUpdate(this, 2, this.te.currentItemBurnTime);
            }

            iCrafting.sendProgressBarUpdate(this, 3, this.te.liquidID);
            iCrafting.sendProgressBarUpdate(this, 4, this.te.liquidLevel);
            iCrafting.sendProgressBarUpdate(this, 5, this.te.pressure);
        }

        this.lastExtractTime = this.te.extractTime;
        this.lastBurnTime = this.te.burningTime;
        this.lastItemBurnTime = this.te.currentItemBurnTime;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int v) {
        if (id == 0) {
            this.te.extractTime = v;
        }

        if (id == 1) {
            this.te.burningTime = v;
        }

        if (id == 2) {
            this.te.currentItemBurnTime = v;
        }

        if (id == 3) {
            this.te.liquidID = v;
        }

        if (id == 4) {
            this.te.liquidLevel = v;
        }

        if (id == 5) {
            this.te.pressure = v;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.te.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1) {
                if (TileEntityFurnace.isItemFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (index >= 2 && index < 29) {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                        return null;
                    }
                } else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}