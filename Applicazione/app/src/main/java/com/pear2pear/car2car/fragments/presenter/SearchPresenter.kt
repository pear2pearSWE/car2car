package com.pear2pear.car2car.fragments.presenter

import android.content.Context
import android.os.Build
import co.metalab.asyncawait.async
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.fragments.contract.SearchContract
import com.pear2pear.car2car.restcalls.CarRestCalls
import java.util.*
import kotlin.collections.ArrayList

class SearchPresenter(val context: Context) : SearchContract.Presenter {

    override var view: SearchContract.View? = null

    val session=view?.returnSession()

    override fun downloadSearchList(completeListener: (ArrayList<Car>) -> Unit) {
        try {
            async {
                val cal = Calendar.getInstance()
                cal.set(2019, 5 - 1, 1)

                val lambdaList: ArrayList<Car>
                if (view?.returnSession() == null)
                    lambdaList = await { CarRestCalls().getActiveByDateExcluding(cal.time, 1.0, 1.0, "") }
                else
                    lambdaList = await {
                        CarRestCalls().getActiveByDateExcluding(
                            cal.time,
                            1.0,
                            1.0,
                            CognitoSettings(context).getUserPool().currentUser.userId
                        )
                    }
                completeListener(lambdaList)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun setSearchAdapter(myCarsList: ArrayList<Car>) {
        view?.hideProgressBar()
        view?.setSearchAdapter(myCarsList)
    }

    override fun refresh(){
        view?.refresh()
    }
}