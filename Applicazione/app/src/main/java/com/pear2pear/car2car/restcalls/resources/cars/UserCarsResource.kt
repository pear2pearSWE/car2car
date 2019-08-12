package com.pear2pear.car2car.restcalls.resources.cars

import com.pear2pear.car2car.restcalls.resources.Resource

class UserCarsResource(var user: String): Resource() {
    override fun getURL(): String {
        return "$BASE_ADDRESS/cars/$user"
    }
}