package it.kada49.notenoughaccessories

import gg.essential.universal.UScreen
import gg.essential.vigilance.Vigilance
import it.kada49.notenoughaccessories.Constants.MOD_ID
import it.kada49.notenoughaccessories.Constants.NAME
import it.kada49.notenoughaccessories.Constants.VERSION
import it.kada49.notenoughaccessories.command.AccessoryCommand
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.command.NEACommand
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Accessory.allAccessoriesToFile
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.event.Chat
import net.minecraft.client.Minecraft
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.io.File


@Mod(name = NAME,
    modid = MOD_ID,
    version = VERSION,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter",
    updateJSON = "https://kada49.github.io/json/NotEnoughAccessories-updateJson.json"
)

object NotEnoughAccessories{

    var configGui: UScreen? = null

    @EventHandler @Suppress("unused")
    fun init (event: FMLInitializationEvent) {

        val modDir = File("${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories")
        if (!modDir.exists()) modDir.mkdirs()
        allAccessoriesToFile()

        Vigilance.initialize()
        Configuration.initialize()

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(Chat)

        ClientCommandHandler.instance.registerCommand(AccessoryCommand())
        ClientCommandHandler.instance.registerCommand(NEACommand())
    }

    @SubscribeEvent @Suppress("unused")
    fun tick(event: TickEvent.ClientTickEvent) {
        tick()
    }

    private fun tick() {
        if (configGui != null) {
            try { Minecraft.getMinecraft().displayGuiScreen(configGui) } catch (e: Exception) { e.printStackTrace() }
            configGui = null
        }
    }


}