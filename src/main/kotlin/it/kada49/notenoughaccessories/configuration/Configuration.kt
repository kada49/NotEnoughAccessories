package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import it.kada49.notenoughaccessories.Constants.MOD_ID
import it.kada49.notenoughaccessories.Constants.NAME
import it.kada49.notenoughaccessories.Constants.VERSION
import java.io.File

object Configuration: Vigilant(file = File("./config/$MOD_ID.toml"), guiTitle = "$NAME ($VERSION)") {

    @Property(
        name = "Hypixel API key",
        description = "Either enter your API key manually or type \"/api new\" while on the Hypixel Server",
        category = "General",
        type = PropertyType.TEXT,
        protectedText = true
    )
    var hypixelAPIKey = ""

    init {
        initialize()
    }
}