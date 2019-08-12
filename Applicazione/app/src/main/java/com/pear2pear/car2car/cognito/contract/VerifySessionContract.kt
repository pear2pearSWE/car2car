package com.pear2pear.car2car.cognito.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession

interface VerifySessionContract {
    interface Presenter {
        fun verify()
    }

    interface View {
        fun showNotLogged()
        fun showWelcomeTutorialDialog()
        fun showSearchTutorialDialog()
        fun showMyCarTutorialDialog()
        fun showNotificationTutorialDialog()
        fun showEndTutorialDialog()
        fun showInfoBoxDialog()

        fun showLogged(session: CognitoUserSession?)
        fun showProgressBar()
        fun hideProgressBar()
    }
}