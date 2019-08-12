package com.pear2pear.car2car.activities.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.Rent
import com.pear2pear.car2car.assets.User

interface CarViewContract {
    interface Presenter : BasePresenter<View> {
        fun getCar(id:String)
        fun getUser(user:String)
        fun insertRentRequest(id: Rent)
        fun setCurrentToken(token:String)
    }

    interface View {
        fun hideProgressBar()
        fun initPresenter()
        fun returntoken():String
        fun returnUser():String
        fun setViewCar(car:Car)
        fun showCarData(car:Car)
        fun showUserData(user: User)
        fun closeView()

    }
}