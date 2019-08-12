package com.pear2pear.car2car.restcalls.resources.gamification

import com.pear2pear.car2car.restcalls.resources.Resource

class GlobalStatsResource: Resource() {
    override fun getURL(): String {
        return "$BASE_ADDRESS/gamification"
    }
}