package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.command

import gg.essential.universal.UChat
import it.kada49.notenoughaccessories.NotEnoughAccessories
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory.GU
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory.MissingGui
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory.OnlyHoverGui
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Hypixel
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class NEACommand: CommandBase() {

    override fun getCommandName(): String = "nea"
    override fun getCommandUsage(sender: ICommandSender?): String = "/$commandName"
    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (args?.size == 0 && args.isEmpty()) { NotEnoughAccessories.configGui = Configuration.gui() }
        if (args?.size == 1 && args[0] == "verify") {
            if (!Hypixel.checkAPIKey()) {
                UChat.chat(Util.sendMessageWithPrefix("Invalid Hypixel API key!"))
            } else { UChat.chat(Util.sendMessageWithPrefix("Hypixel API key valid!")) }
        }
        if (args?.size == 1 && args[0] == "test") {
            NotEnoughAccessories.configGui = MissingGui(OnlyHoverGui(6))
        }
    }
}