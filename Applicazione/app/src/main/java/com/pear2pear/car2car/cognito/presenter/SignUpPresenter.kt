package com.pear2pear.car2car.cognito.presenter

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.pear2pear.car2car.cognito.contract.SignUpContract
import com.pear2pear.car2car.cognito.model.CognitoIdentityProvider
import java.lang.Exception

class SignUpPresenter(val context: Context): SignUpContract.Presenter {

    override var view: SignUpContract.View? = null

    var model = CognitoIdentityProvider(context)

    val signupCallback = object: SignUpHandler {
        override fun onSuccess(
            user: CognitoUser?,
            signUpConfirmationState: Boolean,
            cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails?
        ) {
            view?.showConfirmCode(user!!.userId)
            view?.closeView()
        }

        override fun onFailure(exception: Exception?) {
            view?.showError()
        }
    }

    override fun signUp(){
        model.signUp(view!!.getInsertedUser(),view!!.getInsertedPassword(),signupCallback,view!!.getAttribute())
    }
}