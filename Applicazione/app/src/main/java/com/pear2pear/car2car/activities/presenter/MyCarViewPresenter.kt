package com.pear2pear.car2car.activities.presenter

import android.content.Context
import android.widget.Toast
import co.metalab.asyncawait.async
import com.pear2pear.car2car.activities.contract.MyCarViewContract
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.restcalls.CarRestCalls
import java.lang.Exception

class MyCarViewPresenter( var context: Context) : MyCarViewContract.Presenter{
    override var view: MyCarViewContract.View?=null

    var car= Car("","","","","",0.0,"")

    override fun getCar(id:String) {
        async{

            await{
                car= CarRestCalls(view?.returntoken()).getCarById(view?.returnUser()!!, id)
            }
            view?.hideProgressBar()
            view?.showData(car)
        }
    }

    override fun deleteCar() {
        async{
            await{
                try{
                    CarRestCalls(view?.returntoken()).deleteCar(CognitoSettings(context).getUserPool().currentUser.userId,car)
                }
            catch(e:Exception){

            }}
        }
    }

    override fun initDelete(){
        view?.showDeleteCarDialog(car)
    }
}