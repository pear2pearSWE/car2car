package com.pear2pear.car2car.assets

import java.util.*
import kotlin.math.floor

class User(
    var user: String,
    var exp: Int,
    var avatar: String,
    var tutorial: Boolean = false,
    var rewardDate:String
) {
    fun getLevel(): Int {
        return exp/100  //100 punti esp per ogni livello
    }

    fun getCurrentExp(): Int {
        return exp % 100
    }
}