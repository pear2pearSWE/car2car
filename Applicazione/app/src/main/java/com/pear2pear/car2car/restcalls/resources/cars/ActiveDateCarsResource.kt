package com.pear2pear.car2car.restcalls.resources.cars

import com.pear2pear.car2car.restcalls.resources.Resource
import java.text.SimpleDateFormat
import java.util.*

class ActiveDateCarsResource(var dsateFrom: Date, var x: Double, var y: Double) : Resource() {
    companion object {
        val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale.ITALY)
    }

    override fun getURL(): String {
        val parseDate = dateParser.format(dsateFrom)
        return "$BASE_ADDRESS/cars/activebydate/$parseDate/$x/$y"
    }
}