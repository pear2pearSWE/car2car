package com.pear2pear.car2car.cognito.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession

interface LoginContract {
    interface Presenter {
        fun verifyLogin(user: String, psw: String)
    }

    interface View {
        fun showError()
        fun hideDialog(session: CognitoUserSession?)
    }
}