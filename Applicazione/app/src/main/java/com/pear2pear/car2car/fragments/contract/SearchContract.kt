package com.pear2pear.car2car.fragments.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Car

interface SearchContract {
    interface Presenter : BasePresenter<View> {
        fun downloadSearchList(completeListener: (ArrayList<Car>) -> Unit)
        fun setSearchAdapter(myCarsList: ArrayList<Car>)
        fun refresh()
    }

    interface View {
        fun initPresenter()
        fun setCurrentSession(newSession: CognitoUserSession?)
        fun setSearchAdapter(searchList: ArrayList<Car>)
        fun showSearchList()
        fun hideProgressBar()
        fun returnSession():CognitoUserSession?
        fun refresh()
        fun showError()
        fun showLabelNoCars()
    }
}