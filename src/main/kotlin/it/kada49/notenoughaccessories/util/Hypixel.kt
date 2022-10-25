package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration.activeProfile
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration.hypixelAPIKey
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.httpGetRequest
import java.net.URL


object Hypixel {
    fun getSkyBlockProfileId(uuid: String) : String {

        val profiles = httpGetRequest("https://api.hypixel.net/skyblock/profiles?key=$hypixelAPIKey&uuid=$uuid").asJsonObject["profiles"].asJsonArray

        if (activeProfile != "") {
            for (profile in profiles) {
                if (profile.asJsonObject["cute_name"].asString == activeProfile) {
                    return profile.asJsonObject["profile_id"].asString
                }
            }
        }
        //get selected profile
        var i = 0
        while (true) {
            if (profiles[i].asJsonObject["selected"].asBoolean) break
            i++
        }

        return profiles[0].asJsonObject["profile_id"].asString
    }

    fun getSkyBlockCuteName(uuid: String): String {
        val profiles = httpGetRequest("https://api.hypixel.net/skyblock/profiles?key=$hypixelAPIKey&uuid=$uuid").asJsonObject["profiles"].asJsonArray

        if (activeProfile != "") {
            for (profile in profiles) {
                if (profile.asJsonObject["cute_name"].asString == activeProfile) {
                    return profile.asJsonObject["cute_name"].asString
                }
            }
        }

        //get selected profile
        var i = 0
        while (true) {
            if (profiles[i].asJsonObject["selected"].asBoolean) break
            i++
        }

        return profiles[i].asJsonObject["cute_name"].asString
    }

    fun checkAPIKey(): Boolean {
        val unparsed: String?
        try {
            unparsed = URL("https://api.hypixel.net/key?key=$hypixelAPIKey").readText()
        } catch (e: Exception) {
            return false
        }
        val parsed = JsonParser().parse(unparsed) as JsonObject
        return parsed.asJsonObject["success"].asBoolean
    }
}