package com.pear2pear.car2car.restcalls

import com.pear2pear.car2car.assets.Mission
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.assets.parser.MissionParser
import com.pear2pear.car2car.assets.parser.UserParser
import com.pear2pear.car2car.restcalls.resources.gamification.GlobalStatsResource
import com.pear2pear.car2car.restcalls.resources.gamification.UserMissionsResource
import com.pear2pear.car2car.restcalls.resources.gamification.UserStatsResource
import org.json.JSONObject

class GameRestCalls(var token: String? = null) {

    fun getUserGame(user:String): User {
        val u= UserStatsResource(user).request("GET", null, token).getJSONObject("data")
        return UserParser(u).toUser()
    }

    fun getLeaderBoard(): ArrayList<User> {
        val data = GlobalStatsResource().request("GET", null, token).getJSONArray("data")
        val out = arrayListOf<User>()
        for (i in 0 until data.length()){
            out.add(UserParser(data[i] as JSONObject).toUser())
        }
        return out
    }

    fun getUserMissions(user: String): ArrayList<Mission> {
        val data = UserMissionsResource(user).request("GET", null, token).getJSONArray("data")
        val out = arrayListOf<Mission>()
        for (i in 0 until data.length()){
            out.add(MissionParser(data[i] as JSONObject).toMission())
        }
        return out
    }

    fun getReward(user: String): Int{
        val response = UserStatsResource(user).request("PUT", null, token)
        return if (response.getBoolean("executed")){
            response.getInt("gained")
        } else -1
    }

    fun completeTutorial(user: String): Int{
        val response = UserMissionsResource(user).request("POST",
            JSONObject().put("mission", "tutorial"), token)
        return if (response.getBoolean("executed")){
            response.getInt("gained")
        } else -1
    }

    fun insertFirstAuto(user: String): Int{
        val response = UserMissionsResource(user).request("POST",
            JSONObject().put("mission", "first_auto"), token)
        return if (response.getBoolean("executed")){
            response.getInt("gained")
        } else -1
    }

    fun changeAvatar(user: String, newAvatar: String): Boolean{
        return UserStatsResource(user).request("POST",
            JSONObject().put("path", newAvatar), token).getBoolean("executed")
    }
}