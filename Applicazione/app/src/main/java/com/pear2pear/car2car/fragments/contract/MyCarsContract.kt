package com.pear2pear.car2car.fragments.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.User

interface MyCarsContract {
    interface Presenter : BasePresenter<View> {
        fun setCurrentToken(token: String)
        fun downloadUser(completeListener: (User) -> Unit)
        fun downloadMyCarsList(completeListener: (ArrayList<Car>) -> Unit) // download della lista
        fun setMyCarsAdapter(myCarsList: ArrayList<Car>) // adattamento per grafica dinamica
        fun refresh()
    }

    interface View {
        fun clickedBtAddCar()
        fun initPresenter()
        fun setMyCarsAdapter(myCarsList: ArrayList<Car>)
        fun showMyCarsList()
        fun setUserLevel()
        fun hideProgressBar()
        fun setCurrentSession(session: CognitoUserSession)
        fun returnCurrentSession():CognitoUserSession
        fun returnSession(): CognitoUserSession?
        fun refresh()
        fun showError()
        fun showLabelNoCars()
    }
}