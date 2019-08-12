package com.pear2pear.car2car.activities.presenter

import android.content.Context
import co.metalab.asyncawait.async
import com.pear2pear.car2car.activities.contract.CarViewContract
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.Rent
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.restcalls.CarRestCalls
import com.pear2pear.car2car.restcalls.GameRestCalls
import com.pear2pear.car2car.restcalls.RentRestCalls

class CarViewPresenter(var context: Context):CarViewContract.Presenter {

    override var view: CarViewContract.View?=null

    var token=""

    override fun getCar(id:String) {
        async{
            var car=Car("","","","","",0.0,"")
            await{
                car=CarRestCalls(view?.returntoken()).getCarById(view?.returnUser()!!, id)
            }
            view?.hideProgressBar()
            view?.showCarData(car)
        }
    }

    override fun getUser(userId: String) {

        async {
          val user = await { GameRestCalls(token).getUserGame(userId) }
            view?.showUserData(user)
        }

    }

    override fun setCurrentToken(token: String) {
        this.token=token
    }

    override fun insertRentRequest(id:Rent) {
       async{
           await{RentRestCalls(view?.returntoken()).insertRentRequest(id)}
       }
        view?.closeView()
    }



}