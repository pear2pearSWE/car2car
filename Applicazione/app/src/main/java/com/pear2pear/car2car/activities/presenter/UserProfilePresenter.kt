package com.pear2pear.car2car.activities.presenter

import android.content.Context
import co.metalab.asyncawait.async
import com.pear2pear.car2car.activities.contract.UserProfileContract
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.restcalls.CarRestCalls
import com.pear2pear.car2car.restcalls.GameRestCalls
import java.lang.Exception

class UserProfilePresenter(val context: Context): UserProfileContract.Presenter {

    override var view: UserProfileContract.View? = null

    var token:String=""

    override fun setCurrentToken(token: String) {
        this.token = token
    }

    override fun downloadUserCars(profileUsername: String, completeListener: (ArrayList<Car>) -> Unit) {
        try {
            async {
                val list =
                    await { CarRestCalls(token).getCarsByName(profileUsername) }
                completeListener(list)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun downloadUserInfo(profileUsername: String, completeListener: (User) -> Unit) {
        try {
            async {
                val user = await { GameRestCalls(token).getUserGame(profileUsername) }
                completeListener(user)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun setUserCarsAdapter(carsList: ArrayList<Car>) {
        view?.hideProgressBar()
        view?.setUserCarsAdapter(carsList)
    }

}