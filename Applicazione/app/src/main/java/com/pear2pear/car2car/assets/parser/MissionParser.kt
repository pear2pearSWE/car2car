package com.pear2pear.car2car.assets.parser

import com.pear2pear.car2car.assets.Mission
import org.json.JSONObject

class MissionParser(var missionJson: JSONObject) {

    constructor(mission: Mission) : this(parseJSON(mission))

    companion object {
        private fun parseJSON(mission: Mission): JSONObject {
            val out = JSONObject()
            out.put("name", mission.title)
            out.put("description", mission.description)
            out.put("threshold", mission.threshold)
            out.put("prize", mission.prize)
            out.put("imgreward", mission.imgreward)
            out.put("value",mission.value)
            out.put("complete",mission.complete)
            return out
        }
    }

    fun toJSON(): JSONObject {
        return missionJson
    }

    fun toMission(): Mission {
        val obj = missionJson
        val title = obj.getString("name")
        val description = obj.getString("description")
        //val vRef = obj.getString("vRef")
        val threshold = obj.getInt("threshold")
        val prize = obj.getInt("prize")
        val imgreward = obj.getString("imgreward")
        val value = obj.getDouble("value")
        val complete=obj.getBoolean("complete")

        return Mission(title, description, threshold, prize, imgreward,value,complete)
    }
}