package com.pear2pear.car2car.assets.parser

import com.pear2pear.car2car.assets.User
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class UserParser(var userJson: JSONObject) {
    constructor(user: User) : this(parseJSON(user))

    companion object {
        private fun parseJSON(u: User): JSONObject {
            val out = JSONObject()
            out.put("user", u.user)
            out.put("exp", u.exp)
            out.put("avatar", u.avatar)
            out.put(
                "lastReward",
                JSONObject().put(
                    "\$date",
                    SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse(u.rewardDate).time
                )
            )
            out.put("tutorial", u.tutorial)
            return out
        }
    }

    fun toJSON(): JSONObject {
        return userJson
    }

    fun toSimpleString(date: Date) : String {
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date)
    }

    fun toUser(): User {
        val form = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)
        var reward = ""
        var avatar = "mouse"
        var tutorial = false
        try {
            reward = form.format(Date(userJson.getJSONObject("lastReward").getLong("\$date")))
        }
        catch (e: JSONException){
            //
        }
        try{
            avatar = userJson.getString("avatar")
        }
        catch (e: JSONException){
            //
        }
        try{
            tutorial = userJson.getBoolean("tutorial")
        }
        catch (e: JSONException){
            //
        }
        return User(userJson.getString("user"), userJson.getInt("exp"), avatar, tutorial, reward)
    }
}