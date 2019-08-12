package com.pear2pear.car2car.assets

class Car(
    var brand: String,
    var model: String,
    var plates: String,
    var position: String,
    var registrationDate: String,
    var price: Double,
    var description: String = "",
    var username: String = "polo",
    var sharingPeriodStart: String? = null,
    var sharingPeriodEnd: String? = null,
    var using: Boolean? = null,
    var carID: String = "777"
)