package com.pear2pear.car2car.fragments.presenter

import android.content.Context
import android.widget.Toast
import co.metalab.asyncawait.async
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.fragments.contract.MyCarsContract
import com.pear2pear.car2car.restcalls.CarRestCalls
import com.pear2pear.car2car.restcalls.GameRestCalls
import java.lang.Exception

class MyCarsPresenter(val context: Context) : MyCarsContract.Presenter {

    override var view: MyCarsContract.View? = null

    var token = ""

    override fun setCurrentToken(token: String) {
        this.token = token
    }

    override fun downloadMyCarsList(completeListener: (ArrayList<Car>) -> Unit) {
        try {
            async {
                val list =
                    await { CarRestCalls().getCarsByName(CognitoSettings(context).getUserPool().currentUser.userId) }
                completeListener(list)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun downloadUser(completeListener: (User) -> Unit) {
        try {
            async {
                val user =
                    await { GameRestCalls(token).getUserGame(CognitoSettings(context).getUserPool().currentUser.userId) }
                completeListener(user)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun setMyCarsAdapter(myCarsList: ArrayList<Car>) {
        view?.hideProgressBar()
        view?.setMyCarsAdapter(myCarsList)
    }

    override fun refresh(){
        view?.refresh()
    }
}