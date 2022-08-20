package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.kada49.notenoughaccessories.Constants.ACTIVE_PROFILE
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.configuration.Configuration.hypixelAPIKey
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.JsonObject
import it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.util.Util.getUUID
import java.net.URL


object Hypixel {
    fun getSkyBlockProfileId() : String {

        val profiles = JsonObject("https://api.hypixel.net/skyblock/profiles?key=$hypixelAPIKey&uuid=${getUUID()}").asJsonObject["profiles"].asJsonArray

        if (ACTIVE_PROFILE != "") {
            for (profile in profiles) {
                if (profile.asJsonObject["cute_name"].asString == ACTIVE_PROFILE) {
                    return profile.asJsonObject["profile_id"].asString
                }
            }
        }
        //get latest profile
        var latest: Long = 0

        for (profile in profiles) {
            val lastSave = profile.asJsonObject["last_save"].asLong
            if (lastSave > latest) { latest = lastSave }
        }

        for (profile in profiles) {
            if (profile.asJsonObject["last_save"].asLong == latest) { return profile.asJsonObject["profile_id"].asString }
        }

        return profiles[0].asJsonObject["profile_id"].asString
    }

    fun getSkyBlockCuteName(): String {

        val profiles = JsonObject("https://api.hypixel.net/skyblock/profiles?key=$hypixelAPIKey&uuid=${getUUID()}").asJsonObject["profiles"].asJsonArray

        if (ACTIVE_PROFILE != "") {
            for (profile in profiles) {
                if (profile.asJsonObject["cute_name"].asString == ACTIVE_PROFILE) {
                    return profile.asJsonObject["cute_name"].asString
                }
            }
        }
        //get latest profile
        var latest: Long = 0

        for (profile in profiles) {
            val lastSave = profile.asJsonObject["last_save"].asLong
            if (lastSave > latest) { latest = lastSave }
        }

        for (profile in profiles) {
            if (profile.asJsonObject["last_save"].asLong == latest) { return profile.asJsonObject["cute_name"].asString }
        }

        return profiles[0].asJsonObject["cute_name"].asString
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