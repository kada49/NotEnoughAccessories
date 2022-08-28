package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import gg.essential.api.utils.Multithreading
import gg.essential.universal.UChat
import it.kada49.notenoughaccessories.Constants.HYPIXEL_URL
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration.hypixelAPIKey
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Hypixel.checkAPIKey
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Hypixel.getSkyBlockCuteName
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Hypixel.getSkyBlockProfileId
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.JsonObject
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.getUUID
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.nameFromId
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.sendMessageWithPrefix
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.toNbt
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Accessory {

    private var myAccessories: List<String> = mutableListOf()

    fun compare() {
        if (!checkAPIKey()) {
            UChat.chat(sendMessageWithPrefix("Invalid API key!"))
            return
        }
        UChat.chat("${EnumChatFormatting.GRAY}Calculating...")

        Multithreading.runAsync(
            kotlinx.coroutines.Runnable {
                val missing = mutableListOf<String>()
                myAccessories = getAccessoriesInTalismanBag()

                val allAccessoriesFile = FileReader("${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories/allAccessories.json")
                val allAccessoriesJson = JsonParser().parse(allAccessoriesFile.readText()) as JsonObject
                val allAccessoriesArray = allAccessoriesJson.asJsonObject["accessories"].asJsonArray
                for (accessory in allAccessoriesArray) { missing.add(accessory.asJsonObject["id"].asString) }

                val allAccessories = missing.toList()
                for (accessory in allAccessories) {
                    if (myAccessories.contains(accessory)) {
                        missing.remove(accessory)
                        if (hasPrevious(accessory)) {
                            var accessoryID = accessory
                            while (true) {
                                missing.remove(previousID(accessoryID))
                                if (hasPrevious(previousID(accessoryID))) { accessoryID = previousID(accessoryID) }
                                else break
                            }
                        }
                    }
                }

                missing.sort()

                UChat.chat(sendMessageWithPrefix("Accessories missing for profile ${EnumChatFormatting.YELLOW}${getSkyBlockCuteName()}${EnumChatFormatting.GREEN}:"))
                for (element in missing) {
                    if (!element.isUnobtainable()) {
                        UChat.chat(element.nameFromId())
                    }
                }
            }

        )
    }

    fun String.isUnobtainable(): Boolean {
        val obj = JsonObject("https://kada49.github.io/json/NotEnoughAccessories/unobtainable.json")
        if (obj.asJsonObject["unobtainable"].asJsonObject.has(this)) { return true }
        return false
    }

    fun allAccessoriesToFile() {
        val allItemsParsed = JsonObject( "$HYPIXEL_URL/resources/skyblock/items" )
        val allItemsArray = allItemsParsed.asJsonObject["items"].asJsonArray
        var index = 0

        val accessoriesParsed = JsonObject()
        val accessoriesArray = JsonArray()
        accessoriesParsed.add("accessories", accessoriesArray)


        for (element in allItemsArray) {
            if (element.asJsonObject.has("category")
                && element.asJsonObject["category"].asString == "ACCESSORY") {
                accessoriesArray.add(element)
            }
            index++
        }

        val accessoriesFile = File("${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories/allAccessories.json")

        if (accessoriesFile.exists()) accessoriesFile.delete()

        val allAccessoriesFile = FileWriter("${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories/allAccessories.json")
        allAccessoriesFile.write(accessoriesParsed.toString())
        allAccessoriesFile.flush()
        allAccessoriesFile.close()
    }

    private fun getAccessoriesInTalismanBag() : List<String> {
        val accessoriesIdList = mutableListOf<String>()
        var freeBagSlots = 0

        val parsedProfiles = JsonObject("https://api.hypixel.net/skyblock/profile?key=$hypixelAPIKey&profile=${getSkyBlockProfileId()}")

        val unparsedNbtData = parsedProfiles.asJsonObject["profile"].asJsonObject["members"].asJsonObject[getUUID()].asJsonObject["talisman_bag"].asJsonObject["data"].asString

        val accessories: NBTTagList = unparsedNbtData.toNbt().getTagList("i", TAG_COMPOUND)
        var i = 0
        while (i < accessories.tagCount()) {
            val accessory = accessories.getCompoundTagAt(i)
            if (accessory.hasKey("id")) {
                accessoriesIdList.add(accessory.getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id"))
            } else {
                freeBagSlots++
            }
            i++
        }

        return accessoriesIdList
    }

    private fun hasPrevious(id: String): Boolean {
        val notStackable = JsonObject("https://kada49.github.io/json/NotEnoughAccessories/notStackable.json")
        return notStackable.asJsonObject["previous"].asJsonObject.has(id)
    }

    private fun previousID(ID: String): String {
        val stackable = JsonObject("https://kada49.github.io/json/NotEnoughAccessories/notStackable.json")
        return stackable.asJsonObject["previous"].asJsonObject[ID].asString
    }

    fun test() {

    }
}