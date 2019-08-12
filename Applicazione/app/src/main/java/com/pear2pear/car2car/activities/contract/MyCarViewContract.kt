package com.pear2pear.car2car.activities.contract

import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.Rent

interface MyCarViewContract {
    interface Presenter : BasePresenter<MyCarViewContract.View> {
        fun getCar(id:String)
        fun deleteCar()
        fun initDelete()
    }
    interface View {
        fun showDeleteCarDialog(car:Car)
        fun hideProgressBar()
        fun initPresenter()
        fun returntoken():String
        fun returnUser():String
       /* fun setViewCar(car: Car)*/
        fun showData(car: Car)
        fun closeView()

    }
}