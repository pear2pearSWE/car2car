package com.pear2pear.car2car.assets

import java.util.*

class Rent(
    var user: String,
    var toUser: String,
    var idCar: String,
    var date: Date = Calendar.getInstance().time,
    var isAccepted: Boolean = false,
    var hasKeys: Boolean = false,
    var isEnded: Boolean = false,
    var id: String = "",
    var reason: String? = null
)