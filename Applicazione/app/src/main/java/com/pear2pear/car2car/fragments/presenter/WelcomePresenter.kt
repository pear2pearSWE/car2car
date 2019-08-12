package com.pear2pear.car2car.fragments.presenter

import android.content.Context
import android.widget.Toast
import co.metalab.asyncawait.async
import com.pear2pear.car2car.assets.Mission
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.fragments.contract.WelcomeContract
import com.pear2pear.car2car.restcalls.GameRestCalls

class WelcomePresenter(val context: Context) : WelcomeContract.Presenter {

    override var view: WelcomeContract.View? = null

    var token = ""

    override fun setCurrentToken(token: String) {
        this.token = token
    }

    override fun showWelcomeUser() {
        var user: User
        try {
            async {
                user =
                    await { GameRestCalls(token).getUserGame(CognitoSettings(context).getUserPool().currentUser.userId) }
                view?.showWelcomeUsername(user.user)
                view?.showWelcomeAvatar(user.avatar)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun downloadMissionsList(user: String, completeListener: (ArrayList<Mission>) -> Unit) {
        try {
            async {
                val list = await { GameRestCalls(token).getUserMissions(user) }
                completeListener(list)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun setMissionAdapter(missionsList: ArrayList<Mission>) {
        view?.hideProgressBar()
        view?.setMissionAdapter(missionsList)
    }

    override fun refresh(){
        view?.refresh()
    }
}