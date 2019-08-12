package com.pear2pear.car2car.activities.contract

import com.pear2pear.car2car.BasePresenter
import com.pear2pear.car2car.assets.User

interface LeaderboardContract {
    interface Presenter : BasePresenter<View> {
        fun downloadLeaderBoard(completeListener: (ArrayList<User>) -> Unit)
        fun setLeaderBoardAdapter(leaderboard: ArrayList<User>)
        fun setCurrentToken(token:String)
    }

    interface View {
        fun initPresenter()
        fun setLeaderBoardAdapter(leaderboard: ArrayList<User>)
        fun hideProgressBar()
        fun showLeaderBoard()
        fun showError()
    }
}