package it.kada49.notenoughaccessories.command

import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory.MinecraftUser
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.AccessoryActions.getMissing
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class AccessoryCommand: CommandBase() {

    override fun getCommandName(): String = "accessory"
    override fun getCommandUsage(sender: ICommandSender?): String = "/$commandName"
    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender?, args: Array<String>?) {
        getMissing(MinecraftUser(Minecraft.getMinecraft().thePlayer.name))
    }
}