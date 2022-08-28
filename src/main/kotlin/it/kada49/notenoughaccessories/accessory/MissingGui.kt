package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation


class MissingGui(private var inventory: IInventory): GuiScreen() {
    private var inventoryRows = inventory.sizeInventory / 9
    private val texture = ResourceLocation("assets/nea/textures/client/gui/MissingGui.png")
    private val xSize = 176
    private val ySize = 132
    private var guiLeft = 0
    private var guiTop = 0

    override fun initGui() {
        guiLeft = (this.width - xSize) / 2
        guiTop = (this.height - ySize) / 2
        super.initGui()
    }
    override fun drawBackground(tint: Int) {

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        this.mc.textureManager.bindTexture(texture)

        this.drawTexturedModalRect((this.width - xSize) / 2, (this.height - ySize) / 2, 0, 0, xSize, ySize)
        //super.drawBackground(tint)
    }
}