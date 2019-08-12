package com.pear2pear.car2car.cognito.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.pear2pear.car2car.BasePresenter

interface SignUpContract {
    interface Presenter : BasePresenter<View> {
        fun signUp()
    }

    interface View {
        fun initPresenter()
        fun showConfirmCode(user: String)
        fun showCalendar()
        fun showError()
        fun getAttribute(): CognitoUserAttributes
        fun getInsertedUser(): String
        fun getInsertedPassword(): String
        fun closeView()
    }
}