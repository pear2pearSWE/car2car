package com.pear2pear.car2car.fragments.presenter

import android.content.Context
import co.metalab.asyncawait.async
import com.pear2pear.car2car.assets.Rent
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.fragments.contract.NotificationContract
import com.pear2pear.car2car.restcalls.RentRestCalls
import java.lang.Exception

class NotificationPresenter(val context: Context) : NotificationContract.Presenter {

    override var view: NotificationContract.View? = null

    override fun downloadRentsInList(completeListener: (ArrayList<Rent>) -> Unit) {
        try {
            async {
                val inLambdaList =
                    await { RentRestCalls().getReceivedRents(CognitoSettings(context).getUserPool().currentUser.userId) }
                completeListener(inLambdaList)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun setRentsInListAdapter(rentsInList: ArrayList<Rent>) {
        view?.hideInProgressBar()
        view?.setRentsInListAdapter(rentsInList)
    }

    override fun downloadRentsOutList(completeListener: (ArrayList<Rent>) -> Unit) {
        try {
            async {
                val outLambdaList =
                    await { RentRestCalls().getMyRents(CognitoSettings(context).getUserPool().currentUser.userId) }
                completeListener(outLambdaList)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun setRentsOutListAdapter(rentsOutList: ArrayList<Rent>) {
        view?.hideOutProgressBar()
        view?.setRentsOutListAdapter(rentsOutList)
    }

    override fun refresh() {
        view?.refresh()
    }
}