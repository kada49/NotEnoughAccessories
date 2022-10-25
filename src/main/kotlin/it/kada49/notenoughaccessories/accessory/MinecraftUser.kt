package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory

import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.httpGetRequest

class MinecraftUser (name: String) {
    var name: String
    var uuid: String

    init {
        this.name = name

        val response = httpGetRequest("https://api.mojang.com/users/profiles/minecraft/$name")
        this.uuid = response.asJsonObject["id"].asString
    }
}