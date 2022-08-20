package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.command

import it.kada49.notenoughaccessories.NotEnoughAccessories
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class NEACommand: CommandBase() {

    override fun getCommandName(): String = "nea"
    override fun getCommandUsage(sender: ICommandSender?): String = "/$commandName"
    override fun getRequiredPermissionLevel(): Int = 0


    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (args?.size == 1 && args[0] == "config") NotEnoughAccessories.configGui = Configuration.gui()
    }
}