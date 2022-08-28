package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent


class OnlyHoverGui(var rows: Int) : IInventory {
    var stack = arrayOfNulls<ItemStack>(rows * 9)
    
    override fun getName(): String = "Missing Accessories"

    override fun hasCustomName(): Boolean = true

    override fun getDisplayName(): IChatComponent = ChatComponentText(name)

    override fun getSizeInventory(): Int = rows * 9

    override fun getStackInSlot(index: Int): ItemStack? = stack[index]

    override fun decrStackSize(index: Int, count: Int): ItemStack? {
        return if (this.stack[index] != null) {
            val itemstack: ItemStack
            if (this.stack[index]!!.stackSize <= count) {
                itemstack = this.stack[index]!!
                this.stack[index] = null
                itemstack
            } else {
                itemstack = this.stack[index]!!.splitStack(count)
                if (this.stack[index]!!.stackSize == 0) {
                    this.stack[index] = null
                }
                itemstack
            }
        } else {
            null
        }
    }

    override fun removeStackFromSlot(index: Int): ItemStack? {
        return if (this.stack[index] != null) {
            val itemstack = this.stack[index]
            this.stack[index] = null
            itemstack
        } else {
            null
        }
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack?) {
        this.stack[index] = stack
        if (stack != null && stack.stackSize > this.inventoryStackLimit) {
            stack.stackSize = this.inventoryStackLimit
        }
    }

    override fun getInventoryStackLimit(): Int = 64

    override fun markDirty() {}

    override fun isUseableByPlayer(player: EntityPlayer?): Boolean = true

    override fun openInventory(player: EntityPlayer?) {}

    override fun closeInventory(player: EntityPlayer?) {}

    override fun isItemValidForSlot(index: Int, stack: ItemStack?): Boolean = true

    override fun getField(id: Int): Int = 0

    override fun setField(id: Int, value: Int) {}

    override fun getFieldCount(): Int = 0

    override fun clear() {
        var i = 0
        while (true) {
            if (i == sizeInventory) break
            stack[i] = null
            ++i
        }
    }
}