package com.pear2pear.car2car.fragments.contract

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.Mission
import com.pear2pear.car2car.assets.User

interface WelcomeContract {
    interface Presenter : BasePresenter<View> {
        fun setCurrentToken(token: String)
        fun showWelcomeUser()
        fun downloadMissionsList(user: String, completeListener: (ArrayList<Mission>) -> Unit)
        fun setMissionAdapter(missionsList: ArrayList<Mission>)
        fun refresh()
    }

    interface View {
        fun initPresenter()
        fun showWelcomeAvatar(avatar: String)
        fun showWelcomeUsername(username: String)
        fun setMissionAdapter(missionsList: ArrayList<Mission>)
        fun hideProgressBar()
        fun showMissionsList(user: String)
        fun setCurrentSession(session: CognitoUserSession)
        fun returnCurrentSession(): CognitoUserSession
        fun returnSession(): CognitoUserSession?
        fun refresh()
        fun showError()
    }
}