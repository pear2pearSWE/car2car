package com.pear2pear.car2car.activities.contract

import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.User

interface UserProfileContract {
    interface Presenter : BasePresenter<View> {
        fun setCurrentToken(token: String)
        fun downloadUserCars(profileUsername: String, completeListener: (ArrayList<Car>) -> Unit)
        fun setUserCarsAdapter(carsList: ArrayList<Car>)
        fun downloadUserInfo(profileUsername: String, completeListener: (User) -> Unit)
        }

    interface View {
        fun initPresenter()
        fun setUserCarsAdapter(myCarsList: ArrayList<Car>)
        fun showUserCars()
        fun hideProgressBar()
        fun showUserInfo()
        fun showError()
    }
}