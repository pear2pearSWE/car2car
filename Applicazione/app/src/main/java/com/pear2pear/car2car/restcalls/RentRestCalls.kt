package com.pear2pear.car2car.restcalls

import com.pear2pear.car2car.assets.Rent
import com.pear2pear.car2car.assets.parser.RentParser
import com.pear2pear.car2car.restcalls.resources.rents.ReceivedRentsResource
import com.pear2pear.car2car.restcalls.resources.rents.SentRentsResource
import com.pear2pear.car2car.restcalls.resources.rents.SpecificReceivedRentResource
import com.pear2pear.car2car.restcalls.resources.rents.SpecificSentRentResource
import org.json.JSONObject
import kotlin.collections.ArrayList

class RentRestCalls(var token: String? = null) {

    fun getMyRents(name: String): ArrayList<Rent> {
        val jarray = SentRentsResource(name).request("GET", null, token).getJSONArray("data")
        val out = arrayListOf<Rent>()
        for (i in 0 until jarray.length()) {
            val obj = jarray[i] as JSONObject
            out.add(RentParser(obj).toRent())
        }
        return out
    }

    fun getReceivedRents(name: String): ArrayList<Rent> {
        val jarray = ReceivedRentsResource(name).request("GET", null, token).getJSONArray("data")
        val out = arrayListOf<Rent>()
        for (i in 0 until jarray.length()) {
            val obj = jarray[i] as JSONObject
            out.add(RentParser(obj).toRent())
        }
        return out
    }

    fun insertRentRequest(r: Rent): Boolean {  //creazione rent
        return SentRentsResource(r.user).request("POST", RentParser(r).toJSON(), token).getBoolean("executed")
    }

    fun acceptRent(Rentid: String, name:String): Boolean{ //primo conferma da parte dell'affittuario
        val res = SpecificReceivedRentResource(name, Rentid).request("PUT", null, token)
        return res.getBoolean("executed")
    }

    fun confirmKeys(Rentid: String, name: String): Boolean { //prima conferma che deve fare il noleggiatore
        return SpecificSentRentResource(name,Rentid).request("PUT", null, token).getBoolean("executed")
    }

    fun abortRent(Rentid: String, name: String): Boolean { //annullo richiesta inviata
        return SpecificSentRentResource(name, Rentid).request("DELETE", null, token).getBoolean("executed")
    }

    fun denyRent(Rentid: String, name: String): Boolean { // rifiuto richiesta
        return SpecificReceivedRentResource(name, Rentid).request("DELETE", JSONObject()
            .put("command", "deny"), token).getBoolean("executed")
    }

    fun closeRent(Rentid: String, name: String): Boolean{ //fine noleggio da parte dell'affittuario
        return SpecificReceivedRentResource(name, Rentid).request("DELETE", JSONObject()
            .put("command", "close"), token).getBoolean("executed")
    }
}