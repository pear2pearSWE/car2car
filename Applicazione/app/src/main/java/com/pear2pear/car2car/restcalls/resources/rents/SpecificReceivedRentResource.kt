package com.pear2pear.car2car.restcalls.resources.rents

import com.pear2pear.car2car.restcalls.resources.Resource

class SpecificReceivedRentResource(var user: String, var rentId: String) : Resource() {
    override fun getURL(): String {
        return "$BASE_ADDRESS/rents/received/$user/$rentId"
    }
}