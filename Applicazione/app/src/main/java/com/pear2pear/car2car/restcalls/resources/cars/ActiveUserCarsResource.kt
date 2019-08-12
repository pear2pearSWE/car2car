package com.pear2pear.car2car.restcalls.resources.cars

import com.pear2pear.car2car.restcalls.resources.Resource

class ActiveUserCarsResource(var user: String, var x: Double, var y: Double): Resource() {
    override fun getURL(): String {
        return "$BASE_ADDRESS/cars/activebyuser/$user/$x/$y"
    }
}