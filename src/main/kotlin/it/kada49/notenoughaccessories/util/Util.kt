package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.kada49.notenoughaccessories.Constants
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileReader
import java.net.HttpURLConnection
import java.net.URL

object Util {
    fun withPrefix (message: String): String {
        return Constants.PREFIX + message
    }

    fun String.toNbt(): NBTTagCompound {
        val result = ByteArrayInputStream(Base64.decodeBase64(this))
        return CompressedStreamTools.readCompressed(result)
    }

    fun getRequest(path: String): JsonObject {
        val reader = FileReader(File(path))
        val unparsed = reader.readText()
        reader.close()

        return JsonParser().parse(unparsed) as JsonObject
    }

    fun httpGetRequest(url: String): JsonObject {
        val client = URL(url).openConnection() as HttpURLConnection

        return JsonParser().parse(client.inputStream.bufferedReader().readText()) as JsonObject
    }
}