package com.pear2pear.car2car.cognito.presenter


import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.pear2pear.car2car.cognito.ConfirmCodeActivity
import com.pear2pear.car2car.cognito.model.CognitoIdentityProvider
import com.pear2pear.car2car.cognito.contract.ConfirmCodeContract
import java.lang.Exception


class ConfirmCodePresenter(val context:Context) : ConfirmCodeContract.Presenter {

    override var view: ConfirmCodeContract.View?=null

    var model: CognitoIdentityProvider = CognitoIdentityProvider(context)

    val handler = object : GenericHandler {
        override fun onSuccess() {
            view?.showSuccess()
        }

        override fun onFailure(exception: Exception?) {
            view?.showError()
        }
    }

    override fun confirm(user: String, code: String) {
        model.confirmUser(handler, user, code)
    }


}

