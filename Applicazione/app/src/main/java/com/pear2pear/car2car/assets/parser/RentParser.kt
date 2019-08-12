package com.pear2pear.car2car.assets.parser

import com.pear2pear.car2car.assets.Rent
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RentParser(var rentJson: JSONObject) {
    constructor(rent: Rent) : this(parseJSON(rent))

    companion object {
        private fun parseJSON(rent: Rent): JSONObject {
            val out = JSONObject()

            out.put("author", rent.user)
            out.put("addressedto", rent.toUser)
            out.put("date", JSONObject().put("\$date", rent.date.time))
            out.put("carId", rent.idCar)
            out.put("isAccepted", rent.isAccepted)
            out.put("hasKey", rent.hasKeys)
            out.put("isEnded", rent.isEnded)
            if (rent.reason != null) {
                out.put("reason", rent.reason)
            }
            //out.put("reason",rent.reason)
            //out.put("_id",JSONObject().put("\$oid",ID))
            return out
        }
    }

    fun toJSON(): JSONObject {
        return rentJson
    }

    fun toRent(): Rent {
        val obj = rentJson
        val date = Date(obj.getJSONObject("date").getLong("\$date"))
        val oid = obj.getJSONObject("_id").getString("\$oid")
        var reason: String?
        try {
            reason = obj.getString("reason")
        } catch (e: JSONException) {
            reason = null
        }
        return Rent(
            obj.getString("author"),
            obj.getString("addressedto"),
            obj.getString("carId"),
            date,
            obj.getBoolean("isAccepted"),
            obj.getBoolean("hasKey"),
            //obj.getString("reason"),
            obj.getBoolean("isEnded"),
            oid,
            reason
        )
    }
}