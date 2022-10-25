package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.event


import gg.essential.universal.UChat
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration.activeProfile
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration.hypixelAPIKey
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.withPrefix
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object Chat {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @Suppress("unused")
    fun setProfileName(event: ClientChatReceivedEvent) {
        var msg = event.message.unformattedText
        msg = EnumChatFormatting.getTextWithoutFormattingCodes(msg)

        if (msg.startsWith("You are playing on profile: ") || msg.startsWith("Your profile was changed to: ")) {
            if (msg.endsWith(" (Co-op)")) msg = msg.replace(" (Co-op)", "")
            if (msg.contains("You are playing on profile: ")) msg = msg.replace("You are playing on profile: ", "")
            if (msg.contains("Your profile was changed to: ")) msg = msg.replace("Your profile was changed to: ", "")

            if (msg != activeProfile) {
                activeProfile = msg
                UChat.chat(withPrefix("Profile updated to:${EnumChatFormatting.YELLOW} $activeProfile"))
            }
        }

        if (msg.startsWith("Your new API key is ")) {
            msg = msg.replace("Your new API key is ", "")
            hypixelAPIKey = msg
            UChat.chat(withPrefix("Hypixel API key set!"))
        }
    }
}