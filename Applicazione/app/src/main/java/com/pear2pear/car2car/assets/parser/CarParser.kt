package com.pear2pear.car2car.assets.parser

import com.pear2pear.car2car.assets.Car
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CarParser(var carJson: JSONObject) {
    constructor(car: Car) : this(parseJSON(car))

    companion object {
        private fun parseJSON(car: Car): JSONObject {
            val out = JSONObject()
            val coo = JSONArray(car.position.split("-").map { it.toDouble() })
            val posobj = JSONObject().put("coordinates", coo).put("type", "Point")
            out.put("produttore", car.brand)
            out.put("modello", car.model)
            out.put("targa", car.plates)
            out.put("posizione", posobj)
            out.put(
                "dataIm",
                JSONObject().put(
                    "\$date",
                    SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse(car.registrationDate).time
                )
            )
            out.put("inuso", car.using)
            out.put("proprietarioID", car.username)
            if (!car.sharingPeriodStart.equals(null)) {
                val obj = JSONObject().put(
                    "inizio", JSONObject().put(
                        "\$date",
                        SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse(car.sharingPeriodStart).time
                    )
                )
                    .put(
                        "fine", JSONObject().put(
                            "\$date", SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.ITALY
                            ).parse(car.sharingPeriodEnd).time
                        )
                    )
                out.put("sharing", obj)

            }
            out.put("prezzo", car.price)
            out.put("descrizione", car.description)
            out.put("_id", JSONObject().put("\$oid", car.carID))
            return out
        }
    }

    fun toJSON(): JSONObject {
        return carJson
    }

    fun toCar(): Car {
        val obj = carJson
        val form = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)

        val coo = obj.getJSONObject("posizione").getJSONArray("coordinates")
        val dataIm = form.format(Date(obj.getJSONObject("dataIm").getLong("\$date")))
        var shain: String? = null
        var shaend: String? = null
        var use: Boolean? = null
        try {
            shain = form.format(Date(obj.getJSONObject("sharing").getJSONObject("inizio").getLong("\$date")))
            shaend = form.format(Date(obj.getJSONObject("sharing").getJSONObject("fine").getLong("\$date")))
            use = obj.getBoolean("inuso")
        } catch (e: JSONException) {
        }
        val oid = obj.getJSONObject("_id").getString("\$oid")
        return Car(
            obj.getString("produttore"),
            obj.getString("modello"),
            obj.getString("targa"),
            String.format("%s-%s", coo[0].toString(), coo[1].toString()),
            dataIm,
            obj.getDouble("prezzo"),
            obj.getString("descrizione"),
            obj.getString("proprietarioID"),
            shain,
            shaend,
            use,
            oid
        )
    }
}