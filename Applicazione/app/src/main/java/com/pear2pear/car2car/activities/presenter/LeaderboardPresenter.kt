package com.pear2pear.car2car.activities.presenter

import android.content.Context
import co.metalab.asyncawait.async
import com.pear2pear.car2car.activities.contract.LeaderboardContract
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.restcalls.GameRestCalls
import java.lang.Exception

class LeaderboardPresenter(val context: Context): LeaderboardContract.Presenter {

    override var view: LeaderboardContract.View? = null

    var token:String=""

    override fun setCurrentToken(token: String) {
        this.token = token
    }

    override fun downloadLeaderBoard(completeListener: (ArrayList<User>) -> Unit) {
        try {
            async {
                val list = await { GameRestCalls(token).getLeaderBoard() }
                completeListener(list)
            }
        } catch (e: Exception) { view?.showError() }
    }

    override fun setLeaderBoardAdapter(leaderboard: ArrayList<User>) {
        view?.hideProgressBar()
        view?.setLeaderBoardAdapter(leaderboard)
    }

}