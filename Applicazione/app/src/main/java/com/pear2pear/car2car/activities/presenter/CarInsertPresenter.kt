package com.pear2pear.car2car.activities.presenter

import android.content.Context
import android.widget.Toast
import co.metalab.asyncawait.async
import com.pear2pear.car2car.activities.contract.CarInsertContract
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.restcalls.CarRestCalls
import com.pear2pear.car2car.restcalls.GameRestCalls
import java.lang.Exception


class CarInsertPresenter(var context: Context) : CarInsertContract.Presenter {

    var token=""

    override var view: CarInsertContract.View? = null

    override fun InsertCar(car: Car) {

        async {
            try{
                await { CarRestCalls(token).insertCar(CognitoSettings(context).getUserPool().currentUser.userId, car) }

            }
            catch (e:Exception){
                view?.showError()
            }
        }
    }

    override fun verifyFirstCar() {
        async {
            var a = -1
            try{
                await { a = GameRestCalls(token).insertFirstAuto(CognitoSettings(context).getUserPool().currentUser.userId) }
                if(a != -1 ){
                    view?.showFirstCar()
                }
                view?.closeView()
            }
            catch (e:Exception){
                view?.showError()
            }
        }
    }

    override fun insert(car:Car){
        InsertCar(car)
        verifyFirstCar()
    }

    override fun setCurrentToken(jwt:String){
        this.token=jwt
    }
}