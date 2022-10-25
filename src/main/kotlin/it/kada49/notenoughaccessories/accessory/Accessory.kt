package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory

import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.getRequest
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.httpGetRequest
import net.minecraft.client.Minecraft


class Accessory(id: String) {
    var id: String
    var name: String
    var rarity: Rarity

    init {
        val path = "${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories/allAccessories.json"
        val formatted = getRequest(path)

        this.id = id

        var name = ""
        var rarity = Rarity.COMMON
        for (accessory in formatted.asJsonObject["accessories"].asJsonArray) {
            if (accessory.asJsonObject["id"].asString == this.id) {
                name = accessory.asJsonObject["name"].asString
                if (accessory.asJsonObject["tier"] != null) rarity = Rarity.valueOf(accessory.asJsonObject["tier"].asString)
                break
            }
        }

        this.name = name
        this.rarity = rarity
    }

    fun isUnobtainable(): Boolean {
        val obj = httpGetRequest("https://kada49.github.io/json/NotEnoughAccessories/unobtainable.json")
        if (obj.asJsonObject["unobtainable"].asJsonObject.has(this.id)) return true
        return false
    }
}