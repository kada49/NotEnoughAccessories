package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.kada49.notenoughaccessories.Constants
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayInputStream
import java.io.FileReader
import java.net.URL

object Util {
    fun sendMessageWithPrefix (message: String): String {
        return Constants.PREFIX + message
    }

    fun getUUID(): String {
        val unparsedUUID = URL("https://api.mojang.com/users/profiles/minecraft/${Minecraft.getMinecraft().thePlayer.name}").readText()
        val parsedUUID = JsonParser().parse(unparsedUUID) as JsonObject
        return parsedUUID.asJsonObject["id"].asString

    }

    fun String.toNbt(): NBTTagCompound {
        val result = ByteArrayInputStream(Base64.decodeBase64(this))
        return CompressedStreamTools.readCompressed(result)
    }

    fun JsonObject(jsonString: String): JsonObject {
        val jsonUrl = URL(jsonString)
        val unparsed = jsonUrl.readText()
        return JsonParser().parse(unparsed) as JsonObject
    }

    fun String.nameFromId(): String {
        var name = ""
        val file = FileReader("${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories/allAccessories.json")
        val unformatted = file.readText()
        val formatted = JsonParser().parse(unformatted) as JsonObject
        for (element in formatted.asJsonObject["accessories"].asJsonArray) {
            if (element.asJsonObject["id"].asString == this) {
                name = element.asJsonObject["name"].asString
                break
            }
        }
        return name
    }
}