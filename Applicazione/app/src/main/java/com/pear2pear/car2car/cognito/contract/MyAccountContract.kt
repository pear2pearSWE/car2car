package com.pear2pear.car2car.cognito.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.pear2pear.car2car.assets.User

interface MyAccountContract {
    interface Presenter {
        fun getDetails()
        fun confirmDeleteUser()
        fun deleteUser()
        fun getLevel()
        fun setCurrentToken(token: String)
    }

    interface View {
        fun showGameInfo(result: User)
        fun showDetails(list: CognitoUserDetails)
        fun showDialog() //conferma eliminazione utente
        fun closeView()
    }
}