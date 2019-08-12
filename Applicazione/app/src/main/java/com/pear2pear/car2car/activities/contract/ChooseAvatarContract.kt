package com.pear2pear.car2car.activities.contract

import android.support.constraint.ConstraintLayout
import android.widget.ImageView
import com.pear2pear.car2car.BasePresenter

interface ChooseAvatarContract {
    interface Presenter : BasePresenter<View> {
        fun setCurrentToken(token: String)
        fun downloadUserInfo()
        fun changeAvatar(avatar: String)
    }

    interface View {
        fun initPresenter()
        fun hideCover(icon: ImageView, unlock: Int)
        fun setAllListener()
        fun setListener(layout: ConstraintLayout, unlock: Int, avatar: String)
        fun showAllAvatar()
        fun showUserUsername(username: String)
        fun showUserLevel(level : Int)
        fun showUserAvatar(avatar: String)
        fun showError()
    }
}