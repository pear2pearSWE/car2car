package com.pear2pear.car2car.activities.contract

import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Car

interface CarInsertContract {
    interface Presenter : BasePresenter<CarInsertContract.View> {
        fun InsertCar(car: Car)
        fun setCurrentToken(token: String)
        fun verifyFirstCar()
        fun insert(car:Car)
    }


    interface View {
        fun showFirstCar()
        fun closeView()
        fun initPresenter(token: String)
        fun showError()
    }
}
