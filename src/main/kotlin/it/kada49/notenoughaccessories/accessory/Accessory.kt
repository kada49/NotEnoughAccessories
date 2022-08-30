package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory

import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.JsonObject
import net.minecraft.client.Minecraft
import java.io.File


class Accessory(id: String) {
    var id: String
    var name: String

    init {
        val path = "${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories/allAccessories.json"
        val formatted = JsonObject(File(path))

        this.id = id

        var name  = ""
        for (element in formatted.asJsonObject["accessories"].asJsonArray) {
            if (element.asJsonObject["id"].asString == this.id) {
                name = element.asJsonObject["name"].asString
                break
            }
        }
        this.name = name
    }
}