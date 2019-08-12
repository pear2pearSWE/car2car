package com.pear2pear.car2car.restcalls.resources.rents

import com.pear2pear.car2car.restcalls.resources.Resource

class SentRentsResource(var user: String) : Resource() {
    override fun getURL(): String {
        return "$BASE_ADDRESS/rents/$user"
    }
}