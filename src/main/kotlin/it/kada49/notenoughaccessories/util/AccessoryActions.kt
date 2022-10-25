package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import gg.essential.api.utils.Multithreading
import gg.essential.universal.UChat
import it.kada49.notenoughaccessories.Constants.HYPIXEL_URL
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory.Accessory
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory.MinecraftUser
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration.hypixelAPIKey
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Hypixel.checkAPIKey
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Hypixel.getSkyBlockCuteName
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Hypixel.getSkyBlockProfileId
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.getRequest
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.httpGetRequest
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.withPrefix
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.toNbt
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND
import java.io.File
import java.io.FileWriter

object AccessoryActions {
    fun getMissing(player: MinecraftUser) {
        if (!checkAPIKey()) {
            UChat.chat(withPrefix("Invalid API key!"))
            return
        }
        UChat.chat("${EnumChatFormatting.GRAY}Calculating...")

        Multithreading.runAsync(
            kotlinx.coroutines.Runnable {

                val missingIDs = mutableListOf<String>()
                val allObtainableAccessoriesIDs = mutableListOf<String>()

                val myAccessoriesIDs = getAccessoriesIDsInTalismanBag(player) ?: return@Runnable


                val allAccessoriesJson = getRequest("${Minecraft.getMinecraft().mcDataDir}/config/NotEnoughAccessories/allAccessories.json")
                val allAccessoriesArray = allAccessoriesJson.asJsonObject["accessories"].asJsonArray

                for (accessory in allAccessoriesArray) {
                    allObtainableAccessoriesIDs.add(accessory.asJsonObject["id"].asString)
                    missingIDs.add(accessory.asJsonObject["id"].asString)
                }

                for (id in allObtainableAccessoriesIDs) {
                    if (!myAccessoriesIDs.contains(id)) continue

                    missingIDs.remove(id)
                    if (!idHasPrevious(id)) continue
                    var accessoryID = id
                    while (true) {
                        missingIDs.remove(previousId(accessoryID))

                        if (!idHasPrevious(previousId(accessoryID))) break
                        accessoryID = previousId(accessoryID)
                    }
                }

                UChat.chat(withPrefix("Accessories missing for profile ${getSkyBlockCuteName(player.uuid)}:"))
                val accessories = mutableListOf<Accessory>()
                for (id in missingIDs) {
                    if (id.isUnobtainable()) continue

                    accessories.add(Accessory(id))
                }

                accessories.sortWith(Comparator { o1, o2 ->
                    return@Comparator o1.id.compareTo(o2.id)
                })

                for (accessory in accessories) {
                    if (accessory.isUnobtainable()) continue
                    UChat.chat("${accessory.rarity.colorPrefix}${accessory.name}")
                }

                UChat.chat(withPrefix("Total new accessories/upgrades missing: ${accessories.size}"))
            }
        )
    }

    private fun String.isUnobtainable(): Boolean {
        val unobtainable = httpGetRequest("https://kada49.github.io/json/NotEnoughAccessories/unobtainable.json")
        if (unobtainable.asJsonObject["unobtainable"].asJsonObject.has(this)) {
            return true
        }
        return false
    }

    fun allAccessoriesToFile() {
        val allItemsParsed = httpGetRequest("$HYPIXEL_URL/resources/skyblock/items" )
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

    private fun getAccessoriesIDsInTalismanBag(player: MinecraftUser) : MutableList<String>? {
        val accessoriesIdList = mutableListOf<String>()
        var freeBagSlots = 0

        val parsedProfiles = httpGetRequest("https://api.hypixel.net/skyblock/profile?key=$hypixelAPIKey&profile=${getSkyBlockProfileId(player.uuid)}")

        val profileData = parsedProfiles.asJsonObject["profile"].asJsonObject["members"].asJsonObject[player.uuid]
        if (profileData.asJsonObject["talisman_bag"] == null) {
            UChat.chat(withPrefix("Enable your Inventory API and try again!"))
            return null
        }
        val unparsedNbtData = profileData.asJsonObject["talisman_bag"].asJsonObject["data"].asString

        val accessories: NBTTagList = unparsedNbtData.toNbt().getTagList("i", TAG_COMPOUND)
        var i = 0
        while (i < accessories.tagCount()) {
            val accessory = accessories.getCompoundTagAt(i)
            if (accessory.hasKey("id")) {
                val id = accessory.getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id")
                accessoriesIdList.add(id)
            } else {
                freeBagSlots++
            }
            i++
        }

        return accessoriesIdList
    }

    private fun idHasPrevious(accessory: String): Boolean {
        val notStackable = httpGetRequest("https://kada49.github.io/json/NotEnoughAccessories/notStackable.json")
        return notStackable.asJsonObject["previous"].asJsonObject.has(accessory)
    }

    private fun previousId(accessory: String): String {
        val stackable = httpGetRequest("https://kada49.github.io/json/NotEnoughAccessories/notStackable.json")
        return stackable.asJsonObject["previous"].asJsonObject[accessory].asString
    }
}