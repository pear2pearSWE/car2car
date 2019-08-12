package com.pear2pear.car2car.restcalls.resources.rents

import com.pear2pear.car2car.restcalls.resources.Resource

class ReceivedRentsResource(var name: String) : Resource() {
    override fun getURL(): String {
        return "$BASE_ADDRESS:/rents/received/$name"
    }
}