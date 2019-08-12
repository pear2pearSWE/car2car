package com.pear2pear.car2car.restcalls.resources.cars

import com.pear2pear.car2car.restcalls.resources.Resource

class SpecificCarResource(var userId: String, var objId: String) : Resource() {
    override fun getURL(): String {
        return "$BASE_ADDRESS/cars/$userId/$objId"
    }
}