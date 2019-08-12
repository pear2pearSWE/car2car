package com.pear2pear.car2car.fragments.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Rent

interface NotificationContract {
    interface Presenter : BasePresenter<View> {
        fun downloadRentsInList(completeListener: (ArrayList<Rent>) -> Unit)
        fun setRentsInListAdapter(rentsInList: ArrayList<Rent>)
        fun downloadRentsOutList(completeListener: (ArrayList<Rent>) -> Unit)
        fun setRentsOutListAdapter(rentsOutList: ArrayList<Rent>)
        fun refresh()
    }

    interface View {
        fun initPresenter()
        fun showConfirmRentDialog(id: String)
        fun showConfirmKeyDialog(id: String)
        fun showConfirmEndDialog(id: String)
        fun setRentsInListAdapter(inList: ArrayList<Rent>)
        fun setRentsOutListAdapter(outList: ArrayList<Rent>)
        fun showRentsInList()
        fun showRentsOutList()
        fun hideInProgressBar()
        fun hideOutProgressBar()
        fun showLabelNoInRents()
        fun showLabelNoOutRents()
        fun setCurrentSession(session: CognitoUserSession)
        fun returnCurrentSession(): CognitoUserSession
        fun refresh()
        fun showError()
        fun showGenericDialog(title: String, message: String?)
    }
}