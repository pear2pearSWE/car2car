package com.pear2pear.car2car.assets

class Mission(
    var title: String,
    var description: String,
    var threshold: Int,
    var prize: Int,
    var imgreward: String,
    var value: Double,
    var complete: Boolean
){
    fun getStatus(): Double = (value/threshold)*100
}