package com.pear2pear.car2car.activities.presenter

import android.content.Context
import co.metalab.asyncawait.async
import com.pear2pear.car2car.activities.contract.ChooseAvatarContract
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.restcalls.GameRestCalls

class ChooseAvatarPresenter(val context: Context): ChooseAvatarContract.Presenter {

    override var view: ChooseAvatarContract.View? = null

    var token:String=""

    override fun setCurrentToken(token: String) {
        this.token = token
    }

    override fun downloadUserInfo() {
        var user: User
        try {
            async {
                user =
                    await { GameRestCalls(token).getUserGame(CognitoSettings(context).getUserPool().currentUser.userId) }
                view?.showUserUsername(user.user)
                view?.showUserLevel(user.getLevel())
                view?.showUserAvatar(user.avatar)
                view?.showAllAvatar()
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun changeAvatar(avatar: String) {
        try {
            async{
                await{ GameRestCalls(token).changeAvatar(CognitoSettings(context).getUserPool().currentUser.userId,avatar)}
            }
        } catch (e: java.lang.Exception) { view?.showError() }
    }

}