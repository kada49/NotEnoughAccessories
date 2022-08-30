package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation


class MissingGui(private var inventory: IInventory): GuiScreen() {
    private var inventoryRows = inventory.sizeInventory / 9
    private val texture = ResourceLocation("nea:MissingGui.png")
    private val xSize = 176
    private val ySize = 132

    override fun doesGuiPauseGame(): Boolean = false

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(1)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }
    override fun drawBackground(tint: Int) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
        Minecraft.getMinecraft().textureManager.bindTexture(texture)
        this.drawTexturedModalRect(((this.width - xSize)/2), ((this.height - ySize)/2), 0, 0, xSize, ySize)
    }

}