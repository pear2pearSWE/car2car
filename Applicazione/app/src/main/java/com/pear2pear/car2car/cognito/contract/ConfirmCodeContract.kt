package com.pear2pear.car2car.cognito.contract

import com.pear2pear.car2car.BasePresenter

interface ConfirmCodeContract {
    interface Presenter : BasePresenter<ConfirmCodeContract.View> {
        fun confirm(user: String, psw: String)
    }

    interface View {
        fun initPresenter()
        fun showError()
        fun showSuccess()
    }
}